package com.swdn.adapter_qiangxiu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.swdn.R;
import com.swdn.activity_qiangxiu.PreplanningInfo;
import com.swdn.activity_qiangxiu.ToolsRemind;
import com.swdn.model_qiangxiu.RepairInfo;
import com.swdn.myappllication.MyApplication;

import java.util.List;

/**
 * Created by lenovo on 2016/10/21.
 */

public class QiangXiuListAdapter extends ArrayAdapter<RepairInfo> implements View.OnClickListener {

    private int resourceId;

    public QiangXiuListAdapter(Context context, int resource, List<RepairInfo> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RepairInfo repairInfo = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);

        TextView tvManager = (TextView) view.findViewById(R.id.tv_manager);
        TextView tvCategory = (TextView) view.findViewById(R.id.tv_category);
        TextView tvAddress = (TextView) view.findViewById(R.id.tv_address);
        TextView tvReceiver = (TextView) view.findViewById(R.id.tv_receiver);
        TextView tvReceiveDate = (TextView) view.findViewById(R.id.tv_receiveDate);
        TextView tvStateDesc = (TextView) view.findViewById(R.id.stateDesc);

        TextView tvPreplanning = (TextView) view.findViewById(R.id.tv_preplanning);
        TextView tvToolRemind = (TextView) view.findViewById(R.id.tv_toolRemind);
        TextView tvComeHere = (TextView) view.findViewById(R.id.tv_comehere);

        LinearLayout llReceiver = (LinearLayout) view.findViewById(R.id.ll_receiver);
        LinearLayout llReceiveDate = (LinearLayout) view.findViewById(R.id.ll_receiveDate);
        LinearLayout llAssist = (LinearLayout) view.findViewById(R.id.ll_assist);

        if (repairInfo.getProcessState() == 1) {//已处理，处理完成
            //添加信息
            tvManager.setText(repairInfo.getManager());
            tvCategory.setText(repairInfo.getCategory());
            tvAddress.setText(repairInfo.getAddress());
            tvReceiver.setText(repairInfo.getReceiver());
            tvReceiveDate.setText(repairInfo.getReceiveDate());
            llAssist.setVisibility(View.GONE);
            tvStateDesc.setText("处理完成");
            tvStateDesc.setTextColor(Color.rgb(0x93, 0x95, 0x9f));
        } else if (repairInfo.getProcessState() == 0) {//未处理
            if(repairInfo.getReceiveState() == 0) {//等待接单
                //添加信息
                tvManager.setText(repairInfo.getManager());
                tvCategory.setText(repairInfo.getCategory());
                tvAddress.setText(repairInfo.getAddress());
                //隐藏某些控件
                llReceiver.setVisibility(View.GONE);
                llReceiveDate.setVisibility(View.GONE);
                llAssist.setVisibility(View.GONE);
                tvStateDesc.setText("等待接单");
                tvStateDesc.setTextColor(Color.rgb(0xe4, 0xd9, 0x77));
            }else if(repairInfo.getReceiveState() == 1){//等待处理
                //添加信息和点击事件
                tvManager.setText(repairInfo.getManager());
                tvCategory.setText(repairInfo.getCategory());
                tvAddress.setText(repairInfo.getAddress());
                tvReceiver.setText(repairInfo.getReceiver());
                tvReceiveDate.setText(repairInfo.getReceiveDate());
                tvPreplanning.setOnClickListener(this);
                tvToolRemind.setOnClickListener(this);
                tvComeHere.setOnClickListener(this);
                tvStateDesc.setText("等待处理");
                tvStateDesc.setTextColor(Color.rgb(0x61, 0xc9, 0x71));
            }
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_preplanning:
//                Toast.makeText(getContext(), "预案", Toast.LENGTH_SHORT).show();
                Intent intentPreplanning = new Intent(MyApplication.getContext(),PreplanningInfo.class);
                intentPreplanning.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApplication.getContext().startActivity(intentPreplanning);
                break;
            case R.id.tv_toolRemind:
//                Toast.makeText(getContext(), "工具提醒", Toast.LENGTH_SHORT).show();
                Intent intentToolsRemind = new Intent(MyApplication.getContext(),ToolsRemind.class);
                intentToolsRemind.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApplication.getContext().startActivity(intentToolsRemind);
                break;
            case R.id.tv_comehere:
                Toast.makeText(getContext(), "到这里去", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
