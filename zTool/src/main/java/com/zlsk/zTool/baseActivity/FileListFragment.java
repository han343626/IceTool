package com.zlsk.zTool.baseActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;

import com.zlsk.zTool.R;
import com.zlsk.zTool.adapter.FileListAdapter;
import com.zlsk.zTool.model.photo.FileModel;
import com.zlsk.zTool.utils.file.FileLoaderUtil;

import java.util.List;

public class FileListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<FileModel>> {

    /**
     * Interface to listen for events.
     */
    public interface Callbacks {
        /**
         * Called when a file is selected from the list.
         *
         * @param file The file selected
         */
        void onFileSelected(FileModel file);

        void onFileSelected(List<FileModel> files);
    }

    private static final int LOADER_ID = 0;

    private FileChooserActivity mActivity;
    private FileListAdapter mAdapter;
    private String mPath;
    private boolean mListShown;
    private View mProgressContainer;
    private View mListContainer;
    private Callbacks mListener;

    /**
     * Create a new instance build the given file path.
     *
     * @param path The absolute path of the file (directory) to display.
     * @return A new Fragment build the given file path.
     */
    public static FileListFragment newInstance(String path) {
        FileListFragment fragment = new FileListFragment();
        Bundle args = new Bundle();
        args.putString(FileChooserActivity.PATH, path);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (Callbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement FileListFragment.Callbacks");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() instanceof FileChooserActivity) {
            mActivity = (FileChooserActivity) getActivity();
        }
        mAdapter = new FileListAdapter(getActivity());
        mPath = getArguments() != null ? getArguments().getString(FileChooserActivity.PATH) : Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View contentView = inflater.inflate(R.layout.fragment_container, container, false);
        mListContainer = contentView.findViewById(R.id.listContainer);
        mProgressContainer = contentView.findViewById(R.id.progressContainer);
        mListShown = true;

        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setEmptyText("Empty Directory");
        setListAdapter(mAdapter);
        setListShown(false);

        getLoaderManager().initLoader(LOADER_ID, null, this);

        setBaseActivityListener();

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        FileListAdapter adapter = (FileListAdapter) l.getAdapter();
        if (adapter != null) {
            FileModel file = adapter.getItem(position);
            mPath = file.getmFile().getAbsolutePath();
            mListener.onFileSelected(file);
        }
    }

    @NonNull
    @Override
    public Loader<List<FileModel>> onCreateLoader(int id, Bundle args) {
        return new FileLoaderUtil(getActivity(), mPath);
    }

    @Override
    public void onLoadFinished(Loader<List<FileModel>> loader, List<FileModel> data) {
        mAdapter.setListItems(data);

        if (isResumed())
            setListShown(true);
        else
            setListShownNoAnimation(true);
    }

    @Override
    public void onLoaderReset(Loader<List<FileModel>> loader) {
        mAdapter.clear();
    }

    @Override
    public void setEmptyText(CharSequence text) {
        TextView emptyView = (TextView) getListView().getEmptyView();
        emptyView.setVisibility(View.GONE);
        emptyView.setText(text);
    }

    public void setListShown(boolean shown, boolean animate) {
        if (mListShown == shown) {
            return;
        }
        mListShown = shown;
        if (shown) {
            if (animate) {
                mProgressContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
                mListContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
            } else {
                mProgressContainer.clearAnimation();
                mListContainer.clearAnimation();
            }
            mProgressContainer.setVisibility(View.GONE);
            mListContainer.setVisibility(View.VISIBLE);
        } else {
            if (animate) {
                mProgressContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
                mListContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
            } else {
                mProgressContainer.clearAnimation();
                mListContainer.clearAnimation();
            }
            mProgressContainer.setVisibility(View.VISIBLE);
            mListContainer.setVisibility(View.INVISIBLE);
        }
    }

    public void setListShown(boolean shown) {
        setListShown(shown, true);
    }

    public void setListShownNoAnimation(boolean shown) {
        setListShown(shown, false);
    }

    private void setBaseActivityListener() {
        if (null == mActivity) {
            return;
        }

        mActivity.setTitleBarCallBack(new FileChooserActivity.ITitleBarCallBack() {
			
			@Override
			public void onRightButtonClicked(View view) {
				mListener.onFileSelected(mAdapter.getListItems());
			}
			
			@Override
			public void onLeftButtonClicked(View view) {
			}
		});
    }
}
