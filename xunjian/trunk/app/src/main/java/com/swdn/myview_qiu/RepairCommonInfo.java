package com.swdn.myview_qiu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.swdn.R;

import java.util.zip.Inflater;

/**
 * Created by lenovo on 2016/10/22.
 */

public class RepairCommonInfo extends LinearLayout {
    public RepairCommonInfo(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.repair_common, this);

    }
}
