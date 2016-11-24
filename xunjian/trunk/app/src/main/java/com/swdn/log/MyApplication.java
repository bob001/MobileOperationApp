package com.swdn.log;

import android.app.Application;

public class MyApplication extends Application {

    private static MyApplication application ;

    public static Application getInstence(){
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }
}
