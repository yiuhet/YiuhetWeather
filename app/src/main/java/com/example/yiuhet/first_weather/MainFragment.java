package com.example.yiuhet.first_weather;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.example.yiuhet.first_weather.adapter.CityDataAdapter;
import com.example.yiuhet.first_weather.model.AsyncUpdate;
import com.example.yiuhet.first_weather.model.WeatherInfo;
import com.example.yiuhet.first_weather.model.WeatherInfoBefore;
import com.example.yiuhet.first_weather.util.LocationUtils;
import com.example.yiuhet.first_weather.util.PublicMethod;
import com.example.yiuhet.first_weather.util.RetroFactory;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yiuhet on 2017/3/14.
 */

public class MainFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView recyclerview;
    private SwipeRefreshLayout swiprefreshlayout;
    private String LocalCity;
    String mErrorCode;
    WeatherInfo weatherInfo;
    Toolbar toolbar;


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
        Log.d("dada",LocalCity);
    }

    @Override
    public void onRefresh() {
        getCityData(LocalCity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);
        initView(view);
        initCity();
        return view;
    }

    private void initCity() {
        if (LocalCity != null) {
            getCityData(LocalCity);
        } else {
            PublicMethod.ShowTips(getContext(),"异常");
        }
    }

    private void initView(View view) {
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        swiprefreshlayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperwfreshlayout);
        swiprefreshlayout.setOnRefreshListener(this);
        swiprefreshlayout.setColorSchemeColors(Color.GREEN,Color.BLUE,Color.RED);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    }

    private void getCityData(final String string) {
        //getActivity().setTitle(string);
        RetroFactory.getInstance().getWeatherData(string,RetroFactory.API_KEY)
                .subscribeOn(Schedulers.io())
                .map(new Function<WeatherInfoBefore, WeatherInfo>() {
                    @Override
                    public WeatherInfo apply(WeatherInfoBefore weatherInfoBefore) throws Exception {
                        return weatherInfoBefore.WeatherDataService.get(0);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WeatherInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                    @Override
                    public void onNext(WeatherInfo value) {
                        CityDataAdapter cityDataAdapter = new CityDataAdapter(value);
                        recyclerview.setAdapter(cityDataAdapter);
                        cityDataAdapter.notifyDataSetChanged();
                        PublicMethod.ShowTips(getContext(),"刷新成功");

                    }

                    @Override
                    public void onError(Throwable e) {
                        PublicMethod.ShowTips(getContext(),"网络异常");
                    }

                    @Override
                    public void onComplete() {
                        swiprefreshlayout.setRefreshing(false);
                    }
                });
        /*
        HttpUtil.HefengFetchr(string, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                try {
                    JSONObject jsonBody = new JSONObject(responseData);
                    JSONArray HeWeather5JsonArrary = jsonBody.getJSONArray("HeWeather5");
                    JSONObject WeatherJsonObject = HeWeather5JsonArrary.getJSONObject(0);
                    Gson gson = new Gson();
                    weatherInfo =gson.fromJson(WeatherJsonObject.toString(),WeatherInfo.class);
                    setUpAdapter();
                    if (weatherInfo != null){
                        getActivity().setTitle(weatherInfo.basic.city);
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(),"错误：",Toast.LENGTH_LONG).show();
                    Log.e("tag",e.toString());
                }

            }
        });*/
    }


    //    private void setUpAdapter(WeatherInfo data) {
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                CityDataAdapter cityDataAdapter = new CityDataAdapter(data);
//                recyclerview.setAdapter(cityDataAdapter);
//                //cityDataAdapter.notifyDataSetChanged();
//                swiprefreshlayout.setRefreshing(false);
//            }
//        });
//    }

}
