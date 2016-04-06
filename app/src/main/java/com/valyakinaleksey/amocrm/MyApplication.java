package com.valyakinaleksey.amocrm;

import android.app.Application;

import com.valyakinaleksey.amocrm.util.Session;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Session.restoreSession(this);
    }
}
