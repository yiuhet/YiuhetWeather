package com.example.yiuhet.first_weather.model;


import android.content.Context;

import com.example.yiuhet.first_weather.db.Cityitem;
import com.example.yiuhet.first_weather.util.SharedPreferenceUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yiuhet on 2017/4/7.
 */

public class WeatherManager {
    private static Context mContext;
    private List<Cityitem> mWeatherList ;
    private List<String> mCityList;
    private boolean mIsLbsOn;
    private String mLbsCity;

    public List<String> getCityList() {
        return mCityList;
    }

    public List<Cityitem> getWeatherList() {
        mWeatherList.clear();
        mWeatherList = DataSupport.findAll(Cityitem.class);
        return mWeatherList;
    }

    public boolean isExit(String city) {
        for (int i =0;i<mWeatherList.size();i++) {
            if (mWeatherList.get(i).getCityName().equals(city))
                return true;
        }
        return false;
    }

    public boolean isLbsOn() {
        return mIsLbsOn;
    }
    public String getLbsCity() {
        return mLbsCity;
    }

    private WeatherManager(){
        mWeatherList = new ArrayList<>();
        mCityList = new ArrayList<>();
        mIsLbsOn = SharedPreferenceUtil.getBoolean(mContext,"isLbsOn",true);
        mLbsCity = SharedPreferenceUtil.getString(mContext,"lbs","");
        if (DataSupport.isExist(Cityitem.class)) {
            mWeatherList = DataSupport.findAll(Cityitem.class);
            for (int i =0; i < mWeatherList.size(); i++) {
                mCityList.add(mWeatherList.get(i).getCityName());
            }
        }
    }

    private static class ManagerHolder {
        private static final WeatherManager INSTANCE = new WeatherManager();
    }
    public static WeatherManager getInstance(Context context) {
        mContext = context;
        return ManagerHolder.INSTANCE;
    }
}
