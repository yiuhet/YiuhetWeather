package com.example.yiuhet.first_weather;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.yiuhet.first_weather.adapter.MainPagerAdapter;
import com.example.yiuhet.first_weather.db.Cityitem;
import com.example.yiuhet.first_weather.model.AsyncUpdate;
import com.example.yiuhet.first_weather.model.CityWeatherData;
import com.example.yiuhet.first_weather.model.WeatherInfo;
import com.example.yiuhet.first_weather.model.WeatherInfoBefore;
import com.example.yiuhet.first_weather.util.HttpUtil;
import com.example.yiuhet.first_weather.util.LocationUtils;
import com.example.yiuhet.first_weather.util.PublicMethod;
import com.example.yiuhet.first_weather.util.RetroFactory;
import com.example.yiuhet.first_weather.util.SharedPreferenceUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.relex.circleindicator.CircleIndicator;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity{

    public Toolbar toolbar;
    private ViewPager mViewPager;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private String cityName;
    private List<Cityitem> mCityList;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private SharedPreferenceUtil sharedPreferenceUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferenceUtil = new SharedPreferenceUtil(this,"someData");
        requestPermission();  // 请求权限 bug 初次加载
        initView();
        initDrawer();
        initBar();
    }
    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.READ_PHONE_STATE},
                    0);//自定义的code
        }
    }


    private void initView() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
    }
    //初始化侧边菜单事件 --
    private void initDrawer() {
        if (mNavigationView != null) {
            mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_city:
                            PublicMethod.ShowTips(MainActivity.this,"添加城市");
                            break;
                    }
                    return false;
                }
            });
            mNavigationView.inflateHeaderView(R.layout.nav_header_img);
        }
    }
    //初始化viewpager适配器 --定位后
    private void initAdapter() {
        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(),mFragmentList);
        mViewPager.setAdapter(mainPagerAdapter);
        mViewPager.addOnPageChangeListener(new MyPageChangeListener());
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mViewPager);
    }
    //初始化定位和列表数据
    private void initData() {
        //if (cityName == null) {
            new LocationUtils(getApplicationContext(), new AsyncUpdate() {
                @Override
                public void onFinsh(String city) {
                    RetroFactory.getInstance().getWeatherData(city, RetroFactory.API_KEY)
                            .subscribeOn(Schedulers.io())
                            .map(new Function<WeatherInfoBefore, WeatherInfo>() {
                                @Override
                                public WeatherInfo apply(WeatherInfoBefore weatherInfoBefore) throws Exception {
                                    return weatherInfoBefore.WeatherDataService.get(0);
                                }
                            })
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<WeatherInfo>() {
                                @Override
                                public void accept(WeatherInfo weatherInfo) throws Exception {
                                    if (!DataSupport.isExist(Cityitem.class,"cityName = ?",weatherInfo.basic.city)) {
                                        Cityitem cityItem = new Cityitem();
                                        cityItem.setCityName(weatherInfo.basic.city);
                                        cityItem.setTmpInfo(weatherInfo.dailyForecast.get(0).cond.txtD);
                                        cityItem.setTmpMin(weatherInfo.dailyForecast.get(0).tmp.min);
                                        cityItem.setTmpMax(weatherInfo.dailyForecast.get(0).tmp.max);
                                        cityItem.setCond(weatherInfo.dailyForecast.get(0).cond.codeD);
                                        cityItem.save();
                                    }
                                    mCityList = DataSupport.findAll(Cityitem.class);
                                    sharedPreferenceUtil.putString("lbs",weatherInfo.basic.city);
                                    toolbar.setLogo(R.drawable.ic_action_location);
                                    toolbar.setTitle(mCityList.get(0).getCityName());
                                    for (int i=0;i<mCityList.size();i++) {
                                        mFragmentList.add(MainFragment.newInstance(mCityList.get(i).getCityName()));
                                    }
                                    initAdapter();
                                }
                            });
                }
                @Override
                public void onLocationError(String ErrorCode) {
                    toolbar.setTitle("定位失败。。。");
                }
            }).start();
    }
   // }
    //初始化状态栏和toolbar
    private void initBar() {
        //状态栏透明
        if(Build.VERSION.SDK_INT> Build.VERSION_CODES.KITKAT){
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.flags= (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS|params.flags);
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("定位中...");
        initData();
    }




    public class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            toolbar.setLogo(null);
            if ( sharedPreferenceUtil.getString("lbs","哈尔滨").equals(mCityList.get(position).getCityName())) {
                toolbar.setLogo(R.drawable.ic_action_location);
            }
            toolbar.setTitle(mCityList.get(position).getCityName());
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mCityList = DataSupport.findAll(Cityitem.class);
        mFragmentList.clear();
        for (int i=0;i<mCityList.size();i++) {
            mFragmentList.add(MainFragment.newInstance(mCityList.get(i).getCityName()));
        }
        initAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("cccc", String.valueOf(getIntent().getIntExtra("cityPos",0)));
        mViewPager.setCurrentItem(getIntent().getIntExtra("cityPos",0));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        getIntent().putExtras(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //配置searchView... 输入完成后搜索城市天气 快速查询 弹出天气数据
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
             public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this,String.format("查询%s中",query),Toast.LENGTH_SHORT).show();
                MainFragment mainFragment = new MainFragment();
                Bundle bundle = new Bundle();
                bundle.putString("LocalCity",query);
                Log.d("ppap",bundle.getString("LocalCity"));
                mainFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.viewpager, mainFragment)
                        .commit();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(MainActivity.this,ChooseActivity.class));
                return true;
            case R.id.action_search:
                break;
            case R.id.action_add:
                startActivity(new Intent(MainActivity.this,CityListActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
