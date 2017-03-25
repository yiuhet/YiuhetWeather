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
import android.widget.Toast;

import com.example.yiuhet.first_weather.adapter.CityDataAdapter;
import com.example.yiuhet.first_weather.model.AsyncUpdate;
import com.example.yiuhet.first_weather.model.CityWeatherData;
import com.example.yiuhet.first_weather.util.HttpUtil;
import com.example.yiuhet.first_weather.util.LocationUtils;
import com.example.yiuhet.first_weather.util.PublicMethod;
import com.example.yiuhet.first_weather.util.RetroFactory;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by yiuhet on 2017/3/14.
 */

public class MainFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView recyclerview;
    private SwipeRefreshLayout swiprefreshlayout;
    private static String LocalCity = "北京";
    String mErrorCode;
    WeatherInfo weatherInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onRefresh() {
        getCityData(LocalCity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main,container,false);
        initView(view);
        initCity();
        return view;
    }

    private void initCity() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            LocalCity = bundle.getString("LocalCity");
            getCityData(LocalCity);
        } else {
            new LocationUtils(getContext(), new AsyncUpdate() {
                @Override
                public void onFinsh(String city) {
                    LocalCity = PublicMethod.safeText(city).replaceAll("(?:省|市|自治区|特别行政区|地区|盟)", "");
                    getCityData(LocalCity);
                }
                @Override
                public void onLocationError(String ErrorCode) {
                    mErrorCode = ErrorCode;
                   // getActivity().setTitle("定位失败...");
                    Log.e("code",ErrorCode);
                }
            }).start();
        }
    }

    private void initView(View view) {
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        swiprefreshlayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperwfreshlayout);
        swiprefreshlayout.setOnRefreshListener(this);
        swiprefreshlayout.setColorSchemeColors(Color.GREEN,Color.BLUE,Color.RED);
    }

    private void getCityData(final String string) {
        getActivity().setTitle(string);
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
