package com.nyc.utils;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONUtils {

    public static final String TAG = JSONUtils.class.getSimpleName();

    public static ArrayList<String> stringArrayFromJson(String json, String key) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        ArrayList<String> result = new ArrayList<String>();
        try {
            JSONObject obj = new JSONObject(json);
            Object content = obj.get(key);
            if (content instanceof JSONArray) {
                JSONArray array = (JSONArray)content;
                for (int i = 0; i < array.length(); i++) {
                    result.add(array.getString(i));
                }
            }
            return result;
        } catch (JSONException je) {
            Log.e(TAG, "Could not parse json " + key + ":"+json);
            return null;
        }
    }
    
    public static String safeGetString(JSONObject obj, String key) {
        if (obj != null && obj.has(key)) {
            try {
                return obj.getString(key);
            } catch (JSONException e) {
                Log.w(TAG, "Could not get string from key "+key,e);
            }
        }
        return null;
    }
    
    public static int safeGetInt(JSONObject obj, String key, int defaultValue) {
        if (obj != null && obj.has(key)) {
            try {
                return obj.getInt(key);
            } catch (JSONException e) {
                Log.w(TAG, "Could not get int from key "+key,e);
            }
        }
        return defaultValue;
    }
    
    public static boolean safeGetBoolean(JSONObject obj, String key, boolean defaultValue) {
        if (obj != null && obj.has(key)) {
            try {
                return obj.getBoolean(key);
            } catch (JSONException e) {
                Log.w(TAG, "Could not get boolean from key "+key,e);
            }
        }
        return defaultValue;
    }
    
    public static long safeGetLong(JSONObject obj, String key, long defaultValue) {
        if (obj != null && obj.has(key)) {
            try {
                return obj.getLong(key);
            } catch (JSONException e) {
                Log.w(TAG, "Could not get long from key "+key,e);
            }
        }
        return defaultValue;
    }
    
    public static double safeGetDouble(JSONObject obj, String key, double defaultValue) {
        if (obj != null && obj.has(key)) {
            try {
                return obj.getDouble(key);
            } catch (JSONException e) {
                Log.w(TAG, "Could not get double from key "+key,e);
            }
        }
        return defaultValue;
    }
    
    public static float safeGetFloat(JSONObject obj, String key, float defaultValue) {
        if (obj != null && obj.has(key)) {
            try {
                return (float) obj.getDouble(key);
            } catch (JSONException e) {
                Log.w(TAG, "Could not get float from key "+key,e);
            }
        }
        return defaultValue;
    }

    public static String safeGetStringFromArray(JSONArray obj, int index) {
        if (obj != null && obj.length() > index) {
            try {
                return obj.getString(index);
            } catch (JSONException e) {
                Log.w(TAG, "Could not get string from JSONArray from at index "+index,e);
            }
        }
        return null;
    }
    
    public static JSONArray safeGetArrayFromArray(JSONObject obj, String key) {
        if (obj != null && obj.has(key)) {
            try {
                return obj.getJSONArray(key);
            } catch (JSONException e) {
                Log.w(TAG, "Could not get Array from JSONArray from key "+key,e);
            }
        }
        return null;
    }
    
    public static JSONObject safeGetObjectFromArray(JSONArray obj, int index) {
        if (obj != null && obj.length() > index) {
            try {
                return obj.getJSONObject(index);
            } catch (JSONException e) {
                Log.w(TAG, "Could not get object from JSONArray from at index "+index,e);
            }
        }
        return null;
    }

    
    public static JSONArray safeGetArray(JSONObject obj, String key) {
        if (obj != null && obj.has(key)) {
            try {
                return obj.getJSONArray(key);
            } catch (JSONException e) {
                Log.w(TAG, "Could not get array from key "+key,e);
            }
        }
        return null;
    }
    
    public static JSONObject safeGetObject(JSONObject obj, String key) {
        if (obj != null && obj.has(key)) {
            try {
                return obj.getJSONObject(key);
            } catch (JSONException e) {
                Log.w(TAG, "Could not get object from key "+key,e);
            }
        }
        return null;
    }
    
    public static void safePutFloat(JSONObject obj, String key, float value) {
        try {
            obj.put(key, value);
        } catch (JSONException e) {
            Log.w(TAG, "Could not put float with key "+key,e);
        }
    }
    
    public static void safePutInt(JSONObject obj, String key, int value) {
        try {
            obj.put(key, value);
        } catch (JSONException e) {
            Log.w(TAG, "Could not put int with key "+key,e);
        }
    }
    
    public static void safePutLong(JSONObject obj, String key, long value) {
        try {
            obj.put(key, value);
        } catch (JSONException e) {
            Log.w(TAG, "Could not put long with key "+key,e);
        }
    }
    
    public static void safePutDouble(JSONObject obj, String key, double value) {
        try {
            obj.put(key, value);
        } catch (JSONException e) {
            Log.w(TAG, "Could not put double with key "+key,e);
        }
    }
    
    public static void safePutString(JSONObject obj, String key, String value) {
        try {
            obj.put(key, value);
        } catch (JSONException e) {
            Log.w(TAG, "Could not put string with key "+key,e);
        }
    }
    
    public static void safePutBoolean(JSONObject obj, String key, boolean value) {
        try {
            obj.put(key, value);
        } catch (JSONException e) {
            Log.w(TAG, "Could not put boolean with key "+key,e);
        }
    }
    
    public static void safePutArray(JSONObject obj, String key, JSONArray value) {
        try {
            obj.put(key, value);
        } catch (JSONException e) {
            Log.w(TAG, "Could not put array with key "+key,e);
        }
    }
    
    public static void safePutObject(JSONObject obj, String key, JSONObject value) {
        try {
            obj.put(key, value);
        } catch (JSONException e) {
            Log.w(TAG, "Could not put object with key "+key,e);
        }
    }
}
