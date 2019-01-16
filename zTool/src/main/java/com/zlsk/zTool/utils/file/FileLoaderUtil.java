package com.zlsk.zTool.utils.file;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.FileObserver;
import android.support.v4.content.AsyncTaskLoader;

import com.zlsk.zTool.model.photo.FileModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FileLoaderUtil extends AsyncTaskLoader<List<FileModel>> {

    private static final int FILE_OBSERVER_MASK = FileObserver.CREATE | FileObserver.DELETE | FileObserver.DELETE_SELF | FileObserver.MOVED_FROM | FileObserver.MOVED_TO
            | FileObserver.MODIFY | FileObserver.MOVE_SELF;

    private FileObserver mFileObserver;

    private List<FileModel> mData;
    private String mPath;

    public FileLoaderUtil(Context context, String path) {
        super(context);
        this.mPath = path;
    }

    @Override
    public List<FileModel> loadInBackground() {

        ArrayList<FileModel> list = new ArrayList<FileModel>();

        // Current directory File instance
        final File pathDir = new File(mPath);

        // List file in this directory build the directory filter
        final File[] dirs = pathDir.listFiles(FileSuffixUtils.sDirFilter);
        if (dirs != null) {
            // Sort the folders alphabetically
            Arrays.sort(dirs, FileSuffixUtils.sComparator);
            // Add each folder to the File list for the list adapter
            for (File dir : dirs) {
                list.add(adapt2FileModel(dir));
            }
        }

        // List file in this directory build the file filter
        final File[] files = pathDir.listFiles(FileSuffixUtils.mFileFileterBySuffixs);
        if (files != null) {
            // Sort the files alphabetically
            Arrays.sort(files, FileSuffixUtils.sComparator);
            // Add each file to the File list for the list adapter
            for (File file : files)
                list.add(adapt2FileModel(file));
        }

        return list;
    }

    @Override
    public void deliverResult(List<FileModel> data) {
        if (isReset()) {
            onReleaseResources(data);
            return;
        }

        List<FileModel> oldData = mData;
        mData = data;

        if (isStarted())
            super.deliverResult(data);

        if (oldData != null && oldData != data)
            onReleaseResources(oldData);
    }

    @Override
    protected void onStartLoading() {
        if (mData != null)
            deliverResult(mData);

        if (mFileObserver == null) {
            mFileObserver = new FileObserver(mPath, FILE_OBSERVER_MASK) {
                @Override
                public void onEvent(int event, String path) {
                    onContentChanged();
                }
            };
        }
        mFileObserver.startWatching();

        if (takeContentChanged() || mData == null)
            forceLoad();
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();

        if (mData != null) {
            onReleaseResources(mData);
            mData = null;
        }
    }

    @Override
    public void onCanceled(List<FileModel> data) {
        super.onCanceled(data);

        onReleaseResources(data);
    }

    protected void onReleaseResources(List<FileModel> data) {

        if (mFileObserver != null) {
            mFileObserver.stopWatching();
            mFileObserver = null;
        }
    }

    private FileModel adapt2FileModel(File file) {
    	FileModel fileModel = new FileModel();
    	fileModel.setmFile(file);
    	fileModel.setChecked(false);

        return fileModel;
    }

    public static void openFile(Context context, String filePath) {
        File file = new File(filePath);
        if (file != null && file.exists() && file.length() > 0) {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            String type = FileLoaderUtil.getMIMEType(file.getName());
            intent.setDataAndType(Uri.fromFile(file), type);
            context.startActivity(intent);
        }
    }

    public static String getMIMEType(String name) {
        String type = "";
        String end = name.substring(name.lastIndexOf(".") + 1, name.length()).toLowerCase();
        if (end.equals("apk")) {
            return "application/vnd.android.package-archive";
        } else if (end.equals("mp4") || end.equals("avi") || end.equals("3gp") || end.equals("rmvb")) {
            type = "video";
        } else if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            type = "audio";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")) {
            type = "image";
        } else if (end.equals("txt") || end.equals("log")) {
            type = "text";
        } else {
            type = "*";
        }
        type += "/*";
        return type;
    }
}