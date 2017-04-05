package com.example.yiuhet.first_weather.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.yiuhet.first_weather.BasicApplication;

/**
 * Created by yiuhet on 2017/3/29.
 */

public class SharedPreferenceUtil {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public SharedPreferenceUtil(Context context, String fileName) {
        preferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public String getString(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }


    public int getInt(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

}
