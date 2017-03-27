package com.example.yiuhet.first_weather.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yiuhet on 2017/3/24.
 */

public class WeatherInfo {

    /**
     * aqi : {"city":{"aqi":"58","co":"1","no2":"41","o3":"108","pm10":"56","pm25":"41","qlty":"良","so2":"12"}}
     * basic : {"city":"上海","cnty":"中国","id":"CN101020100","lat":"31.213000","lon":"121.445000","update":{"loc":"2017-03-24 15:51","utc":"2017-03-24 07:51"}}
     * daily_forecast : [{"astro":{"mr":"03:12","ms":"14:16","sr":"05:52","ss":"18:08"},"cond":{"code_d":"305","code_n":"104","txt_d":"小雨","txt_n":"阴"},"date":"2017-03-24","hum":"81","pcpn":"4.2","pop":"96","pres":"1022","tmp":{"max":"12","min":"8"},"uv":"5","vis":"16","wind":{"deg":"84","dir":"东北风","sc":"微风","spd":"0"}},{"astro":{"mr":"03:54","ms":"15:15","sr":"05:51","ss":"18:08"},"cond":{"code_d":"101","code_n":"101","txt_d":"多云","txt_n":"多云"},"date":"2017-03-25","hum":"78","pcpn":"0.3","pop":"96","pres":"1021","tmp":{"max":"16","min":"9"},"uv":"6","vis":"18","wind":{"deg":"279","dir":"西北风","sc":"微风","spd":"2"}},{"astro":{"mr":"04:35","ms":"16:16","sr":"05:50","ss":"18:09"},"cond":{"code_d":"101","code_n":"100","txt_d":"多云","txt_n":"晴"},"date":"2017-03-26","hum":"71","pcpn":"0.0","pop":"9","pres":"1023","tmp":{"max":"17","min":"8"},"uv":"8","vis":"20","wind":{"deg":"239","dir":"西北风","sc":"微风","spd":"3"}},{"astro":{"mr":"05:16","ms":"17:19","sr":"05:48","ss":"18:10"},"cond":{"code_d":"100","code_n":"100","txt_d":"晴","txt_n":"晴"},"date":"2017-03-27","hum":"62","pcpn":"0.0","pop":"0","pres":"1024","tmp":{"max":"19","min":"11"},"uv":"7","vis":"20","wind":{"deg":"244","dir":"西北风","sc":"微风","spd":"1"}},{"astro":{"mr":"05:56","ms":"18:23","sr":"05:47","ss":"18:10"},"cond":{"code_d":"100","code_n":"101","txt_d":"晴","txt_n":"多云"},"date":"2017-03-28","hum":"64","pcpn":"0.0","pop":"4","pres":"1022","tmp":{"max":"20","min":"12"},"uv":"7","vis":"20","wind":{"deg":"169","dir":"东南风","sc":"微风","spd":"4"}},{"astro":{"mr":"06:36","ms":"19:29","sr":"05:46","ss":"18:11"},"cond":{"code_d":"101","code_n":"104","txt_d":"多云","txt_n":"阴"},"date":"2017-03-29","hum":"73","pcpn":"1.9","pop":"75","pres":"1019","tmp":{"max":"18","min":"10"},"uv":"4","vis":"17","wind":{"deg":"176","dir":"北风","sc":"微风","spd":"2"}},{"astro":{"mr":"07:19","ms":"20:35","sr":"05:45","ss":"18:12"},"cond":{"code_d":"300","code_n":"101","txt_d":"阵雨","txt_n":"多云"},"date":"2017-03-30","hum":"90","pcpn":"2.5","pop":"86","pres":"1020","tmp":{"max":"15","min":"8"},"uv":"3","vis":"13","wind":{"deg":"37","dir":"东北风","sc":"微风","spd":"0"}}]
     * hourly_forecast : [{"cond":{"code":"104","txt":"阴"},"date":"2017-03-24 16:00","hum":"77","pop":"70","pres":"1021","tmp":"11","wind":{"deg":"61","dir":"东北风","sc":"微风","spd":"12"}},{"cond":{"code":"305","txt":"小雨"},"date":"2017-03-24 19:00","hum":"88","pop":"96","pres":"1022","tmp":"9","wind":{"deg":"56","dir":"东北风","sc":"微风","spd":"6"}},{"cond":{"code":"306","txt":"中雨"},"date":"2017-03-24 22:00","hum":"90","pop":"94","pres":"1023","tmp":"8","wind":{"deg":"191","dir":"西南风","sc":"微风","spd":"3"}}]
     * now : {"cond":{"code":"104","txt":"阴"},"fl":"11","hum":"63","pcpn":"0","pres":"1019","tmp":"11","vis":"7","wind":{"deg":"360","dir":"西北风","sc":"微风","spd":"8"}}
     * status : ok
     * suggestion : {"air":{"brf":"中","txt":"气象条件对空气污染物稀释、扩散和清除无明显影响，易感人群应适当减少室外活动时间。"},"comf":{"brf":"较舒适","txt":"白天会有降雨，这种天气条件下，人们会感到有些凉意，但大部分人完全可以接受。"},"cw":{"brf":"不宜","txt":"不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"},"drsg":{"brf":"较冷","txt":"建议着厚外套加毛衣等服装。年老体弱者宜着大衣、呢外套加羊毛衫。"},"flu":{"brf":"易发","txt":"天冷空气湿度大，易发生感冒，请注意适当增加衣服，加强自我防护避免感冒。"},"sport":{"brf":"较不宜","txt":"有降水，推荐您在室内进行各种健身休闲运动，若坚持户外运动，须注意保暖并携带雨具。"},"trav":{"brf":"适宜","txt":"温度适宜，又有较弱降水和微风作伴，会给您的旅行带来意想不到的景象，适宜旅游，可不要错过机会呦！"},"uv":{"brf":"最弱","txt":"属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。"}}
     */

    @SerializedName("aqi")
    public AqiEntity aqi;
    @SerializedName("basic")
    public BasicEntity basic;
    @SerializedName("now")
    public NowEntity now;
    @SerializedName("status")
    public String status;
    @SerializedName("suggestion")
    public SuggestionEntity suggestion;
    @SerializedName("daily_forecast")
    public List<DailyForecastEntity> dailyForecast;
    @SerializedName("hourly_forecast")
    public List<HourlyForecastEntity> hourlyForecast;

    public static WeatherInfo objectFromData(String str) {

        return new Gson().fromJson(str, WeatherInfo.class);
    }

    public static class AqiEntity {
        /**
         * city : {"aqi":"58","co":"1","no2":"41","o3":"108","pm10":"56","pm25":"41","qlty":"良","so2":"12"}
         */

        @SerializedName("city")
        public CityEntity city;

        public static AqiEntity objectFromData(String str) {

            return new Gson().fromJson(str, AqiEntity.class);
        }

        public static class CityEntity {
            /**
             * aqi : 58
             * co : 1
             * no2 : 41
             * o3 : 108
             * pm10 : 56
             * pm25 : 41
             * qlty : 良
             * so2 : 12
             */

            @SerializedName("aqi")
            public String aqi;
            @SerializedName("co")
            public String co;
            @SerializedName("no2")
            public String no2;
            @SerializedName("o3")
            public String o3;
            @SerializedName("pm10")
            public String pm10;
            @SerializedName("pm25")
            public String pm25;
            @SerializedName("qlty")
            public String qlty;
            @SerializedName("so2")
            public String so2;

            public static CityEntity objectFromData(String str) {

                return new Gson().fromJson(str, CityEntity.class);
            }
        }
    }

    public static class BasicEntity {
        /**
         * city : 上海
         * cnty : 中国
         * id : CN101020100
         * lat : 31.213000
         * lon : 121.445000
         * update : {"loc":"2017-03-24 15:51","utc":"2017-03-24 07:51"}
         */

        @SerializedName("city")
        public String city;
        @SerializedName("cnty")
        public String cnty;
        @SerializedName("id")
        public String id;
        @SerializedName("lat")
        public String lat;
        @SerializedName("lon")
        public String lon;
        @SerializedName("update")
        public UpdateEntity update;

        public static BasicEntity objectFromData(String str) {

            return new Gson().fromJson(str, BasicEntity.class);
        }

        public static class UpdateEntity {
            /**
             * loc : 2017-03-24 15:51
             * utc : 2017-03-24 07:51
             */

            @SerializedName("loc")
            public String loc;
            @SerializedName("utc")
            public String utc;

            public static UpdateEntity objectFromData(String str) {

                return new Gson().fromJson(str, UpdateEntity.class);
            }
        }
    }

    public static class NowEntity {
        /**
         * cond : {"code":"104","txt":"阴"}
         * fl : 11
         * hum : 63
         * pcpn : 0
         * pres : 1019
         * tmp : 11
         * vis : 7
         * wind : {"deg":"360","dir":"西北风","sc":"微风","spd":"8"}
         */

        @SerializedName("cond")
        public CondEntity cond;
        @SerializedName("fl")
        public String fl;
        @SerializedName("hum")
        public String hum;
        @SerializedName("pcpn")
        public String pcpn;
        @SerializedName("pres")
        public String pres;
        @SerializedName("tmp")
        public String tmp;
        @SerializedName("vis")
        public String vis;
        @SerializedName("wind")
        public WindEntity wind;

        public static NowEntity objectFromData(String str) {

            return new Gson().fromJson(str, NowEntity.class);
        }

        public static class CondEntity {
            /**
             * code : 104
             * txt : 阴
             */

            @SerializedName("code")
            public String code;
            @SerializedName("txt")
            public String txt;

            public static CondEntity objectFromData(String str) {

                return new Gson().fromJson(str, CondEntity.class);
            }
        }

        public static class WindEntity {
            /**
             * deg : 360
             * dir : 西北风
             * sc : 微风
             * spd : 8
             */

            @SerializedName("deg")
            public String deg;
            @SerializedName("dir")
            public String dir;
            @SerializedName("sc")
            public String sc;
            @SerializedName("spd")
            public String spd;

            public static WindEntity objectFromData(String str) {

                return new Gson().fromJson(str, WindEntity.class);
            }
        }
    }

    public static class SuggestionEntity {
        /**
         * air : {"brf":"中","txt":"气象条件对空气污染物稀释、扩散和清除无明显影响，易感人群应适当减少室外活动时间。"}
         * comf : {"brf":"较舒适","txt":"白天会有降雨，这种天气条件下，人们会感到有些凉意，但大部分人完全可以接受。"}
         * cw : {"brf":"不宜","txt":"不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"}
         * drsg : {"brf":"较冷","txt":"建议着厚外套加毛衣等服装。年老体弱者宜着大衣、呢外套加羊毛衫。"}
         * flu : {"brf":"易发","txt":"天冷空气湿度大，易发生感冒，请注意适当增加衣服，加强自我防护避免感冒。"}
         * sport : {"brf":"较不宜","txt":"有降水，推荐您在室内进行各种健身休闲运动，若坚持户外运动，须注意保暖并携带雨具。"}
         * trav : {"brf":"适宜","txt":"温度适宜，又有较弱降水和微风作伴，会给您的旅行带来意想不到的景象，适宜旅游，可不要错过机会呦！"}
         * uv : {"brf":"最弱","txt":"属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。"}
         */

        @SerializedName("air")
        public AirEntity air;
        @SerializedName("comf")
        public AirEntity comf;
        @SerializedName("cw")
        public AirEntity cw;
        @SerializedName("drsg")
        public AirEntity drsg;
        @SerializedName("flu")
        public AirEntity flu;
        @SerializedName("sport")
        public AirEntity sport;
        @SerializedName("trav")
        public AirEntity trav;
        @SerializedName("uv")
        public AirEntity uv;

        public static SuggestionEntity objectFromData(String str) {

            return new Gson().fromJson(str, SuggestionEntity.class);
        }

        public static class AirEntity {
            /**
             * brf : 中
             * txt : 气象条件对空气污染物稀释、扩散和清除无明显影响，易感人群应适当减少室外活动时间。
             */

            @SerializedName("brf")
            public String brf;
            @SerializedName("txt")
            public String txt;

            public static AirEntity objectFromData(String str) {

                return new Gson().fromJson(str, AirEntity.class);
            }
        }
    }

    public static class DailyForecastEntity {
        /**
         * astro : {"mr":"03:12","ms":"14:16","sr":"05:52","ss":"18:08"}
         * cond : {"code_d":"305","code_n":"104","txt_d":"小雨","txt_n":"阴"}
         * date : 2017-03-24
         * hum : 81
         * pcpn : 4.2
         * pop : 96
         * pres : 1022
         * tmp : {"max":"12","min":"8"}
         * uv : 5
         * vis : 16
         * wind : {"deg":"84","dir":"东北风","sc":"微风","spd":"0"}
         */

        @SerializedName("astro")
        public AstroEntity astro;
        @SerializedName("cond")
        public CondEntityX cond;
        @SerializedName("date")
        public String date;
        @SerializedName("hum")
        public String hum;
        @SerializedName("pcpn")
        public String pcpn;
        @SerializedName("pop")
        public String pop;
        @SerializedName("pres")
        public String pres;
        @SerializedName("tmp")
        public TmpEntity tmp;
        @SerializedName("uv")
        public String uv;
        @SerializedName("vis")
        public String vis;
        @SerializedName("wind")
        public WindEntity wind;

        public static DailyForecastEntity objectFromData(String str) {

            return new Gson().fromJson(str, DailyForecastEntity.class);
        }

        public static class AstroEntity {
            /**
             * mr : 03:12
             * ms : 14:16
             * sr : 05:52
             * ss : 18:08
             */

            @SerializedName("mr")
            public String mr;
            @SerializedName("ms")
            public String ms;
            @SerializedName("sr")
            public String sr;
            @SerializedName("ss")
            public String ss;

            public static AstroEntity objectFromData(String str) {

                return new Gson().fromJson(str, AstroEntity.class);
            }
        }

        public static class CondEntityX {
            /**
             * code_d : 305
             * code_n : 104
             * txt_d : 小雨
             * txt_n : 阴
             */

            @SerializedName("code_d")
            public String codeD;
            @SerializedName("code_n")
            public String codeN;
            @SerializedName("txt_d")
            public String txtD;
            @SerializedName("txt_n")
            public String txtN;

            public static CondEntityX objectFromData(String str) {

                return new Gson().fromJson(str, CondEntityX.class);
            }
        }

        public static class TmpEntity {
            /**
             * max : 12
             * min : 8
             */

            @SerializedName("max")
            public String max;
            @SerializedName("min")
            public String min;

            public static TmpEntity objectFromData(String str) {

                return new Gson().fromJson(str, TmpEntity.class);
            }
        }
        public static class WindEntity {
            @SerializedName("dir")
            public String dir;
            @SerializedName("sc")
            public String sc;
            @SerializedName("spd")
            public String spd;
        }

    }

    public static class HourlyForecastEntity {
        /**
         * cond : {"code":"104","txt":"阴"}
         * date : 2017-03-24 16:00
         * hum : 77
         * pop : 70
         * pres : 1021
         * tmp : 11
         * wind : {"deg":"61","dir":"东北风","sc":"微风","spd":"12"}
         */

        @SerializedName("date")
        public String date;
        @SerializedName("hum")
        public String hum;
        @SerializedName("pop")
        public String pop;
        @SerializedName("pres")
        public String pres;
        @SerializedName("tmp")
        public String tmp;
        @SerializedName("cond")
        public CondEntityY cond;

        public static class CondEntityY{
            @SerializedName("code")
            public String code;
            @SerializedName("txt")
            public String txt;
        }

        public static HourlyForecastEntity objectFromData(String str) {

            return new Gson().fromJson(str, HourlyForecastEntity.class);
        }

    }
}
