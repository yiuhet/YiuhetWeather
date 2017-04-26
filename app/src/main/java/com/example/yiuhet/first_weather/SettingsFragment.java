package com.example.yiuhet.first_weather;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import com.example.yiuhet.first_weather.util.SharedPreferenceUtil;

/**
 * Created by yiuhet on 2017/3/30.
 */

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        CheckBoxPreference checkboxLbs = (CheckBoxPreference) getPreferenceManager()
                .findPreference("isLbsOn");

        checkboxLbs.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean checked = Boolean.valueOf(newValue.toString());
                //保存到SharedPreferences中
                SharedPreferenceUtil.setBoolean(getActivity(),"isLbsOn",checked);
                return true;
            }
        });


    }

}
