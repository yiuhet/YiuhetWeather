package com.example.yiuhet.first_weather;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Created by yiuhet on 2017/3/28.
 */

public class BasicApplication extends Application {
    private static String sCacheDir;
    public static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        //http://blog.csdn.net/androidwifi/article/details/17725989/ android储存
        if (getApplicationContext().getExternalCacheDir() != null && ExistSDCard()){
            sCacheDir = getApplicationContext().getExternalCacheDir().toString();
        } else {
            sCacheDir = getApplicationContext().getCacheDir().toString();
        }

    }
    private boolean ExistSDCard() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    public static Context getAppContext() {
        return sContext;
    }

    public static String getAppCacheDir() {
        return sCacheDir;
    }

}
