package com.example.yiuhet.first_weather;

import com.example.yiuhet.first_weather.model.CityWeatherData;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by yiuhet on 2017/3/23.
 */

public interface WeatherService {
    @GET("weather")
    Observable<CityWeatherData> getWeatherData(@Query("city") String city, @Query("key") String apiKey );
}
