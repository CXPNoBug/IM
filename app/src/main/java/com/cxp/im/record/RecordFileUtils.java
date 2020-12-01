package com.cxp.im.record;

import android.os.Environment;

import com.cxp.im.MyApplication;

import java.io.File;

/**
 * 文 件 名: RecordFileUtils
 * 创 建 人: CXP
 * 创建日期: 2020-10-16 10:19
 * 描    述:
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class RecordFileUtils {

    public static final String VIDEO_DIR = "/Record/Videos";
    public static final String IMG_DIR = "/Record/Image";
    public static final String APP_DIR = "/MOA3.0-Beta";

    public static File getRootDir() {
        String state = Environment.getExternalStorageState();
        File rootDir = state.equals(Environment.MEDIA_MOUNTED) ? Environment.getExternalStorageDirectory() : MyApplication.mApplication.getFilesDir();
        return rootDir;
    }

    public static File getDir() {
        File file = new File(RecordFileUtils.getRootDir().getPath() + APP_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 创建根目录,文件夹
     *
     * @param filePath 目录路径
     */
    public static void createDirFile(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception e) {
        }
    }

    public static boolean isFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * 删除文件
     *
     * @param filePath
     * @return
     */
    public static boolean deleteFile(String filePath) {
        boolean status;
        SecurityManager checker = new SecurityManager();
        if (!filePath.equals("")) {
            File newPath = new File(filePath);
            checker.checkDelete(newPath.toString());
            if (newPath.isFile()) {
                try {
                    newPath.delete();
                    status = true;
                } catch (SecurityException se) {
                    se.printStackTrace();
                    status = false;
                }
            } else
                status = false;
        } else
            status = false;
        return status;
    }

    /**
     * 删除目录(包括：目录里的所有文件)
     *
     * @param filePath 文件路径
     * @return
     */
    public static boolean deleteDirectory(String filePath) {
        boolean status = false;
        SecurityManager checker = new SecurityManager();

        if (!filePath.equals("")) {
            File newPath = new File(filePath);
            checker.checkDelete(newPath.toString());
            if (newPath.isDirectory()) {
                String[] listfile = newPath.list();
                // delete all files within the specified directory and then
                // delete the directory
                try {
                    for (int i = 0; i < listfile.length; i++) {
                        File deletedFile = new File(newPath.toString() + "/"
                                + listfile[i].toString());
                        deletedFile.delete();
                    }
                    newPath.delete();
                    status = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    status = false;
                }

            } else {
                status = false;
            }

        } else {
            status = false;
        }

        return status;
    }

    /**
     * 获取文件大小
     *
     * @param filePath 字节
     * @return
     */
    public static String getFileSizeWithformat(String filePath) {
        long size = 0;

        File file = new File(filePath);
        if (file != null && file.exists()) {
            size = file.length();
        }

        if (size <= 0)
            return "0MB";
        java.text.DecimalFormat df = new java.text.DecimalFormat("##.##");
        float temp = (float) size / 1024;
        return df.format((double) temp / 1024) + "MB";
		/*if (temp >= 1024) {
			return df.format(temp / 1024) + "MB";
		} else {
			return df.format(temp) + "KB";
		}*/
    }
}
