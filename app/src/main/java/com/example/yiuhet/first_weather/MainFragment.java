package com.example.yiuhet.first_weather;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yiuhet.first_weather.adapter.CityDataAdapter;
import com.example.yiuhet.first_weather.db.Cityitem;
import com.example.yiuhet.first_weather.model.WeatherInfo;
import com.example.yiuhet.first_weather.util.Utils;
import com.example.yiuhet.first_weather.util.RetroFactory;
import com.example.yiuhet.first_weather.util.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by yiuhet on 2017/3/14.
 */

public class MainFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.swiperwfreshlayout)
    SwipeRefreshLayout swiprefreshlayout;
    private String LocalCity;
    private CityDataAdapter cityDataAdapter;
    private static WeatherInfo weatherInfo = new WeatherInfo();

    public static MainFragment newInstance(String city) {
        MainFragment f = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putString("LocalCity",city);
        f.setArguments(bundle);
        return f;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalCity = getArguments().getString("LocalCity","哈尔滨");
    }

    @Override
    public void onRefresh() {
        getCityData(LocalCity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);
        ButterKnife.bind(this, view) ;
        initView(view);
        if (LocalCity != null) {
            getCityData(LocalCity);
        }
        return view;
    }

    private void initView(View view) {
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        cityDataAdapter = new CityDataAdapter(weatherInfo);
        recyclerview.setAdapter(cityDataAdapter);

        swiprefreshlayout.setOnRefreshListener(this);
        swiprefreshlayout.setColorSchemeColors(Color.GREEN,Color.BLUE,Color.RED);
    }

    private void getCityData(String string) {
        RetroFactory.getInstance().fetchWeather(string)
                .doOnNext(new Consumer<WeatherInfo>() {
                    @Override
                    public void accept(WeatherInfo weatherInfo) throws Exception {
                        swiprefreshlayout.setRefreshing(true);
                    }
                })
                .subscribe(new Observer<WeatherInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext(WeatherInfo value) {
                        weatherInfo.dailyForecast = value.dailyForecast;
                        weatherInfo.aqi = value.aqi;
                        weatherInfo.basic = value.basic;
                        weatherInfo.hourlyForecast = value.hourlyForecast;
                        weatherInfo.now = value.now;
                        weatherInfo.now.tmp = value.now.tmp;
                        weatherInfo.status = value.status;
                        weatherInfo.suggestion = value.suggestion;
                        cityDataAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onError(Throwable e) {
                        Utils.ShowTips(getContext(),"网络异常");
                    }

                    @Override
                    public void onComplete() {
                        swiprefreshlayout.setRefreshing(false);
                    }
                });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
