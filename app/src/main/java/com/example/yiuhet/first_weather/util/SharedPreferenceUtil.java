package com.example.yiuhet.first_weather.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.yiuhet.first_weather.BasicApplication;

/**
 * Created by yiuhet on 2017/3/29.
 */

public class SharedPreferenceUtil {

    private static SharedPreferences preferences;
    public  static final String PREF_NAME = "setting";

    public static void setString(Context context,String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static void setBoolean(Context context,String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    public static void setInt(Context context,String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }

    public static String getString(Context context,String key, String defValue) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    public static boolean getBoolean(Context context,String key, boolean defValue) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }


    public int getInt(Context context,String key, int defValue) {
        SharedPreferences sp = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }

}
