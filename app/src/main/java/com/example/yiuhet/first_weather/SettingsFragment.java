package com.example.yiuhet.first_weather;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import com.example.yiuhet.first_weather.util.LocationUtils;
import com.example.yiuhet.first_weather.util.PublicMethod;
import com.example.yiuhet.first_weather.util.SharedPreferenceUtil;

import java.util.logging.Logger;

/**
 * Created by yiuhet on 2017/3/30.
 */

public class SettingsFragment extends PreferenceFragment {


    private SharedPreferenceUtil sharedPreferenceUtil;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        sharedPreferenceUtil = new SharedPreferenceUtil(getActivity(),"someData");
        CheckBoxPreference checkboxLbs = (CheckBoxPreference) getPreferenceManager()
                .findPreference("isLbsOn");
        CheckBoxPreference checkboxNotice = (CheckBoxPreference) getPreferenceManager()
                .findPreference("isNoticeOn");
        Preference btnReflash = getPreferenceManager().findPreference("timeResflash");

        checkboxLbs.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean checked = Boolean.valueOf(newValue.toString());
                //保存到SharedPreferences中3656
                sharedPreferenceUtil.putBoolean("isLbsOn",checked);
                return true;
            }
        });
        checkboxNotice.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean checked = Boolean.valueOf(newValue.toString());
                sharedPreferenceUtil.putBoolean("isNoticeOn",checked);
                return true;
            }
        });
        btnReflash.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                PublicMethod.ShowTips(getActivity(),"pppp");
                return true;
            }
        });
    }

}
