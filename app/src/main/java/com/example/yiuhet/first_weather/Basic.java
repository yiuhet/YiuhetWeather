package com.example.yiuhet.first_weather;

/**
 * Created by yiuhet on 2017/3/18.
 */

public class Basic {
    public static String LocalCity;
    private static Basic instance=null;
    public static Basic getInstance(){
        if(instance==null){
            instance=new Basic();
        }
        return instance;
    }
    private Basic(){
    }
}
