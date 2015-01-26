package com.nyc.utils;

import android.text.TextUtils;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by j.ostrander on 12/8/14.
 */
public class ReflectionUtils {

    private static final String TAG = ReflectionUtils.class.getSimpleName();

    public static <T> T call(Object instance, String methodName) {
        try {
            Class cmClass = Class.forName(instance.getClass().getName());
            Method method = cmClass.getDeclaredMethod(methodName);
            method.setAccessible(true); // Make the method callable
            // get the setting for "mobile data"
            return (T) method.invoke(instance);
        } catch (Exception e) {
            Log.e(TAG, "Exception calling method: ", e);
        }
        return null;
    }

    /**
     * @author kquan
     * This method returns a method that is hidden using a @hide annotation.  It should
     * not be used to retrieve methods that you do not have visibility to (i.e., private, protected
     * or default visibility)
     *
     * @param fullyQualifiedClassName
     * @param methodName
     * @param parameters
     * @return
     */
    public static Method getHiddenMethod(String fullyQualifiedClassName, String methodName, Class[] parameters) {
        if (TextUtils.isEmpty(fullyQualifiedClassName)) {
            return null;
        }
        Class classType = null;
        try {
            classType = Class.forName(fullyQualifiedClassName);
        } catch (ClassNotFoundException cnfe) {
        }

        if (classType == null) {
            Log.w(TAG, "Requested class could not be found: " + fullyQualifiedClassName);
            return null;
        }

        return getHiddenMethod(classType, methodName, parameters);
    }

    /**
     * @author kquan
     * This method returns a method that is hidden using a @hide annotation.  It should
     * not be used to retrieve methods that you do not have visibility to (i.e., private, protected
     * or default visibility)
     *
     * @param classType
     * @param methodName
     * @param parameters
     * @return
     */
    public static Method getHiddenMethod(Class classType, String methodName, Class[] parameters) {
        if (classType == null || TextUtils.isEmpty(methodName)) {
            return null;
        }

        Method method = null;
        try {
            method = classType.getMethod(methodName, parameters);
        } catch (NoSuchMethodException nsme) {
            /* This is just for debugging this method...
            Log.d(TAG, "No method of name "+methodName, nsme);
            Method[] allMethods = classType.getMethods();
            for (Method m : allMethods) {
                Log.v(TAG, "Method: "+m.getName());
                Class[] debugParams = m.getParameterTypes();
                if (debugParams != null) {
                    for (int i = 0; i < debugParams.length; i++) {
                        Log.v(TAG, "Parameter "+i+" is of type "+debugParams[i].getName());
                    }
                }
            }
            */
        }

        return method;
    }
}
