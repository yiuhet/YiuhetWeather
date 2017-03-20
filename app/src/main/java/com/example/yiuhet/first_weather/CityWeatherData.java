package com.example.yiuhet.first_weather;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yiuhet on 2017/3/15.
 */

public class CityWeatherData implements Serializable{
    public String aqi_City_Pm25,aqi_City_Qlty;
    public String basic_City,basic_update;
    public List<daily_Data> daily_Data_List = new ArrayList<>();
    public List<hourly_Date> hourly_dates_List = new ArrayList<>();
    public String now_tmp,now_cond_txt;
    public String status;
}
