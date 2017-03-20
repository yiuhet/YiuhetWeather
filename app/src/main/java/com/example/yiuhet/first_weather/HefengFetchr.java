package com.example.yiuhet.first_weather;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yiuhet on 2017/3/15.
 */

public class HefengFetchr {

    private static final String API_KEY = "cb8861667aad4146b752f821dbef4dce";

    public static void fetchItem(String city, okhttp3.Callback callback) {
        String url = Uri.parse("https://free-api.heweather.com/v5/weather")
                .buildUpon()
                .appendQueryParameter("city",city)
                .appendQueryParameter("key",API_KEY)
                .build().toString();
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            client.newCall(request).enqueue(callback);
    }

    /***
     * HttpURLConnection 获取数据的方法
     ***/
//    public String getUrlString(String httpUrl) throws IOException {
//        String jsonResult ;
//        BufferedReader reader = null;
//        StringBuffer sbf = new StringBuffer();
//        try {
//            URL url = new URL(httpUrl);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//            connection.connect();
//            InputStream is = connection.getInputStream();
//            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//            String strRead = null;
//            while ((strRead = reader.readLine()) != null) {
//                sbf.append(strRead); sbf.append("\r\n");
//            }
//            reader.close();
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//        jsonResult =sbf.toString();
//        Log.d("pp",jsonResult);
//        return jsonResult;
//    }
}
