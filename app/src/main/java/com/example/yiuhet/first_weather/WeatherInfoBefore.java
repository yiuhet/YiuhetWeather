package com.example.yiuhet.first_weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yiuhet on 2017/3/25.
 */

public class WeatherInfoBefore {
    @SerializedName("HeWeather5") @Expose
    public List<WeatherInfo> WeatherDataService
            = new ArrayList<>();
}
