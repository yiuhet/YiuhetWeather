package com.example.yiuhet.first_weather.util;

import android.text.TextUtils;

import com.example.yiuhet.first_weather.BasicApplication;
import com.example.yiuhet.first_weather.model.WeatherInfo;
import com.example.yiuhet.first_weather.model.WeatherInfoBefore;
import com.example.yiuhet.first_weather.model.WeatherService;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yiuhet on 2017/3/24.
 */

public class RetroFactory {
    public static final String API_KEY = "cb8861667aad4146b752f821dbef4dce";
    public static String baseUrl = "https://free-api.heweather.com/v5/";
    private static WeatherService retrofitService = null;
    private static OkHttpClient okHttpClient = null;
    private RetroFactory(){
        init();

    }
    private void init() {
        initOkHttp();
        initRetrofit();
    }
    private static void initOkHttp() {

//        http://blog.csdn.net/u014614038/article/details/51210685 教你如何使用okhttp缓存
        //创建拦截器

        OkHttpClient.Builder builder = new OkHttpClient.Builder();


        //设置缓存路径
        File httpCacheDirectory = new File(BasicApplication.getAppCacheDir(), "WeatherCache");
        //Log.d("sdfa",BasicApplication.getAppCacheDir());
        //设置缓存 10M
        Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);

        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response = chain.proceed(request);
                String cacheControl = request.cacheControl().toString();
                if (TextUtils.isEmpty(cacheControl)) {
                    cacheControl = "public, max-age=300";
                }
                return response.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .build();
            }
        };
        //创建OkHttpClient，并添加拦截器和缓存代码
        okHttpClient =builder
                .cache(cache)
                .addInterceptor(interceptor)
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();
    }

    private static void initRetrofit(){
        retrofitService = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
                .create(WeatherService.class);
    }

    public WeatherService getRetrofitService() {
        return retrofitService;
    }

    public Observable<WeatherInfo> fetchWeather(String city) {
        return retrofitService.getWeatherData(city,RetroFactory.API_KEY)
                //status 状态码分析
                .map(new Function<WeatherInfoBefore, WeatherInfo>() {
                    @Override
                    public WeatherInfo apply(WeatherInfoBefore weatherInfoBefore) throws Exception {
                        return weatherInfoBefore.WeatherDataService.get(0);
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    private static class SingletonHolder {
        private static final RetroFactory INSTANCE = new RetroFactory();
    }

    public static RetroFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

}
