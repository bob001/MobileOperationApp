package com.swdn.activity_qiangxiu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.R;
import com.swdn.utils.HttpCallbackListener;
import com.swdn.utils.HttpUtil;
import com.swdn.utils.Utility;

public class AlarmInfo extends Activity implements View.OnClickListener{

    private ImageView ivBack;
    private TextView tvAlarmDevLoc;
    private TextView tvAlarmContent;
    private TextView tvAlarmType;
    private TextView tvAlarmOccurtime;
    private TextView tvAlarmLevel;
    private TextView tvProcessFlag;


    //从上一Activity中获取的告警id和处理状态
    private int alarmId;
    private int processState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_qiangxiu_alarm_info);
        //获取上一Activity传来的数据
        Intent i = getIntent();
        alarmId = i.getIntExtra("alarmId",0);
        processState = i.getIntExtra("precessState", -1);
        initView();
        initData();
        initOnClick();
    }

    private void initView() {
        ivBack = (ImageView) findViewById(R.id.inspection_list_back_btn);
        tvAlarmDevLoc = (TextView) findViewById(R.id.alarm_device_location);
        tvAlarmContent = (TextView) findViewById(R.id.alarm_content);
        tvAlarmType = (TextView) findViewById(R.id.alarm_type);
        tvAlarmOccurtime = (TextView) findViewById(R.id.alarm_occurtime);
        tvAlarmLevel = (TextView) findViewById(R.id.alarm_level);
        tvProcessFlag = (TextView) findViewById(R.id.process_flag);
    }

    /**
     * if（有网络）：请求数据并解析，并将解析到的数据放到指定控件位置
     * if（无网络）：提示无法加载数据
     */
    private void initData() {
        //检测是否有网络
        if (Utility.getNetworkStatus(this)) {
            //向服务器请求数据并解析,初始化控件
            getData_initView();
        }else{
            Toast.makeText(this, "当前在无网络环境", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 从服务端读取数据，并初始化控件数据
     */
    private void getData_initView() {
//        String baseaddress = "http://wxeis.eis.swdnkj.com/hrkweb/rest/Alarm/getAlarmById?alarm_id=";
//        String baseaddress = "http://dsm.swdnkj.com:9080/EIS/rest/Alarm/getAlarmById?alarm_id=";
        String baseaddress = "http://61.177.76.218:8090/boyuan/rest/Alarm/getAlarmById?alarm_id=";
//        final int alarmid  = alarmId;
        final int alarmid  = 33;
        String address = baseaddress + alarmid;
        HttpUtil.sendHttpGetRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                final com.swdn.model_qiangxiu.AlarmInfo result = Utility.handleAlarmInfoResponse(response);
                if (result != null) {
                    //在UI主线程中初始化控件
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initViewData(result);
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 初始化视图控件数据
     */
    private void initViewData(com.swdn.model_qiangxiu.AlarmInfo result) {
        tvAlarmDevLoc.setText(result.getAlarmDevLoc());
        tvAlarmContent.setText(result.getAlarmContent());
        tvAlarmType.setText(result.getAlarmType());
        tvAlarmOccurtime.setText(result.getAlarmOccurtime());
        tvAlarmLevel.setText(result.getAlarmLevel());
        if (processState == 1) {//已处理
            tvProcessFlag.setText("已处理");
        } else if (processState == 0) {//未处理
            tvProcessFlag.setText("未处理");
        }
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
