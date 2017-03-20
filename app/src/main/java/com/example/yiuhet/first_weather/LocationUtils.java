package com.example.yiuhet.first_weather;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * Created by yiuhet on 2017/3/18.
 */

public class LocationUtils {

    private Context mContext;
    private String City = "未定位到...";
    Toolbar toolbar;
    AsyncUpdate mAsyncUpdate;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    public LocationUtils(Context context,final AsyncUpdate finshListener) {
        mAsyncUpdate = finshListener;
        mContext = context;
    }
    public void start(){
        //初始化定位
        mLocationClient = new AMapLocationClient(mContext);
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    //toolbar.setTitle(aMapLocation.getCity());//城市信息
                    //mAsyncUpdate.onFinsh(aMapLocation.getCity());
                    //Log.d("ithink",aMapLocation.getCity());
                    if (mAsyncUpdate != null) {
                        Log.d("ithink",aMapLocation.getCity());
                        mAsyncUpdate.onFinsh(aMapLocation.getCity());
                    } else {
                        mAsyncUpdate.onLocationError(String.valueOf(aMapLocation.getErrorCode()));
                    }
                    mLocationClient.stopLocation();
                    mLocationClient.onDestroy();
                }else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError","location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    };
}
