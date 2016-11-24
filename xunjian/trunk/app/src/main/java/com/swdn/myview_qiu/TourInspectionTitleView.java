package com.swdn.myview_qiu;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.swdn.R;


/**
 * Created by lenovo on 2016/9/29.
 */

public class TourInspectionTitleView extends LinearLayout {

    private Paint mPaint;
    private Context mContext;

    public TourInspectionTitleView(Context context) {
        super(context);
        mPaint = new Paint();
    }

    public TourInspectionTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.TourInspectionTitleView);
        String titleName = a.getString(R.styleable.TourInspectionTitleView_titleName);
        float textSize = a.getDimension(R.styleable.TourInspectionTitleView_textSize, 36);
        mPaint.setTextSize(textSize);
        a.recycle();
        LayoutInflater.from(context).inflate(R.layout.tour_inspection_title, this);
        TextView tvTitleName = (TextView)findViewById(R.id.inspection_list_text);
        tvTitleName.setText(titleName);
//        ImageView backImage = (ImageView)findViewById(R.id.inspection_list_back_btn);
//        backImage.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ((Activity) getContext()).finish();
//            }
//        });


    }
}
