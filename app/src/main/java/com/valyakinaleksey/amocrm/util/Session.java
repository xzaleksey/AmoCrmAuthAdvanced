package com.valyakinaleksey.amocrm.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.valyakinaleksey.amocrm.models.api.AuthResponse;
import com.valyakinaleksey.amocrm.models.api.Response;

import java.util.List;

public class Session {
    public static String SESSION_ID = null;

    public static void saveSession(retrofit2.Response<Response<AuthResponse>> response, Context context) {
        List<String> namesAndValues = response.headers().values("Set-Cookie");
        String sessionIdRaw = namesAndValues.get(0);
//        int sessionIdStart = sessionIdRaw.indexOf("=") + 1;
        int sessionIdEnd = sessionIdRaw.indexOf(";") + 1;
        Session.SESSION_ID = sessionIdRaw.substring(0, sessionIdEnd);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        sharedPreferences.edit().putString(Constants.SESSION_ID_F, Session.SESSION_ID).apply();
    }

    public static void restoreSession(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        if (sharedPreferences.contains(Constants.SESSION_ID_F)) {
            SESSION_ID = sharedPreferences.getString(Constants.SESSION_ID_F, "");
        }
    }

    public static void clearSession(Context context) {
        SESSION_ID = "";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        sharedPreferences.edit().remove(Constants.SESSION_ID_F).apply();
    }
}
