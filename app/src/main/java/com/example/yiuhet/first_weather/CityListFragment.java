package com.example.yiuhet.first_weather;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yiuhet.first_weather.adapter.CityListAdapter;
import com.example.yiuhet.first_weather.db.Cityitem;
import com.example.yiuhet.first_weather.model.WeatherManager;
import com.example.yiuhet.first_weather.util.Utils;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CityListFragment extends Fragment {

    @BindView(R.id.recyclerview_citylist)
    RecyclerView recyclerviewCitylist;
    Unbinder unbinder;
    private RecyclerView recyclerView;
    CityListAdapter cityListAdapter;
    private List<Cityitem> cityitemList;
    private WeatherManager mWeatherManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeatherManager = WeatherManager.getInstance(getActivity().getApplicationContext());
        cityitemList = mWeatherManager.getWeatherList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_city_list, container, false);
        Log.d("life", "onCreateView");
        // Inflate the layout for this fragment
        initView(rootView);

        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    private void initView(View rootView) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_citylist);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        cityListAdapter = new CityListAdapter(cityitemList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(cityListAdapter);
        cityListAdapter.setOnItemClickListener(new CityListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                if (pos == -1) {
                    startActivityForResult(new Intent(getContext(), ChooseActivity.class), 1);
                } else {
                    Intent i = MainActivity.newIntent(getActivity(),pos);

                    startActivity(i);
                    getActivity().finish();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 666:
                cityListAdapter.addItem(DataSupport.findLast(Cityitem.class));
                recyclerView.scrollToPosition(0);
                break;
            case 233:
                Utils.ShowTips(getContext(), "网络异常");
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
