package com.swdn.myview_qiu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.swdn.R;


/**
 * Created by lenovo on 2016/9/29.
 */

public class TourInspectionDetailInfoView extends LinearLayout {

    public TourInspectionDetailInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.detailinfo_tour_inspection, this);
    }
}
