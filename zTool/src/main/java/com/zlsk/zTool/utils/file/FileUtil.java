package com.zlsk.zTool.utils.file;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Environment;

import com.zlsk.zTool.utils.system.BuildConfigUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by IceWang on 2018/5/29.
 */

public class FileUtil {
    public static final String TEXT_FONT_FOR_ARC_GIS = "DroidSansFallback.ttf";

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private static final String MPEG_FILE_PREFIX = "VID_";
    private static final String MPEG_FILE_SUFFIX = ".mp4";

    public static final String MAP_VERSION_FILE_NAME = "map_common_config";
    public static final String MAP_VERSION_KEY = "key_version";

    private Context context;
    @SuppressLint("StaticFieldLeak")
    private static FileUtil fileUtils;

    public String DIRECTORY_DCIM = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM;

    private String rootNode = "/zlsk/";
    private String mediaNode = "/media";
    private String imageCache = "/cache";
    private String imageNode = "/image";
    private String videoNode = "/video";
    private String thumbnailNode = "/thumbnail";
    private String mediaTempNode = "/temp";
    private String mapNode = "/map";
    private String mapDbNode = "/db";
    private String objDbNode = "/obj";
    private String mapTpkNode = "/tpk";
    private String mapShpNode = "/shp";
    private String storageNode = "/storage";
    private String crashNode = "/crash";
    private String fontNode = "/font";
    private String moduleName;
    private String rootPath;

    public static synchronized FileUtil getInstance(Context context) {
        if(fileUtils == null) {
            fileUtils = new FileUtil(context);
        }
        return fileUtils;
    }

    public static synchronized FileUtil getInstance() {
        return fileUtils;
    }

    private FileUtil(Context context) {
        this.context = context;
        moduleName = (String) BuildConfigUtil.getBuildConfigValue("MODULE_NAME");

        if(Environment.getExternalStorageState().equals("mounted")) {
            rootPath = Environment.getExternalStorageDirectory().getPath() + rootNode + moduleName;
        }else {
            rootPath = Environment.getRootDirectory().getPath() + rootNode + moduleName;
        }
    }

    public String getMediaPath(){
        String result = rootPath + mediaNode;
        hasFileDir(result);
        return result;
    }

    public String getMediaTempPath(){
        String result = getMediaPath() + mediaTempNode;
        hasFileDir(result);
        return result;
    }

    public String getImagePath(){
        String result = getMediaPath() + imageNode;
        hasFileDir(result);
        return result;
    }

    public String getImageCachePath(){
        String result = getImageCachePath() + imageCache;
        hasFileDir(result);
        return result;
    }

    public String getVideoPath(){
        String result = getMediaPath() + videoNode;
        hasFileDir(result);
        return result;
    }

    public String getThumbnailPath(){
        String result = getMediaPath() + thumbnailNode;
        hasFileDir(result);
        return result;
    }

    public String getStoragePath(){
        String result = rootPath + storageNode;
        hasFileDir(result);
        return result;
    }

    public String getCrashPath(){
        String result = rootPath + crashNode;
        hasFileDir(result);
        return result;
    }

    public String getMapPath(){
        String result = rootPath + mapNode;
        hasFileDir(result);
        return result;
    }

    public String getMapDbPath(){
        String result = getMapPath() + mapDbNode;
        hasFileDir(result);
        return result + "/";
    }

    public String getObjPath(){
        String result = getMapPath() + objDbNode;
        hasFileDir(result);
        return result + "/";
    }

    public String getMapTpkPath(){
        String result = getMapPath() + mapTpkNode;
        hasFileDir(result);
        return result + "/";
    }

    public String getmapShpPath(){
        String result = getMapPath() + mapShpNode;
        hasFileDir(result);
        return result + "/";
    }

    public String getFontPath(){
        String result = rootPath + fontNode;
        hasFileDir(result);
        return result + "/";
    }


    /**
     * 拍照后保存的位置
     */
    public String getZLSKShootImagePath(){
        String result = DIRECTORY_DCIM + "/" + moduleName;
        hasFileDir(result);
        return result;
    }

    /**
     * 拍视频后保存的位置
     */
    public String getZLSKRecordVideoPath(){
        String result = DIRECTORY_DCIM + "/" + moduleName;
        hasFileDir(result);
        return result;
    }

    public boolean isFileExist(String path){
        File file = new File(path);
        return file.exists();
    }

    public boolean hasFileDir(String var1) {
        File var2;
        if(!(var2 = new File(var1)).exists()) {
            var2.mkdirs();
        }

        return var2.exists();
    }

    /**
     * 获取可用相册路径
     */
    public ArrayList<String> getPreferredAlbumNames() {
        ArrayList<String> preferredAlbumNames = new ArrayList<String>();
        String deviceImageTopDirectory = Environment.getExternalStorageDirectory() + "/" + Environment.DIRECTORY_DCIM + "/";
        File deviceImageTopDirectoryFile = new File(deviceImageTopDirectory);
        if (deviceImageTopDirectoryFile.exists()) {
            preferredAlbumNames.add(deviceImageTopDirectoryFile.getName());
        }
        File[] deviceImageDirectories = new File(deviceImageTopDirectory).listFiles();
        for (File directory : deviceImageDirectories) { // 添加系统相册
            preferredAlbumNames.add(directory.getName());
        }

        return preferredAlbumNames;
    }

    public void getVideoFile(final List<File> list, String dir) {
        File file = new File(dir);
        if(!file.exists()){
            return;
        }
        file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                String name = file.getName();
                if (name.endsWith(".mp4")) {
                    list.add(file);
                    return true;
                } else if (file.isDirectory()) {
                    getVideoFile(list, file.getAbsolutePath());
                }
                return false;
            }
        });
    }

    public byte[] getBytes(File file){
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public boolean copyFile(File var0, File var1) throws Exception {
        if(var0.isFile()) {
            FileInputStream var2 = new FileInputStream(var0);
            FileOutputStream var3 = new FileOutputStream(var1);
            BufferedInputStream var6 = new BufferedInputStream(var2);
            BufferedOutputStream var8 = new BufferedOutputStream(var3);
            byte[] var4 = new byte[8192];

            for(int var5 = var6.read(var4); var5 != -1; var5 = var6.read(var4)) {
                var8.write(var4, 0, var5);
            }

            var6.close();
            var8.close();
        }

        if(var0.isDirectory()) {
            File[] var7 = var0.listFiles();
            var1.mkdir();

            for(int var9 = 0; var9 < var7.length; ++var9) {
                copyFile(var7[var9].getAbsoluteFile(), new File(var1.getAbsoluteFile() + File.separator + var7[var9].getName()));
            }
        }

        return true;
    }

    private boolean isFileImage(File file){
        String name = file.getName();
        if(name.contains(".jpg") ||
                name.contains(".jpeg") ||
                name.contains(".png") ||
                name.contains(".bmp") ||

                name.contains(".JPG") ||
                name.contains(".JPEG") ||
                name.contains(".PNG") ||
                name.contains(".BMP")
                ){
            return true;
        }else {
            return false;
        }
    }

    public boolean isFileHasImage(File file){
        File[] files = file.listFiles();
        for (File item : files) {
            if(isFileImage(item)){
                return true;
            }
        }
        return false;
    }

    public boolean fileExists(File file){
        return file != null && file.exists() && file.isFile();
    }

    public boolean directoryExists(File file){
        return file != null && file.exists() && file.isDirectory();
    }

    public int createDir(String path){
        if(path == null){
            return -1;
        }
        File f = new File(path);
        if(f.exists() && f.isDirectory()){
            return 0;
        }
        f.mkdirs();
        return 1;
    }

    public void createNewFile(String filePath){
        File file = new File(filePath);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public  boolean deleteFile(String filePath){
        File f = new File(filePath);
        return f.delete();
    }


    public void deleteDir(String pPath) {
        File dir = new File(pPath);
        deleteDirWithFile(dir);
    }

    private void deleteDirWithFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete();
            else if (file.isDirectory())
                deleteDirWithFile(file);
        }
        dir.delete();
    }

    public File createImageFile() throws IOException {
        // Create an image file name
        File imageF = null;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp;
        File albumF = new File(FileUtil.getInstance().getMediaPath());
        if(!albumF.exists()){
            albumF.mkdirs();
        }
        return File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
    }

    public static File createVideoFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String videoFileName = MPEG_FILE_PREFIX + timeStamp;
        File albumF = new File(FileUtil.getInstance().getMediaTempPath());
        File videoF = File.createTempFile(videoFileName, MPEG_FILE_SUFFIX, albumF);
        return videoF;
    }

    public boolean isDirNotEmpty(String filePath){
        File file = new File(filePath);
        return file.exists() && file.listFiles() != null && file.listFiles().length != 0;
    }

    /**
     * @param name Asset 文件名
     * @param path Local 路径
     */
    public void copyFileFromAssetToLocal(Context context, String name, String path, IProgress iProgress){
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = context.getAssets().open(name);
            if(inputStream == null || inputStream.available() == 0){
                iProgress.onError("文件不存在");
                return;
            }

            outputStream = new FileOutputStream(path + "/" + name);

            byte[] buf = new byte[1024];
            int len;
            long count = 0;
            int max = inputStream.available();

            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
                count += len;
                int progress = (int)((count * 100)/max) ;
                iProgress.onProgress(progress);
            }

            iProgress.onDone();
        } catch (IOException e) {
            iProgress.onError("失败");
        } finally {
            try {
                if(outputStream != null){
                    outputStream.close();
                }
                if(inputStream != null){
                    inputStream.close();
                }
            } catch (IOException e) {
                iProgress.onError("失败");
            }
        }
    }

    /**
     * 关闭IO
     *
     * @param closeables closeable
     */
    public static void closeIO(Closeable... closeables) {
        if (closeables == null) {
            return;
        }
        try {
            for (Closeable closeable : closeables) {
                if (closeable != null) {
                    closeable.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * MediaScanner扫描,让其包含到相册中
     */
    public void refreshImageFolders(String photoPath) {
        new Thread(() -> MediaScannerConnection.scanFile(context, new String[]{photoPath}, null, (path, uri) -> {
        })).start();
    }
}
