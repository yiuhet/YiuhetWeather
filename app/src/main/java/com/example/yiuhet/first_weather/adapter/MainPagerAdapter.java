package com.example.yiuhet.first_weather.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.yiuhet.first_weather.MainFragment;
import com.example.yiuhet.first_weather.db.Cityitem;

import java.util.List;

/**
 * Created by yiuhet on 2017/3/27.
 */

public class MainPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragmentList;

    public MainPagerAdapter(FragmentManager fm,List<Fragment> fragmentList) {
        super(fm);
        mFragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }


}
