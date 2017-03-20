package com.example.yiuhet.first_weather;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AddCityActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("城市列表");

        CityListFragment cityListFragment = new CityListFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frament_citylist, cityListFragment)
                .commit();
    }
}
