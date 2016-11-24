package com.swdn.myview_qiu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.R;

/**
 * Created by lenovo on 2016/11/14.
 */

public class TourCommonInfo extends LinearLayout {

    public TourCommonInfo(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.tour_common, this);
    }
}
