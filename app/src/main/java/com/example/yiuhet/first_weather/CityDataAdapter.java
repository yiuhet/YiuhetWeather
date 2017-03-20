package com.example.yiuhet.first_weather;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by yiuhet on 2017/3/15.
 */
class CityDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context Context;
    String result;
    private CityWeatherData mCityWeatherItems;
    private static final int TYPE_ONE = 0;
    private static final int TYPE_TWO = 2;
    private static final int TYPE_THREE = 1;

    public CityDataAdapter(CityWeatherData cityWeatherItems){
        mCityWeatherItems = cityWeatherItems;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == CityDataAdapter.TYPE_ONE) {
            return CityDataAdapter.TYPE_ONE;
        }
        if (position == CityDataAdapter.TYPE_TWO) {
            return CityDataAdapter.TYPE_TWO;
        }
        if (position == CityDataAdapter.TYPE_THREE) {
            return CityDataAdapter.TYPE_THREE;
        }
        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context = parent.getContext();
        switch (viewType) {
            case TYPE_ONE:
                return new NowWeatherViewHolder(
                        LayoutInflater.from(Context).inflate(
                                R.layout.item_shownow,parent,false));
            case TYPE_TWO:
                return new WeatherDataViewHolder(
                        LayoutInflater.from(Context).inflate(
                                R.layout.item_forecast,parent,false));
            case TYPE_THREE:
                return new HourlyWeatherViewHolder(
                        LayoutInflater.from(Context).inflate(
                                R.layout.item_hourly,parent,false));

        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemType = getItemViewType(position);
        switch (itemType) {
            case TYPE_ONE:
                ((NowWeatherViewHolder) holder).bind(mCityWeatherItems);
                break;
            case TYPE_TWO:
                ((WeatherDataViewHolder) holder).bind(mCityWeatherItems);
                break;
            case TYPE_THREE:
                ((HourlyWeatherViewHolder) holder).bind(mCityWeatherItems);
                break;
            default:
                break;
        }
    }


    @Override
    public int getItemCount() {
        return 3;
    }

    class NowWeatherViewHolder extends RecyclerView.ViewHolder{

        private TextView text_show_oc;
        private TextView text_show_info;
        private TextView text_show_pm;
        private TextView text_show_min;
        private TextView text_show_time;

        public NowWeatherViewHolder(View root) {
            super(root);
            text_show_oc = (TextView) root.findViewById(R.id.text_show_oc);
            text_show_info = (TextView) root.findViewById(R.id.text_show_info);
            text_show_pm = (TextView) root.findViewById(R.id.text_show_pm);
            text_show_min = (TextView) root.findViewById(R.id.text_show_min);
            text_show_time = (TextView) root.findViewById(R.id.refresh_time);
        }

        public TextView getText_show_oc() {
            return text_show_oc;
        }

        public TextView getText_show_info() {
            return text_show_info;
        }

        public TextView getText_show_min() {
            return text_show_min;
        }

        public TextView getText_show_pm() {
            return text_show_pm;
        }

        protected void bind(CityWeatherData item) {
            text_show_oc.setText(item.now_tmp + "°C");
            text_show_info.setText(item.now_cond_txt);
            text_show_time.setText("上次探测更新:"+item.basic_update.split(" ")[1]);
            text_show_min.setText( item.daily_Data_List.get(0).daily_Tmp_Min+ "°C/" + item.daily_Data_List.get(0).daily_Tmp_Max+"°C");
            text_show_pm.setText(item.basic_City);
        }
    }

    class WeatherDataViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout forecastLinear;
        private TextView[] forecastDate = new TextView[mCityWeatherItems.daily_Data_List.size()];
        private TextView[] forecastTemp = new TextView[7];
        private TextView[] forecastTxt = new TextView[7];
        private ImageView[] forecastIcon = new ImageView[7];
        public WeatherDataViewHolder(View itemView) {
            super(itemView);
            forecastLinear = (LinearLayout) itemView.findViewById(R.id.forecast_linear);
            for (int i = 0; i < mCityWeatherItems.daily_Data_List.size(); i++) {
                View view = View.inflate(Context, R.layout.item_forecast_line, null);
                forecastDate[i] = (TextView) view.findViewById(R.id.forecast_date);
                forecastTemp[i] = (TextView) view.findViewById(R.id.forecast_temp);
                forecastTxt[i] = (TextView) view.findViewById(R.id.forecast_txt);
                forecastLinear.addView(view);
            }
        }

        protected void bind(CityWeatherData cityWeatherData) {
            forecastDate[0].setText("今日");
            forecastDate[1].setText("明日");
            for (int i = 0; i < mCityWeatherItems.daily_Data_List.size(); i++) {
                if (i > 1) {
                    forecastDate[i].setText(
                            mCityWeatherItems.daily_Data_List.get(i).daily_Date);
                }
                forecastTemp[i].setText(
                        String.format("%s℃ - %s℃",
                                mCityWeatherItems.daily_Data_List.get(i).daily_Tmp_Min,
                                mCityWeatherItems.daily_Data_List.get(i).daily_Tmp_Max)
                );
                forecastTxt[i].setText(
                        String.format("%s。 %s 风速： %s ",
                                mCityWeatherItems.daily_Data_List.get(i).daily_Cond_Txtd,
                                mCityWeatherItems.daily_Data_List.get(i).daily_Wind_Dir,
                                mCityWeatherItems.daily_Data_List.get(i).daily_Wind_Sc
                                ));
            }
        }
    }

    class HourlyWeatherViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout hourlyLinear;
        private TextView[] hourlyDate = new TextView[mCityWeatherItems.hourly_dates_List.size()];
        private TextView[] hourlyTemp = new TextView[mCityWeatherItems.hourly_dates_List.size()];
        private TextView[] hourlyTxt = new TextView[mCityWeatherItems.hourly_dates_List.size()];
        public HourlyWeatherViewHolder(View itemView) {
            super(itemView);
            hourlyLinear = (LinearLayout) itemView.findViewById(R.id.hourly_linear);
            for (int i = 0; i < mCityWeatherItems.hourly_dates_List.size(); i++) {
                View view = View.inflate(Context, R.layout.item_hourly_item, null);
                hourlyDate[i] = (TextView) view.findViewById(R.id.hourly_time);
                hourlyTemp[i] = (TextView) view.findViewById(R.id.hourly_tmp);
                hourlyTxt[i] = (TextView) view.findViewById(R.id.hourly_cond);
                hourlyLinear.addView(view);
            }
        }
        protected void bind(CityWeatherData cityWeatherData) {
            for (int i = 0; i < mCityWeatherItems.hourly_dates_List.size(); i++) {
                hourlyDate[i].setText(cityWeatherData.hourly_dates_List.get(i).hourly_Date.split(" ")[1]);
                hourlyTemp[i].setText(cityWeatherData.hourly_dates_List.get(i).hourly_Tmp+"°C");
                hourlyTxt[i].setText(cityWeatherData.hourly_dates_List.get(i).hourly_Cond_Txt);
            }
        }

    }

}
