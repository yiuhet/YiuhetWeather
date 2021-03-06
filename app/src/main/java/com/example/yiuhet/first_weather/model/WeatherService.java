package com.example.yiuhet.first_weather.model;

import com.example.yiuhet.first_weather.model.WeatherInfoBefore;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by yiuhet on 2017/3/23.
 */

public interface WeatherService {
    @GET("weather")
    Observable<WeatherInfoBefore> getWeatherData(@Query("city") String city, @Query("key") String apiKey );
}
