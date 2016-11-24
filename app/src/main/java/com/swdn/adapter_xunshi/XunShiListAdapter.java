package com.swdn.adapter_xunshi;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.R;
import com.swdn.model_xunshi.TourInfo;

import java.util.List;

/**
 * Created by lenovo on 2016/11/15.
 */

public class XunShiListAdapter extends ArrayAdapter<TourInfo> {

    private int resourceId;
    public XunShiListAdapter(Context context, int resource, List<TourInfo> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
    }

    private TextView tv_pretourDate;
    private TextView tv_tourCategory;
    private TextView tv_tourAddress;
    private LinearLayout ll_receiver;
    private TextView tv_receiver;
    private LinearLayout ll_receiveDate;
    private TextView tv_receiveDate;
    private TextView tv_tourState;
    private LinearLayout ll_assist;
    private TextView tv_weather;
    private TextView tv_temperature;

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TourInfo tourInfo = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        initViewData(view,tourInfo);
        return view;
    }

    private void initViewData(View view,TourInfo tourInfo) {
        tv_pretourDate = (TextView) view.findViewById(R.id.tv_pretour_date);
        tv_tourCategory = (TextView) view.findViewById(R.id.tv_tour_category);
        tv_tourAddress = (TextView) view.findViewById(R.id.tv_tour_address);
        ll_receiver = (LinearLayout) view.findViewById(R.id.ll_tour_receiver);
        tv_receiver = (TextView) view.findViewById(R.id.tv_tour_receiver);
        ll_receiveDate = (LinearLayout) view.findViewById(R.id.ll_tour_receive_date);
        tv_receiveDate = (TextView) view.findViewById(R.id.tv_tour_receive_date);
        tv_tourState = (TextView) view.findViewById(R.id.tv_tour_stateDesc);
        ll_assist = (LinearLayout) view.findViewById(R.id.ll_assist);
        tv_weather = (TextView) view.findViewById(R.id.tv_tour_weather);
        tv_temperature = (TextView) view.findViewById(R.id.tv_tour_temperature);
        if (tourInfo.getIsFinished() == 1) {//已处理，处理完成
            //添加信息
            tv_pretourDate.setText(tourInfo.getPreTourDate());
            tv_tourCategory.setText(tourInfo.getTourCategory());
            tv_tourAddress.setText(tourInfo.getBds());
            tv_receiver.setText(tourInfo.getReceivePerson());
            tv_receiveDate.setText(tourInfo.getReceiveDate());
            ll_assist.setVisibility(View.GONE);
            tv_tourState.setText("处理完成");
            tv_tourState.setTextColor(Color.rgb(0x93, 0x95, 0x9f));
        } else if (tourInfo.getIsFinished() == 0) {//未处理
            if(tourInfo.getIsReceived() == 0) {//等待接单
                //添加信息
                tv_pretourDate.setText(tourInfo.getPreTourDate());
                tv_tourCategory.setText(tourInfo.getTourCategory());
                tv_tourAddress.setText(tourInfo.getBds());
                //隐藏某些控件
                ll_receiver.setVisibility(View.GONE);
                ll_receiveDate.setVisibility(View.GONE);
                ll_assist.setVisibility(View.GONE);
                //填写状态
                tv_tourState.setText("等待接单");
                tv_tourState.setTextColor(Color.rgb(0xe4, 0xd9, 0x77));
            }else if(tourInfo.getIsReceived() == 1){//等待处理
                //添加信息和点击事件
                tv_pretourDate.setText(tourInfo.getPreTourDate());
                tv_tourCategory.setText(tourInfo.getTourCategory());
                tv_tourAddress.setText(tourInfo.getBds());
                tv_receiver.setText(tourInfo.getReceivePerson());
                tv_receiveDate.setText(tourInfo.getReceiveDate());
                tv_weather.setText(tourInfo.getWeather());
                tv_temperature.setText(tourInfo.getTemperature());
                tv_tourState.setText("等待处理");
                tv_tourState.setTextColor(Color.rgb(0x61, 0xc9, 0x71));
            }
        }
    }
}
