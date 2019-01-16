package com.zlsk.zTool.utils.camera;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.zlsk.zTool.baseActivity.photo.AlbumSelectActivity;
import com.zlsk.zTool.constant.Constant;
import com.zlsk.zTool.dialog.ZToast;
import com.zlsk.zTool.utils.file.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by IceWang on 2018/5/25.
 */

public class StartCameraUtil {
    public static boolean checkSdCard(Context context){
        String status = Environment.getExternalStorageState();
        if (!status.equals(Environment.MEDIA_MOUNTED)) {
            ZToast.getInstance().show("没有储存卡");
            return false;
        }
        return true;
    }

    public static void launchCameraTakeVideo(Activity activity,int code,String dirName, String fileName){
        if(!checkSdCard(activity)){
            return;
        }

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;

        File file = new File(dirName, fileName);
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);

        if (currentapiVersion < 24) {
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        } else {
            //兼容android7.0
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            Uri uri = activity.getApplication().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        activity.startActivityForResult(intent,code);
    }

    /**
     * 启动手机自带相机应用工具
     * @param activity
     * @param code 自定义onActivity处理编号
     * @param dirName 图片存储的文件夹路径，不包括文件名
     *  @param fileName 照片的文件名
     */
    public static void launchCamera(Activity activity, int code, String dirName, String fileName) {
        if(!checkSdCard(activity)){
            return;
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(dirName, fileName);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                ZToast.getInstance().show("创建元文件失败,请检查权限");
                return;
            }
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getImageContentUri(activity,file));
        activity.startActivityForResult(intent, code);
    }

    /**
     * 转换 content:// uri
     *
     * @param imageFile
     * @return
     */
    public static Uri getImageContentUri(Context context,File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public static void getPhotoFromLocal(Context context,int limitCount,String albumName,int requestCode){
        ArrayList<String> preferredAlbumNames = FileUtil.getInstance().getPreferredAlbumNames();
        Intent intent = new Intent(context, AlbumSelectActivity.class);
        intent.putExtra(Constant.INTENT_FIELD_VIDEO_ALBUM, albumName);
        intent.putExtra(Constant.INTENT_FIELD_IMAGE_LIMIT_COUNT, limitCount);
        intent.putStringArrayListExtra(AlbumSelectActivity.INTENT_KEY_PREFERRED_ALBUM_NAMES, preferredAlbumNames);
        ((Activity)context).startActivityForResult(intent, requestCode);
    }
}
