package com.swdn.myappllication;

import android.app.Application;
import android.content.Context;

/**
 * Created by lenovo on 2016/11/11.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
