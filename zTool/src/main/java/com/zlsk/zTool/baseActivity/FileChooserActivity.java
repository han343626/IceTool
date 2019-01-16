package com.zlsk.zTool.baseActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.BackStackEntry;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;

import com.zlsk.zTool.R;
import com.zlsk.zTool.dialog.ZToast;
import com.zlsk.zTool.model.photo.FileModel;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FileChooserActivity extends ATitleBaseFragmentActivity implements OnBackStackChangedListener, FileListFragment.Callbacks {
	public interface ITitleBarCallBack{
		void onLeftButtonClicked(View view);
		void onRightButtonClicked(View view);
	}
	
    public static final String PATH = "path";
    public static final String PATHS = "paths";
    public static final String EXTERNAL_BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    private ITitleBarCallBack callBack;
    private static final boolean HAS_ACTIONBAR = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;

    private FragmentManager mFragmentManager;
    private BroadcastReceiver mStorageListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ZToast.getInstance().show("内存卡异常");
            finishWithResults(null);
        }
    };

    private String mPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.addOnBackStackChangedListener(this);

        if (savedInstanceState == null) {
            mPath = EXTERNAL_BASE_PATH;
            addFragment();
        } else {
            mPath = savedInstanceState.getString(PATH);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterStorageListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerStorageListener();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(PATH, mPath);
    }

    @Override
    public void onBackStackChanged() {

        int count = mFragmentManager.getBackStackEntryCount();
        if (count > 0) {
            BackStackEntry fragment = mFragmentManager.getBackStackEntryAt(count - 1);
            mPath = fragment.getName();
        } else {
            mPath = EXTERNAL_BASE_PATH;
        }

//        setTitle(mPath);
        if (HAS_ACTIONBAR)
            invalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mFragmentManager.popBackStack();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Add the initial Fragment build given path.
     */
    private void addFragment() {
        FileListFragment fragment = FileListFragment.newInstance(mPath);
        mFragmentManager.beginTransaction().add(R.id.content, fragment).commit();
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_mediafile_chooser;
    }

    @Override
    public String getTitleString() {
        return "文件选择";
    }

    @Override
    public String getActionString() {
        return "确定";
    }

    @Override
    public boolean showRightImg() {
        return false;
    }

    @Override
    public void onBackButtonClicked(View view) {
    	if(null != callBack){
    		callBack.onLeftButtonClicked(view);
    	}
    	
    	int count = mFragmentManager.getBackStackEntryCount();
        if (count > 0) {
            BackStackEntry fragment = mFragmentManager.getBackStackEntryAt(count - 1);
            mPath = fragment.getName();
            mFragmentManager.popBackStack();
        } else {
            mPath = EXTERNAL_BASE_PATH;
            finish();
        }

        if (HAS_ACTIONBAR)
            invalidateOptionsMenu();
    }

    @Override
    public void onActionButtonClicked(View view) {
    	if(null != callBack){
    		callBack.onRightButtonClicked(view);
    	}
    }
    
    public void setTitleBarCallBack(ITitleBarCallBack callBack){
    	this.callBack = callBack;
    }
    
    public static boolean isHasActionbar() {
		return HAS_ACTIONBAR;
	}

	public FragmentManager getmFragmentManager() {
		return mFragmentManager;
	}

	/**
     * "Replace" the existing Fragment build a new one using given path. We're
     * really adding a Fragment to the back stack.
     * @param file The file (directory) to display.
     */
    private void replaceFragment(File file) {
        mPath = file.getAbsolutePath();

        FileListFragment fragment = FileListFragment.newInstance(mPath);
        mFragmentManager.beginTransaction().replace(R.id.content, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(mPath).commit();
    }

    /**
     * Finish this Activity build a result code and URI of the selected file.
     * @param file The file selected.
     */
    private void finishWithResult(File file) {
        if (file != null) {
            Uri uri = Uri.fromFile(file);
            setResult(RESULT_OK, new Intent().setData(uri));
            finish();
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    private void finishWithResults(List<File> files) {
    	Intent intent = new Intent();
        intent.putExtra(PATHS, (Serializable) files);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Called when the user selects a File
     * @param file The file that was selected
     */
    @Override
    public void onFileSelected(FileModel file) {
        if (file != null) {
            if (file.getmFile().isDirectory()) {
                replaceFragment(file.getmFile());
            } else {
                finishWithResult(file.getmFile());
            }
        } else {
            ZToast.getInstance().show("Error selecting File");
        }
    }

    /**
     * Register the external storage BroadcastReceiver.
     */
    private void registerStorageListener() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_REMOVED);
        registerReceiver(mStorageListener, filter);
    }

    /**
     * Unregister the external storage BroadcastReceiver.
     */
    private void unregisterStorageListener() {
        unregisterReceiver(mStorageListener);
    }

    @Override
    public void onFileSelected(List<FileModel> files) {
        if (null != files && files.size() > 0) {
            ArrayList<File> fileLists = new ArrayList<File>();
            for (FileModel file2 : files) {
                if (file2.isChecked()) {
                    fileLists.add(file2.getmFile());
                }
            }
            finishWithResults(fileLists);
        }
    }
}
