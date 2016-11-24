package com.swdn.adapter_xunjian;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.R;
import com.swdn.model_xunjian.TourInspectionDev;

import java.util.List;

/**
 * Created by lenovo on 2016/9/28.
 */

public class DetaiTourInspectionAdapter extends ArrayAdapter<TourInspectionDev> {
    private int resourceId;

    public DetaiTourInspectionAdapter(Context context, int resourceId, List<TourInspectionDev> objs) {
        super(context, resourceId, objs);
        this.resourceId = resourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final TourInspectionDev tiDev = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        //listview中每项的序号和点击回复按钮
        TextView tvSNo = (TextView)view.findViewById(R.id.tv_sno);
        tvSNo.setText(String.valueOf(position + 1));
//        Button btnToReply = (Button)view.findViewById(R.id.btn_toReply);
//        btnToReply.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(),TourInspectionReplyActivity.class);
//                intent.putExtra("data",tiDev);
//                view.getContext().startActivity(intent);
//            }
//        });
        TextView deviceId = (TextView)view.findViewById(R.id.tv_deviceId);
        TextView deviceName = (TextView)view.findViewById(R.id.tv_deviceName);
        TextView deviceType = (TextView)view.findViewById(R.id.tv_deviceType);
        TextView installLocation = (TextView)view.findViewById(R.id.tv_installLocation);
        TextView tourInspKey = (TextView)view.findViewById(R.id.tv_tourInspKey);
        TextView remark = (TextView)view.findViewById(R.id.tv_remark);
        TextView isCommited = (TextView) view.findViewById(R.id.tv_commitState);
        deviceId.setText(tiDev.getDevId());
        deviceName.setText(tiDev.getDevName());
        deviceType.setText(tiDev.getDevType());
        installLocation.setText(tiDev.getLocation());
        tourInspKey.setText(tiDev.getPretourKey());
        remark.setText(tiDev.getRemarks());
        if (tiDev.getIsCommited() == 0) {
            isCommited.setText("未提交");
        } else if (tiDev.getIsCommited() == 1) {
            isCommited.setText("已提交");
        }
        return view;
    }
}
