package com.nyc.utils;

import android.database.Cursor;
import android.database.CursorJoiner;
import android.util.Log;

public class CursorUtils {

    private static final String TAG = CursorUtils.class.getSimpleName();
    
    public static final int VALUE_FALSE = 0;
    public static final int VALUE_TRUE = 1;

    public static boolean hasColumn(Cursor result, String columnName) {
        int columnIndex = result.getColumnIndex(columnName);
        return columnIndex != -1;
    }
    
    public static String safeGetString(Cursor result, String columnName) {
        int columnIndex = result.getColumnIndex(columnName);
        if (columnIndex == -1) return new String();
        if (result.getColumnCount() <= columnIndex) return new String();
        try {
            return result.getString(columnIndex);
        } catch (Exception e) {
            Log.e(TAG, "Could not retrieve string value for "+columnName,e);
            return new String();
        }
    }
    
    public static int safeGetInt(Cursor result, String columnName, int defaultValue) {
        int columnIndex = result.getColumnIndex(columnName);
        if (columnIndex == -1) return defaultValue;
        if (result.getColumnCount() <= columnIndex) return defaultValue;
        try {
            return result.getInt(columnIndex);
        } catch (Exception e) {
            Log.e(TAG, "Could not retrieve int value for "+columnName,e);
            return defaultValue;
        }
    }

    public static long safeGetLong(Cursor result, String columnName, long defaultValue) {
        int columnIndex = result.getColumnIndex(columnName);
        if (columnIndex == -1) return defaultValue;
        if (result.getColumnCount() <= columnIndex) return defaultValue;
        try {
            return result.getLong(columnIndex);
        } catch (Exception e) {
            Log.e(TAG, "Could not retrieve long value for "+columnName,e);
            return defaultValue;
        }
    }

    public static boolean safeGetIntBackedBoolean(Cursor result, String columnName, boolean defaultValue) {
        int columnIndex = result.getColumnIndex(columnName);
        if (columnIndex == -1) return defaultValue;
        if (result.getColumnCount() <= columnIndex) return defaultValue;
        try {
            return result.getInt(columnIndex) == VALUE_TRUE;
        } catch (Exception e) {
            Log.e(TAG, "Could not retrieve boolean (int backed) value for "+columnName,e);
            return defaultValue;
        }
    }
    
    public static double safeGetDouble(Cursor result, String columnName, double defaultValue) {
        int columnIndex = result.getColumnIndex(columnName);
        if (columnIndex == -1) return defaultValue;
        if (result.getColumnCount() <= columnIndex) return defaultValue;
        try {
            return result.getDouble(columnIndex);
        } catch (Exception e) {
            Log.e(TAG, "Could not retrieve string value for "+columnName,e);
            return defaultValue;
        }
    }
    
    public static void safeClose(Cursor result) {
        if (result != null && !result.isClosed()) {
            result.close();
        }
    }

    public static boolean isDuplicate(Cursor cursor, Cursor oldCursor, String[] columns) {
        // CursorJoiner fails when one of the cursors is null
        if (oldCursor == null && cursor != null) return false;
        else if (oldCursor != null && cursor == null) return false;
        else if (oldCursor == null && cursor == null) return true;
        
        CursorJoiner joiner = new CursorJoiner(
                cursor, 
                columns,
                oldCursor,
                columns);
        for (CursorJoiner.Result result : joiner) {
            switch (result) {
                case RIGHT:
                case LEFT:
                    return false;
                case BOTH:
                    // cursors are the same
                    return true;
            }
        }
        return false;
    }

    public static boolean hasResults(Cursor cursor) {
        return cursor != null && cursor.getCount() > 0 && cursor.moveToFirst();
    }
}
