package com.example.yiuhet.first_weather;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.yiuhet.first_weather.adapter.MainPagerAdapter;
import com.example.yiuhet.first_weather.db.Cityitem;
import com.example.yiuhet.first_weather.model.AsyncUpdate;
import com.example.yiuhet.first_weather.model.CityWeatherData;
import com.example.yiuhet.first_weather.util.HttpUtil;
import com.example.yiuhet.first_weather.util.LocationUtils;
import com.example.yiuhet.first_weather.util.PublicMethod;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    String cityName;
    private List<Cityitem> mCityList;
    private List<Fragment> mFragmentList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermission();  // 请求权限 bug 初次加载
        initData();
        initView();
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

    private void initData() {
        mCityList = DataSupport.findAll(Cityitem.class);
        for (int i=0;i<mCityList.size();i++) {
            mFragmentList.add(MainFragment.newInstance(mCityList.get(i).getCityName()));
        }

    }

    private void initBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(mCityList.get(0).getCityName());
        if (cityName == null) {
            new LocationUtils(getApplicationContext(), new AsyncUpdate() {
                @Override
                public void onFinsh(String city) {
                    //toolbar.setTitle(city);
                    cityName = city;
                }
                @Override
                public void onLocationError(String ErrorCode) {
                }
            }).start();
        }
    }


    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        MainPagerAdapter mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(),mFragmentList);
        mViewPager.setAdapter(mainPagerAdapter);
        mViewPager.setCurrentItem(getIntent().getIntExtra("cityPos",0));
        mViewPager.addOnPageChangeListener(new MyPageChangeListener());
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mViewPager);
        //mainPagerAdapter.registerDataSetObserver(indicator.getDataSetObserver());
    }

    public class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            toolbar.setTitle(mCityList.get(position).getCityName());
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
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
