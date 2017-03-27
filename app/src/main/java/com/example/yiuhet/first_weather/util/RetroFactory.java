package com.example.yiuhet.first_weather.util;

import com.example.yiuhet.first_weather.model.WeatherService;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yiuhet on 2017/3/24.
 */

public class RetroFactory {
    public static final String API_KEY = "cb8861667aad4146b752f821dbef4dce";
    public static String baseUrl = "https://free-api.heweather.com/v5/";
    private RetroFactory(){
    }

    private static OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build();

    private static WeatherService retrofitService = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient)
            .build()
            .create(WeatherService.class);

    //懒汉式单例模式
    public static WeatherService getInstance() {
        return retrofitService;
    }
}
