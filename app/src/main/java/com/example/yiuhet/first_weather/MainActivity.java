package com.example.yiuhet.first_weather;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.example.yiuhet.first_weather.adapter.MainPagerAdapter;
import com.example.yiuhet.first_weather.db.Cityitem;
import com.example.yiuhet.first_weather.model.AsyncUpdate;
import com.example.yiuhet.first_weather.model.WeatherInfo;
import com.example.yiuhet.first_weather.model.WeatherInfoBefore;
import com.example.yiuhet.first_weather.util.LocationUtils;
import com.example.yiuhet.first_weather.util.PublicMethod;
import com.example.yiuhet.first_weather.util.RetroFactory;
import com.example.yiuhet.first_weather.util.SharedPreferenceUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.main_drawer)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.indicator)
    CircleIndicator indicator;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.coord)
    CoordinatorLayout coord;

    private List<Cityitem> mCityList;
    private List<Fragment> mFragmentList = new ArrayList<>();
    private SharedPreferenceUtil sharedPreferenceUtil;
    SearchView searchView;
    AlertDialog.Builder dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        sharedPreferenceUtil = new SharedPreferenceUtil(this, "someData");
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
        //状态栏透明
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | params.flags);
        }
        setSupportActionBar(toolbar);
    }

    //初始化侧边菜单事件 --
    private void initDrawer() {
        if (mNavigationView != null) {
            mNavigationView.inflateHeaderView(R.layout.nav_header_img);
            mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    mDrawerLayout.closeDrawers();
                    switch (item.getItemId()) {
                        case R.id.nav_search:
                            break;
                        case R.id.nav_add:
                            startActivity(new Intent(MainActivity.this, ChooseActivity.class));
                            break;
                        case R.id.nav_citymanage:
                            startActivity(new Intent(MainActivity.this, CityListActivity.class));
                            break;
                        case R.id.nav_settings:
                            startActivity(new Intent(MainActivity.this, SettingActivity.class));
                            break;
                        case R.id.nav_about:
                            break;
                    }
                    return true;
                }
            });
        }
    }

    //初始化viewpager适配器 --定位后
    private void initAdapter() {
        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(mainPagerAdapter);
        mViewPager.addOnPageChangeListener(new MyPageChangeListener());
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
                                if (!DataSupport.isExist(Cityitem.class, "cityName = ?", weatherInfo.basic.city)) {
                                    Cityitem cityItem = new Cityitem();
                                    cityItem.setCityName(weatherInfo.basic.city);
                                    cityItem.setTmpInfo(weatherInfo.dailyForecast.get(0).cond.txtD);
                                    cityItem.setTmpMin(weatherInfo.dailyForecast.get(0).tmp.min);
                                    cityItem.setTmpMax(weatherInfo.dailyForecast.get(0).tmp.max);
                                    cityItem.setCond(weatherInfo.dailyForecast.get(0).cond.codeD);
                                    cityItem.save();
                                }
                                mCityList = DataSupport.findAll(Cityitem.class);
                                sharedPreferenceUtil.putString("lbs", weatherInfo.basic.city);
                                getSupportActionBar().setTitle(mCityList.get(0).getCityName());
                                if (mCityList.get(0).getCityName().equals(weatherInfo.basic.city)) {
                                    toolbar.setLogo(R.drawable.ic_action_location);
                                }
                                for (int i = 0; i < mCityList.size(); i++) {
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

        initData();
    }


    public class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            toolbar.setLogo(null);
            if (sharedPreferenceUtil.getString("lbs", "哈尔滨").equals(mCityList.get(position).getCityName())) {
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
        updataUi();
    }

    //bug 所在 数量不能决定操作
    private void updataUi() {
        mViewPager.setCurrentItem(getIntent().getIntExtra("cityPos", 0));
        //重启界面时数据更新就更新界面
        if (DataSupport.count(Cityitem.class) > mCityList.size()) {
            mCityList.add(DataSupport.findLast(Cityitem.class));
            mFragmentList.add(MainFragment.newInstance(mCityList.get(mCityList.size() - 1).getCityName()));
        } else if (DataSupport.count(Cityitem.class) < mCityList.size()) {
            mCityList = DataSupport.findAll(Cityitem.class);
            mFragmentList.clear();
            for (int i = 0; i < mCityList.size(); i++) {
                mFragmentList.add(MainFragment.newInstance(mCityList.get(i).getCityName()));
            }
        }
        initAdapter();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("jianshi", "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("jianshi", "onResume");
    }


    //sigleTask 启动的activity 更新intent
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
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //hide 软键盘
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().
                                getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                //初始化对话框
                dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("查询结果");
                RetroFactory.getInstance().getWeatherData(query, RetroFactory.API_KEY)
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
                                if (value.status.equals("ok")) {
                                    showDialog(value);
                                } else {
                                    showErrorDialog("错误代码:" + value.status);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                PublicMethod.ShowTips(MainActivity.this, "网络异常");
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    private void showDialog(final WeatherInfo weatherInfo) {
        dialog.setMessage(String.format("城市：%s\n" +
                        "当前温度：%s°C\n" +
                        "今日温度：%s°C-%s°C\n"
                        + "天气状况: %s", weatherInfo.basic.city,
                weatherInfo.now.tmp, weatherInfo.dailyForecast.get(0).tmp.min,
                weatherInfo.dailyForecast.get(0).tmp.max,
                weatherInfo.now.cond.txt));
        dialog.setPositiveButton("添加城市", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!DataSupport.isExist(Cityitem.class, "cityName = ?", weatherInfo.basic.city)) {
                    Cityitem cityItem = new Cityitem();
                    cityItem.setCityName(weatherInfo.basic.city);
                    cityItem.setTmpInfo(weatherInfo.dailyForecast.get(0).cond.txtD);
                    cityItem.setTmpMin(weatherInfo.dailyForecast.get(0).tmp.min);
                    cityItem.setTmpMax(weatherInfo.dailyForecast.get(0).tmp.max);
                    cityItem.setCond(weatherInfo.dailyForecast.get(0).cond.codeD);
                    cityItem.save();
                    updataUi();
                } else {
                    PublicMethod.ShowTips(MainActivity.this, weatherInfo.basic.city + "已存在");
                }
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();

    }

    private void showErrorDialog(String e) {
        dialog.setMessage(e);
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                break;
            case R.id.action_add:
                startActivity(new Intent(MainActivity.this, CityListActivity.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}
