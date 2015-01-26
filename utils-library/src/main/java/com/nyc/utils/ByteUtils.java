package com.nyc.utils;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ByteUtils {

    private static final String TAG = ByteUtils.class.getSimpleName();
    
    public static byte[] convertStreamToByteArray(InputStream stream) {
        if (stream == null) {
            return new byte[] {};
        }
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        byte[] data = new byte[Short.MAX_VALUE-1];
        int read = -1;
        
        try {
            while ((read = stream.read(data, 0, data.length)) != -1) {
                buf.write(data,0,read);
            }
            buf.flush();
        } catch (IOException ioe) {
            Log.e(TAG, "Could not read from stream.");
        }
        try {
            return buf.toByteArray();
        } finally {
            try {
                buf.close();
            } catch (IOException ioe) {}
        }
    }
    
    /**
     * Writes the input to the output stream and closes both streams.
     * @param input
     * @param output
     */
    public static void convertStream(InputStream input, OutputStream output) {
        convertStream(input, output, true);
    }
    
    /**
     * Writes the input to the output stream and closes both streams.
     * @param input
     * @param output
     */
    public static void convertStream(InputStream input, OutputStream output, boolean close) {
        byte[] buffer = new byte[1024];
        int readBytes = 0;
        try {
            while ((readBytes = input.read(buffer)) != -1) {
                output.write(buffer,0,readBytes);
            }
            output.flush();
        } catch (IOException ioe) {
            Log.e(TAG, "Could not write input to output stream.");
        }
        try {
            if (close) {
                input.close();
            }
            output.flush();
            if (close) {
                output.close();
            }
        } catch (IOException ioe) {}
    }
}
