package com.swdn.activity_qiangxiu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.swdn.R;

public class AlarmInfo extends Activity implements View.OnClickListener{

    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_alarm_info);
        initView();
        initOnClick();
    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.inspection_list_back_btn);
    }

    private void initOnClick() {
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.inspection_list_back_btn:
                finish();
                break;
        }
    }
}
