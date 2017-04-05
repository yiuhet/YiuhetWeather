package com.example.yiuhet.first_weather;

import android.app.FragmentManager;
import android.preference.CheckBoxPreference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingActivity extends BaseActivity {

    private SettingsFragment mSettingsFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setTitle("设置");
        if (savedInstanceState == null) {
            mSettingsFragment = new SettingsFragment();
            replaceFragment(R.id.settings_container, mSettingsFragment);
        }
    }

    public void replaceFragment(int viewId, android.app.Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(viewId, fragment).commit();
    }
}
