package com.swdn.adapter_xunjian;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.R;
import com.swdn.model_xunjian.TourInspectionTask;

import java.util.List;

/**
 * Created by lenovo on 2016/9/28.
 */

public class TourInspectionAdapter extends ArrayAdapter<TourInspectionTask> {
    private int resourceId;

    public TourInspectionAdapter(Context context, int resourceId, List<TourInspectionTask> objects) {
        super(context,resourceId,objects);
        this.resourceId = resourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TourInspectionTask tiModel = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        ImageView exceInfo = (ImageView)view.findViewById(R.id.iv_exceInfo);
        TextView category = (TextView)view.findViewById(R.id.tv_category);
        TextView exceP = (TextView)view.findViewById(R.id.et_exceP);
        TextView exceDate = (TextView)view.findViewById(R.id.et_exceDate);
        if (tiModel.getIsFinished() == 0) {
            exceInfo.setImageResource(R.drawable.operating);
        } else if (tiModel.getIsFinished() == 1) {
            exceInfo.setImageResource(R.drawable.finish);
        }
        category.setText(tiModel.getCategory());
        exceP.setText(tiModel.getExecPerson());
        exceDate.setText(tiModel.getExecDate());
        return view;
    }
}
