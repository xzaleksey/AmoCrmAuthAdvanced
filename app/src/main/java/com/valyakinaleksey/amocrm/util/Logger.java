package com.valyakinaleksey.amocrm.util;

import android.util.Log;

public class Logger {
    public static final String TAG = "AmoCrmLog";
    private static boolean isDebug = true;

    public static void d(String message) {
        if (isDebug) {
            Log.d(TAG, message);
        }
    }

}
