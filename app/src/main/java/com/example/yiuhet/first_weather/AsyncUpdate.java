package com.example.yiuhet.first_weather;

/**
 * Created by yiuhet on 2017/3/18.
 */

public interface AsyncUpdate {
    public void onFinsh(String city);

    public void onLocationError(String ErrorCode);
}
