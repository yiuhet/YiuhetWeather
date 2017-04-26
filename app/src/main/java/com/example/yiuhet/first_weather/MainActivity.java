package com.example.yiuhet.first_weather;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
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
import com.example.yiuhet.first_weather.model.WeatherManager;
import com.example.yiuhet.first_weather.util.LocationUtils;
import com.example.yiuhet.first_weather.util.Utils;
import com.example.yiuhet.first_weather.util.RetroFactory;
import com.example.yiuhet.first_weather.util.SharedPreferenceUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;
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

    private static final String INTENTINFO = "MainActivityIntent";

    private List<Fragment> mFragmentList ;
    private SearchView searchView;
    private AlertDialog.Builder dialog;
    private MainPagerAdapter mMainPagerAdapter;
    private List<Cityitem> mCityList;
    private WeatherManager mWeatherManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission();
        }
        initData();
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            },
                    0);//自定义的code
        }
    }


    private void initView() {
        //状态栏透明
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | params.flags);
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                //将侧边栏顶部延伸至status bar
                mDrawerLayout.setFitsSystemWindows(true);
                //将主页面顶部延伸至status bar;虽默认为false,但经测试,DrawerLayout需显示设置
                mDrawerLayout.setClipToPadding(false);
            }
        }
        setSupportActionBar(toolbar);
        initDrawer();
    }

    //初始化侧边栏菜单 --
    private void initDrawer() {
        if (mNavigationView != null) {
            mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    switch (item.getItemId()) {
                        case R.id.nav_search:
                            searchView.setIconified(false);
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

    //更新视图
    private void updateUI() {
        if (mFragmentList == null) {
            mFragmentList = new ArrayList<>();
        } else {
            mFragmentList.clear();
        }
        mCityList = mWeatherManager.getWeatherList();
        for (int i = 0; i < mCityList.size(); i++) {
            mFragmentList.add(MainFragment.newInstance(mCityList.get(i).getCityName()));
        }
        if (mMainPagerAdapter == null) {
            mMainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), mFragmentList);
            mViewPager.setAdapter(mMainPagerAdapter);
            mViewPager.addOnPageChangeListener(new MyPageChangeListener());
        } else {
            mMainPagerAdapter.notifyDataSetChanged();
            mViewPager.setCurrentItem(getIntent().getIntExtra(INTENTINFO, 0));
        }
        indicator.setViewPager(mViewPager);

    }

    //初始化定位和列表数据
    private void initData() {
        mWeatherManager = WeatherManager.getInstance(getApplicationContext());
        if (mWeatherManager.isLbsOn()){
            lbsOnGetData();
        }else {
            updateUI();
        }
    }

    private void lbsOnGetData() {
        new LocationUtils(getApplicationContext(), new AsyncUpdate() {
            @Override
            public void onFinsh(String city) {
                SharedPreferenceUtil.setString(getApplicationContext(),"lbs", city);
                toolbar.setTitle(city);
                toolbar.setLogo(R.drawable.ic_action_location);
                if (!mWeatherManager.isExit(city)) {
                    RetroFactory.getInstance().fetchWeather(city)
                            .subscribe(new Consumer<WeatherInfo>() {
                                @Override
                                public void accept(WeatherInfo weatherInfo) throws Exception {
                                    if (weatherInfo.status.equals("ok")) {
                                        Cityitem cityItem = new Cityitem();
                                        cityItem.setCityName(weatherInfo.basic.city);
                                        cityItem.setTmpInfo(weatherInfo.dailyForecast.get(0).cond.txtD);
                                        cityItem.setTmpMin(weatherInfo.dailyForecast.get(0).tmp.min);
                                        cityItem.setTmpMax(weatherInfo.dailyForecast.get(0).tmp.max);
                                        cityItem.setCond(weatherInfo.dailyForecast.get(0).cond.codeD);
                                        cityItem.save();
                                        updateUI();
                                    } else {
                                        showErrorDialog("错误代码:" + weatherInfo.status);
                                    }
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    Utils.ShowTips(MainActivity.this, "网络异常");
                                }
                            });
                }

            }
            @Override
            public void onLocationError(String ErrorCode) {
                toolbar.setTitle("定位失败。。。");
            }
        }).start();
    }

    public class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            toolbar.setLogo(null);
            if ((mWeatherManager.getLbsCity()).equals(mCityList.get(position).getCityName())) {
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
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWeatherManager != null) {
            updateUI();
        }
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

                RetroFactory.getInstance().fetchWeather(query)
                        .subscribe(new Consumer<WeatherInfo>() {
                            @Override
                            public void accept(WeatherInfo weatherInfo) throws Exception {
                                if (weatherInfo.status.equals("ok")) {
                                    showDialog(weatherInfo);
                                } else {
                                    showErrorDialog("错误代码:" + weatherInfo.status);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Utils.ShowTips(MainActivity.this, "网络异常");
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
                if (mWeatherManager.isExit(weatherInfo.basic.city)) {
                    Cityitem cityItem = new Cityitem();
                    cityItem.setCityName(weatherInfo.basic.city);
                    cityItem.setTmpInfo(weatherInfo.dailyForecast.get(0).cond.txtD);
                    cityItem.setTmpMin(weatherInfo.dailyForecast.get(0).tmp.min);
                    cityItem.setTmpMax(weatherInfo.dailyForecast.get(0).tmp.max);
                    cityItem.setCond(weatherInfo.dailyForecast.get(0).cond.codeD);
                    cityItem.save();
                    updateUI();
                } else {
                    Utils.ShowTips(MainActivity.this, weatherInfo.basic.city + "已存在");
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

    public static Intent newIntent(Context context,int pos) {
        Intent i = new Intent(context,MainActivity.class);
        i.putExtra(INTENTINFO,pos);
        return i;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initData();
            } else {
                Utils.ShowTips(getApplicationContext(),"未获得定位权限，请手动添加。。。");
            }
        }
    }
}
