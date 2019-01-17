package com.zlsk.zTool.utils.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.Looper;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import com.zlsk.zTool.customControls.scaleImage.ImageSource;
import com.zlsk.zTool.customControls.scaleImage.ZScaleImageView;
import com.zlsk.zTool.utils.file.FileUtil;
import com.zlsk.zTool.utils.string.StringUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public class BitmapUtil {
    private static final float PHOTO_DEFAULT_MAX_WIDTH_AND_HEIGHT = 1280;


    public static Bitmap transDrawableToBitmap(Drawable drawable){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        return bitmapDrawable.getBitmap();
    }

    /**
     * Resize the bitmap
     *
     * @param bitmap 图片引用
     * @param width 宽度
     * @param height 高度
     * @return 缩放之后的图片引用
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
    }

    /**
     * 部分手机拍照后照片会旋转
     */
    public static String amendRotatePhoto(String originpath) {

        // 取得图片旋转角度
        int angle = readPictureDegree(originpath);

        // 把原图压缩后得到Bitmap对象
        Bitmap bmp = getCompressPhoto(originpath);

        // 修复图片被旋转的角度
        Bitmap bitmap = rotaingImageView(angle, bmp);

        // 保存修复后的图片并返回保存后的图片路径
        return savePhotoToSD(originpath,bitmap);
    }

    /**
     * 保存Bitmap图片在SD卡中
     * 如果没有SD卡则存在手机中
     *
     * @param bitmap 需要保存的Bitmap图片
     * @return 保存成功时返回图片的路径，失败时返回null
     */
    public static String savePhotoToSD(String filePath,Bitmap bitmap) {
        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(filePath);
            // 把数据写入文件，100表示不压缩
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (outStream != null) {
                    // 记得要关闭流！
                    outStream.close();
                }
                if (bitmap != null) {
                    bitmap.recycle();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param path 原图的路径
     * @return 压缩后的图片
     */
    public static Bitmap getCompressPhoto(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 4;
        Bitmap bmp = BitmapFactory.decodeFile(path, options);
        System.out.println( "照片大小 ：" + bmp.getRowBytes() * bmp.getHeight());
        options = null;
        return bmp;
    }

    /**
     * 旋转图片
     * @param angle 被旋转角度
     * @param bitmap 图片对象
     * @return 旋转后的图片
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }

    /**
     * 读取照片旋转角度
     *
     * @param path 照片路径
     * @return 角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap transImage(String fromFile) throws IOException {
        Bitmap bitmap = BitmapFactory.decodeFile(fromFile);
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        float width = Float.valueOf(bitmapWidth);
        float height = Float.valueOf(bitmapHeight);
        float max = height > width ? height : width;
        float scale = PHOTO_DEFAULT_MAX_WIDTH_AND_HEIGHT < max ? (PHOTO_DEFAULT_MAX_WIDTH_AND_HEIGHT / max) : 1;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, false);
    }

    public static Bitmap transImage(int width,int height,String fromFile){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.outWidth = width;
        options.outHeight = height;
        return BitmapFactory.decodeFile(fromFile,options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;

        reqWidth = width / 2;
        reqHeight = height / 2;

        int inSampleSize = 1;
        if (width > height) {
            int tmp = reqWidth;
            reqWidth = reqHeight;
            reqHeight = tmp;
        }
        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /**
     * 图像压缩后解析
     * @param pathName 图片地址
     * @param reqWidth 指定宽度
     * @param reqHeight 指定高度
     * @return
     */
    public static Bitmap decodeBitmapFromFile(String pathName, int reqWidth, int reqHeight) {
        // First decode build inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap build inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName, options);
    }

    public static Bitmap addWatermark(Bitmap srcBitmap, String time,String location){
        if(time == null || location == null){
            return srcBitmap;
        }

        String familyName = "Arial";
        Typeface font = Typeface.create(familyName, Typeface.NORMAL);
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(40);
        textPaint.setColor(Color.parseColor("#f7c215"));
        textPaint.setTypeface(font);

        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();

        Bitmap newBmp = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBmp);
        canvas.drawBitmap(srcBitmap, 0, 0, null);

        canvas.translate(200,20);
        StaticLayout layout = new StaticLayout(time,textPaint,srcWidth - 400, Layout.Alignment.ALIGN_NORMAL,1.0F,0.0F,true);
        layout.draw(canvas);

        if(!location.equals("")){
            canvas.translate(0,60);
            layout = new StaticLayout(location,textPaint,srcWidth - 400, Layout.Alignment.ALIGN_NORMAL,1.0F,0.0F,true);
            layout.draw(canvas);
        }

        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return newBmp;
    }

    public static String getVideoThumbnailPath(String videoPath) {
        String thumbnailName = "";
        File file = new File(videoPath);
        if(file.exists()){
            thumbnailName = file.getName();
        }else {
            thumbnailName = String.valueOf(System.currentTimeMillis());
        }
        String thumbnailPath = FileUtil.getInstance().getThumbnailPath() + "/t_" + thumbnailName + ".png";
        MediaMetadataRetriever media = new MediaMetadataRetriever();

        try{
            File thumbnail = new File(thumbnailPath);
            if(!thumbnail.exists()){
                if(videoPath.startsWith("http") ||
                        videoPath.startsWith("https")){
                    media.setDataSource(videoPath,new HashMap<String, String>());
                }else {
                    media.setDataSource(videoPath);
                }
                Bitmap bitmap = media.getFrameAtTime();
                savePhotoToSD(thumbnailPath,bitmap);
                media.release();
            }
        }catch (Exception exc){
            thumbnailPath = "";
            media.release();
        }

        return thumbnailPath;
    }

    /**
     * 根据url路径 转bitmap
     *
     */
    public static Bitmap getBitmapFromUrl(String urlPath) {
        Bitmap map = null;
        if(StringUtil.isWebUrl(urlPath)){
            InputStream in;
            try {
                URL url = new URL(urlPath);
                URLConnection conn = url.openConnection();
                conn.connect();
                in = conn.getInputStream();
                map = BitmapFactory.decodeStream(in);
            } catch (IOException e) {
                return map;
            }
        }else {
            map = BitmapFactory.decodeFile(urlPath);
        }
        return map;
    }

    public static void bindScaleImageSource(final String url, final ZScaleImageView scaleImageView){
        //将图片下载下来,转成Bitmap
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ImageSource imageSource = ImageSource.bitmap(BitmapUtil.getBitmapFromUrl(url));
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        scaleImageView.setImage(imageSource);
                    }
                });
            }
        }).start();
    }
}
