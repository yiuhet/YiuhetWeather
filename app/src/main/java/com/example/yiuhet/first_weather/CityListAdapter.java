package com.example.yiuhet.first_weather;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by yiuhet on 2017/3/20.
 */

public class CityListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context Context;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context = parent.getContext();
        return new CityInfoViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_city_list,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class CityInfoViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout listLinearLayout;
        private TextView ListDate;
        private TextView ListTemp;
        private TextView ListTxt;

        public CityInfoViewHolder(View rootView) {
            super(rootView);
        }

    }
}
