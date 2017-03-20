package com.example.yiuhet.first_weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yiuhet on 2017/3/18.
 */

public class JsonParse {
    public JSONObject mJsonObject;
    public JsonParse(JSONObject jsonObject) {
        mJsonObject = jsonObject;
    }
    public CityWeatherData parse() throws JSONException {
        CityWeatherData items = new CityWeatherData();
        JSONArray HeWeather5JsonArrary = mJsonObject.getJSONArray("HeWeather5");
        JSONObject WeatherJsonObject = HeWeather5JsonArrary.getJSONObject(0);

        items.status = WeatherJsonObject.getString("status");

        //获取aqi
        JSONObject aqiJsonObject = WeatherJsonObject.getJSONObject("aqi");
        JSONObject aqiCityJsonObject = aqiJsonObject.getJSONObject("city");
        items.aqi_City_Pm25 = aqiCityJsonObject.getString("pm25");
        items.aqi_City_Qlty = aqiCityJsonObject.getString("qlty");

        //获取城市信息：
        JSONObject basicJsonObject = WeatherJsonObject.getJSONObject("basic");
        items.basic_City = (basicJsonObject.getString("city"));
        JSONObject basicUpdat = basicJsonObject.getJSONObject("update");
        items.basic_update = basicUpdat.getString("loc");

        //获取现在的天气数据:
        JSONObject NowWeatherJsonObject = WeatherJsonObject.getJSONObject("now");
        items.now_tmp = (NowWeatherJsonObject.getString("tmp"));
        JSONObject NowWindWeatherJsonObject = NowWeatherJsonObject.getJSONObject("wind");

        JSONObject NowCondWeatherJsonObject = NowWeatherJsonObject.getJSONObject("cond");
        items.now_cond_txt = (NowCondWeatherJsonObject.getString("txt"));
        //获取未来的天气数据
        JSONArray daily_forcast = WeatherJsonObject.getJSONArray("daily_forecast");
        for (int i = 0; i < daily_forcast.length(); i++) {
            daily_Data tmpData = new daily_Data();
            JSONObject daily_forcastJSONObject_i = daily_forcast.getJSONObject(i);
            tmpData.daily_Date = daily_forcastJSONObject_i.getString("date");
            JSONObject daily_forcastJSONObject_i_cond = daily_forcastJSONObject_i.getJSONObject("cond");
            tmpData.daily_Cond_D = (daily_forcastJSONObject_i_cond.getString("code_d"));
            tmpData.daily_Cond_N = (daily_forcastJSONObject_i_cond.getString("code_n"));
            tmpData.daily_Cond_Txtd = (daily_forcastJSONObject_i_cond.getString("txt_d"));
            tmpData.daily_Cond_Txtn = (daily_forcastJSONObject_i_cond.getString("txt_n"));
            JSONObject daily_forcastJSONObject_i_tmp = daily_forcastJSONObject_i.getJSONObject("tmp");
            tmpData.daily_Tmp_Max = (daily_forcastJSONObject_i_tmp.getString("max"));
            tmpData.daily_Tmp_Min = (daily_forcastJSONObject_i_tmp.getString("min"));
            JSONObject daily_forcastJSONObject_i_wind = daily_forcastJSONObject_i.getJSONObject("wind");
            tmpData.daily_Wind_Dir = (daily_forcastJSONObject_i_wind.getString("dir"));
            tmpData.daily_Wind_Sc = (daily_forcastJSONObject_i_wind.getString("sc"));
            items.daily_Data_List.add(tmpData);
        }

        //获取24小时天气数据:
        JSONArray hourly_forecast = WeatherJsonObject.getJSONArray("hourly_forecast");
        for (int i = 0; i < hourly_forecast.length(); i++) {
            hourly_Date tmp_hourly_data = new hourly_Date();
            JSONObject hourly_forcastJSONObject_i = hourly_forecast.getJSONObject(i);
            JSONObject hourly_cond = hourly_forcastJSONObject_i.getJSONObject("cond");
            tmp_hourly_data.hourly_Cond_Txt = hourly_cond.getString("txt");
            tmp_hourly_data.hourly_Date = hourly_forcastJSONObject_i.getString("date");
            tmp_hourly_data.hourly_Tmp = hourly_forcastJSONObject_i.getString("tmp");
            JSONObject hourly_wind = hourly_forcastJSONObject_i.getJSONObject("wind");
            tmp_hourly_data.hourly_Wind_Dir = hourly_wind.getString("dir");
            tmp_hourly_data.hourly_Wind_Sc = hourly_wind.getString("sc");
            items.hourly_dates_List.add(tmp_hourly_data);
        }
        //数据获取完毕 加载---
        return items;
    }
}
