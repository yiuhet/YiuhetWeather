package com.example.yiuhet.first_weather;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by yiuhet on 2017/3/19.
 */

public class PublicMethod {
    public static String safeText(String msg) {
        if (null == msg) {
            return "";
        }
        return safeText("", msg);
    }
    public static String safeText(String prefix, String obj) {
        if (TextUtils.isEmpty(obj)) return "";
        return TextUtils.concat(prefix, obj).toString();
    }

}
