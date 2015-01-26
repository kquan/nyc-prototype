package com.nyc.utils;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {
    
    private static final String TAG = FileUtils.class.getSimpleName();
    
    public static void ensureParentFoldersCreated(File aFile, boolean leaf) {
        if (leaf) {
            ensureParentFoldersCreated(aFile.getParentFile(), false);
        } else if (aFile != null && !aFile.exists()) {
            ensureParentFoldersCreated(aFile.getParentFile(), false);
            boolean result = aFile.mkdir();
            if (!result) {
                Log.e(TAG, "Could not create directory: "+aFile.getAbsolutePath());
            }
        }
    }
    
    public static boolean copyFile(File srcFile, File destFile) {
        boolean result = false;
        try {
            InputStream in = new FileInputStream(srcFile);
            try {
                result = copyToFile(in, destFile);
            } finally  {
                in.close();
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException: ", e);
            result = false;
        }
        return result;
    }

    /**
     * Copy data from a source stream to destFile.
     * Return true if succeed, return false if failed.
     */
    public static boolean copyToFile(InputStream inputStream, File destFile) {
        try {
            OutputStream out = new FileOutputStream(destFile);
            try {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) >= 0) {
                    out.write(buffer, 0, bytesRead);
                }
            } finally {
                out.close();
            }
            return true;
        } catch (IOException e) {
            Log.e(TAG, "IOException: ", e);
            return false;
        }
    }
    
    public static boolean deleteAllFilesInDirectory(File directory) {
        if (directory == null || !directory.isDirectory()) {
            Log.w(TAG, "Could not delete directory: "+directory);
            return false;
        }
        boolean result = true;
        String[] files = directory.list();
        for (String aFile : files) {
            File childFile = new File(directory, aFile);
            if (childFile.isDirectory()) {
                result &= deleteAllFilesInDirectory(childFile);
            }
            result &= childFile.delete();
        }
        return result;
    }

}
