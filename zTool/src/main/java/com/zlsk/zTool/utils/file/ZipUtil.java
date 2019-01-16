package com.zlsk.zTool.utils.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

    public static String zip(List<String> fileList){
        ArrayList<File> files = new ArrayList<File>();
        for (String file : fileList) {
            files.add(new File(file));
        }
        String path = FileUtil.getInstance(null).getMediaPath() + UUID.randomUUID() + ".zip";
        if (zip(files.toArray(new File[files.size()]), path)) {
            return path;
        } else {
            return null;
        }
    }

    private static boolean zip(File[] sourceFiles, String zipFilePath) {
        boolean r = false;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            File zipFile = new File(zipFilePath);
            if (zipFile.exists()) {
                zipFile.delete();
            }
            fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(new BufferedOutputStream(fos));
            for (int i = 0; i < sourceFiles.length; i++) {
                byte[] buffer = FileUtil.getInstance(null).getBytes(sourceFiles[i]);
                ZipEntry zipEntry = new ZipEntry(i + ".jpg");
                zos.putNextEntry(zipEntry);
                zos.write(buffer, 0, buffer.length);
            }
            r = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (zos != null) {
                    zos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return r;
    }

    public static void unZip(String filePath, String outPath,IProgress iProgress){
        if(!FileUtil.getInstance().isFileExist(filePath)){
            iProgress.onError("zip不存在 " + filePath);
            return;
        }

        long zipLength = getZipSize(filePath);
        ZipInputStream zipInputStream = null;

        try {
            zipInputStream = new ZipInputStream(new FileInputStream(filePath));
            if(zipInputStream == null || zipInputStream.available() == 0){
                iProgress.onError("zip错误");
                return;
            }

            ZipEntry zipEntry;
            String  szName = "";
            long count = 0;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                szName = zipEntry.getName();
                String currentParentFile = "";
                if (zipEntry.isDirectory()) {
                    szName = szName.substring(0, szName.length() - 1);
                    currentParentFile = outPath + File.separator + szName;
                    new File(currentParentFile).mkdirs();
                } else {
                    File file = new File(outPath + "/"  + szName);
                    if (!file.exists()){
                        file.getParentFile().mkdirs();
                        file.createNewFile();
                    }
                    // 获取文件的输出流
                    FileOutputStream out = new FileOutputStream(file);
                    int len;
                    byte[] buffer = new byte[1024];
                    // 读取（字节）字节到缓冲区
                    while ((len = zipInputStream.read(buffer)) != -1) {
                        count += len;
                        int progress = (int)((count * 100)/zipLength) ;
                        iProgress.onProgress(progress);
                        // 从缓冲区（0）位置写入（字节）字节
                        out.write(buffer, 0, len);
                        out.flush();
                    }
                    out.close();
                }
            }
            iProgress.onDone();
        }catch (Exception exc){
            iProgress.onError(exc.getMessage());
        }finally {
            try {
                zipInputStream.close();
                FileUtil.getInstance().deleteFile(filePath);
            } catch (IOException e) {
                iProgress.onError(e.getMessage());
            }
        }
    }

    private static long getZipSize(String filePath){
        long size = 0;
        ZipFile f;
        try {
            f = new ZipFile(filePath);
            Enumeration<? extends ZipEntry> en = f.entries();
            while (en.hasMoreElements()) {
                size += en.nextElement().getSize();
            }
        } catch (IOException e) {
            size = 0;
        }
        return size;
    }

    public static void zip(String src, String dest) throws IOException {
        //提供了一个数据项压缩成一个ZIP归档输出流
        ZipOutputStream out = null;
        try {

            File outFile = new File(dest);//源文件或者目录
            File fileOrDirectory = new File(src);//压缩文件路径
            out = new ZipOutputStream(new FileOutputStream(outFile));
            //如果此文件是一个文件，否则为false。
            if (fileOrDirectory.isFile()) {
                zipFileOrDirectory(out, fileOrDirectory, "");
            } else {
                //返回一个文件或空阵列。
                File[] entries = fileOrDirectory.listFiles();
                for (int i = 0; i < entries.length; i++) {
                    // 递归压缩，更新curPaths
                    zipFileOrDirectory(out, entries[i], "");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            //关闭输出流
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private static void zipFileOrDirectory(ZipOutputStream out,
                                           File fileOrDirectory, String curPath) throws IOException {
        //从文件中读取字节的输入流
        FileInputStream in = null;
        try {
            //如果此文件是一个目录，否则返回false。
            if (!fileOrDirectory.isDirectory()) {
                // 压缩文件
                byte[] buffer = new byte[4096];
                int bytes_read;
                in = new FileInputStream(fileOrDirectory);
                //实例代表一个条目内的ZIP归档
                ZipEntry entry = new ZipEntry(curPath
                        + fileOrDirectory.getName());
                //条目的信息写入底层流
                out.putNextEntry(entry);
                while ((bytes_read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytes_read);
                }
                out.closeEntry();
            } else {
                // 压缩目录
                File[] entries = fileOrDirectory.listFiles();
                for (int i = 0; i < entries.length; i++) {
                    // 递归压缩，更新curPaths
                    zipFileOrDirectory(out, entries[i], curPath
                            + fileOrDirectory.getName() + "/");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            // throw ex;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
