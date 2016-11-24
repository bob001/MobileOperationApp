package com.swdn.activity_qiangxiu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amapsw.MapMainActivity;
import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.google.gson.Gson;
import com.swdn.R;
import com.swdn.activity_xunjian.TourInspectionReplyActivity;
import com.swdn.db_qiangxiu.QXDB;
import com.swdn.model_qiangxiu.RepairInfo;
import com.swdn.model_xunjian.TourInspectionTask;
import com.swdn.utils.HttpUtil;
import com.swdn.utils.Utility;
import com.swdn.utils_xunjian.HttpUtil_Qiu;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单处理Activity
 */
public class OrderHandle extends FragmentActivity implements View.OnClickListener{

    private ImageView ivBack;
    private TextView tvOrganization;
    private TextView tvManager;
    private TextView tvGroup;
    private TextView tvCategory;
    private TextView tvAddress;
    private TextView tvProcessState;

    private TextView tvAlarmInfo;
    private TextView tvPreplanning;
    private TextView tvToolRemind;
    private TextView tvComeHere;

    private EditText etArrivalTime;
    private EditText etEndTime;
    private EditText etRepairPersons;
    private EditText etFailureCause;
    private EditText etExecSituation;
    private EditText etRemarks;

    private RelativeLayout rlProcess;
    private TextView tvProcess;

    //接收上一Activity传来的数据的实例
    private RepairInfo ri;
    private int position;

    private QXDB qxdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order_handle);
        qxdb = QXDB.getInstance(this);
        //获取上一个Activity传来的数据
        Intent i = getIntent();
        ri = i.getParcelableExtra("data");
        position = i.getIntExtra("position",-1);
        initView();
        initData();
        initOnClick();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        ivBack = (ImageView)findViewById(R.id.inspection_list_back_btn);
        tvOrganization = (TextView) findViewById(R.id.tv_organization);
        tvManager = (TextView) findViewById(R.id.tv_manager);
        tvGroup = (TextView) findViewById(R.id.tv_group);
        tvCategory = (TextView) findViewById(R.id.tv_category);
        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvProcessState = (TextView) findViewById(R.id.tv_process_state);

        tvAlarmInfo = (TextView) findViewById(R.id.tv_alarmInfo);
        tvPreplanning = (TextView) findViewById(R.id.tv_preplanning);
        tvToolRemind = (TextView) findViewById(R.id.tv_toolRemind);
        tvComeHere = (TextView) findViewById(R.id.tv_comehere);

        etArrivalTime = (EditText)findViewById(R.id.et_arrivalTime);
        etEndTime = (EditText)findViewById(R.id.et_endTime);
        etRepairPersons = (EditText)findViewById(R.id.et_repairPersons);
        etFailureCause = (EditText)findViewById(R.id.et_failureCause);
        etExecSituation = (EditText)findViewById(R.id.et_execSituation);
        etRemarks = (EditText)findViewById(R.id.et_remarks);

        rlProcess = (RelativeLayout) findViewById(R.id.rl_process);
        tvProcess = (TextView)findViewById(R.id.tv_orderReceive);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        tvOrganization.setText(ri.getAddress());
        tvManager.setText(ri.getManager());
        tvGroup.setText(ri.getGroupInfo());
        tvCategory.setText(ri.getCategory());
        tvAddress.setText(ri.getAddress());
        if (ri.getProcessState() == 0) {
            tvProcessState.setText("未处理");
        } else if (ri.getProcessState() == 1) {
            tvProcessState.setText("已处理");
            //将曾经提交的信息内容写到edittext中
            etArrivalTime.setText(ri.getArrivalTime());
            etEndTime.setText(ri.getEndTime());
            etRepairPersons.setText(ri.getRepairPersons());
            etFailureCause.setText(ri.getFailureCause());
            etExecSituation.setText(ri.getExecuteSituation());
            etRemarks.setText(ri.getRemarks());
            tvProcess.setTextColor(Color.rgb(0x93, 0x95, 0x9f));
        }
    }

    /**
     * 初始化点击事件
     */
    private void initOnClick() {
        ivBack.setOnClickListener(this);
        tvAlarmInfo.setOnClickListener(this);
        tvPreplanning.setOnClickListener(this);
        tvToolRemind.setOnClickListener(this);
        tvComeHere.setOnClickListener(this);

        etArrivalTime.setOnClickListener(this);
        etEndTime.setOnClickListener(this);

        rlProcess.setOnClickListener(this);
    }

    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击左上角的返回
            case R.id.inspection_list_back_btn:
                Intent intent = new Intent();
                intent.putExtra("data",ri);
                intent.putExtra("position", position);
                setResult(RESULT_OK,intent);
                finish();
                break;
            //点击告警详情
            case R.id.tv_alarmInfo:
//                Toast.makeText(this, "告警详情", Toast.LENGTH_SHORT).show();
                Intent intentAlarm = new Intent(this,AlarmInfo.class);
                startActivity(intentAlarm);
                break;
            //点击预案
            case R.id.tv_preplanning:
//                Toast.makeText(this, "预案", Toast.LENGTH_SHORT).show();
                Intent intentPreplanning = new Intent(this,PreplanningInfo.class);
                startActivity(intentPreplanning);
                break;
            //点击工具提醒
            case R.id.tv_toolRemind:
//                Toast.makeText(this, "工具提醒", Toast.LENGTH_SHORT).show();
                Intent intentToolsRemind = new Intent(this,ToolsRemind.class);
                startActivity(intentToolsRemind);
                break;
            //点击到这里去
            case R.id.tv_comehere:
                Intent i = new Intent(this, MapMainActivity.class);
                startActivity(i);
//                i.putExtra("");
//                i.putExtra("");
                Toast.makeText(this, "到这里去", Toast.LENGTH_SHORT).show();
                break;
            //点击选择到场时间
            case R.id.et_arrivalTime:
                new SlideDateTimePicker.Builder(getSupportFragmentManager())
                        .setListener(new SlideDateTimeListener() {
                            @Override
                            public void onDateTimeSet(Date date) {
                                String dateStr = mFormatter.format(date);
                                etArrivalTime.setText(dateStr);
                            }

                            // Optional cancel listener
                            @Override
                            public void onDateTimeCancel() {
                                Toast.makeText(OrderHandle.this,
                                        "Canceled", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setInitialDate(new Date())
                        //.setMinDate(minDate)
                        //.setMaxDate(maxDate)
                        //.setIs24HourTime(true)
                        //.setTheme(SlideDateTimePicker.HOLO_DARK)
                        //.setIndicatorColor(Color.parseColor("#990000"))
                        .build()
                        .show();
                break;
            //点击选择结束时间
            case R.id.et_endTime:
                new SlideDateTimePicker.Builder(getSupportFragmentManager())
                        .setListener(new SlideDateTimeListener() {
                            @Override
                            public void onDateTimeSet(Date date) {
                                String dateStr = mFormatter.format(date);
                                etEndTime.setText(dateStr);
                            }

                            // Optional cancel listener
                            @Override
                            public void onDateTimeCancel() {
                                Toast.makeText(OrderHandle.this,
                                        "Canceled", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setInitialDate(new Date())
                        //.setMinDate(minDate)
                        //.setMaxDate(maxDate)
                        //.setIs24HourTime(true)
                        //.setTheme(SlideDateTimePicker.HOLO_DARK)
                        //.setIndicatorColor(Color.parseColor("#990000"))
                        .build()
                        .show();
                break;
            //点击处理完成进行相应的数据提交和处理
            case R.id.rl_process:
                //将数据提交到服务端，并更新本地数据库
                //判断网络
                if (!Utility.getNetworkStatus(this)) {
                    Toast.makeText(this, "当前处于无网络状态，无法提交！", Toast.LENGTH_SHORT).show();
                    return;
                }
                String at = etArrivalTime.getText().toString();
                if (at.equals("") || at.equals("null")) {
                    Toast.makeText(this, "请输入到场时间！",Toast.LENGTH_SHORT).show();
                    return;
                }
                String et = etEndTime.getText().toString();
                if (et.equals("") || et.equals("null")) {
                    Toast.makeText(this, "请输入结束时间！",Toast.LENGTH_SHORT).show();
                    return;
                }
                String rp = etRepairPersons.getText().toString();
                if (rp.equals("") || rp.equals("null")) {
                    Toast.makeText(this, "请输入抢修人员！",Toast.LENGTH_SHORT).show();
                    return;
                }
                String fc = etFailureCause.getText().toString();
                if (fc.equals("") || fc.equals("null")) {
                    Toast.makeText(this, "请输入故障原因！",Toast.LENGTH_SHORT).show();
                    return;
                }
                String es = etExecSituation.getText().toString();
                if (es.equals("") || es.equals("null")) {
                    Toast.makeText(this, "请输入执行情况！",Toast.LENGTH_SHORT).show();
                    return;
                }
                //提交数据并返回
                commitInfo();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("data",ri);
        intent.putExtra("position", position);
        setResult(RESULT_OK,intent);
        finish();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message message) {
            if (message.what == 1) {
                String str = (String)message.obj;
                JSONObject data = null;
                try {
                    data = new JSONObject(str);
                    int code = data.getInt("code");
                    String msg = data.getString("msg");
                    Toast.makeText(OrderHandle.this, msg, Toast.LENGTH_SHORT).show();
                    //提交成功！
                    if (code == 1) {
                        //更新ri实例
                        ri.setProcessState(1);
                        ri.setArrivalTime(etArrivalTime.getText().toString());
                        ri.setEndTime(etEndTime.getText().toString());
                        ri.setRepairPersons(etRepairPersons.getText().toString());
                        ri.setFailureCause(etFailureCause.getText().toString());
                        ri.setExecuteSituation(etExecSituation.getText().toString());
                        ri.setRemarks(etRemarks.getText().toString());
                        //更新本地数据库中的该抢修单的回填字段和处理状态字段
                        qxdb.updateFillbackInfo(ri);
                        //返回上一Activity
                        Intent i = new Intent();
                        i.putExtra("data", ri);
                        i.putExtra("position", position);
                        setResult(RESULT_OK, i);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    /**
     * 将回填数据提交到服务端，并返回
     */
    private void commitInfo() {
        String address = "http://wxeis.eis.swdnkj.com/hrkweb/rest/patrol/setqxcontent";
        Map<String, String> map = new HashMap<String, String>();
        map.put("ID",String.valueOf(ri.getId()));
        String arrivalTime = etArrivalTime.getText().toString();
        String oArrivalTime = Utility.convertDateString(arrivalTime);
        map.put("DCSJ",oArrivalTime);
        String endTime = etEndTime.getText().toString();
        String oEndTime = Utility.convertDateString(endTime);
        map.put("JSSJ",oEndTime);
        map.put("QXRY",etRepairPersons.getText().toString());
        map.put("GZNR",etFailureCause.getText().toString());
        map.put("QXGC",etExecSituation.getText().toString());
        map.put("BZNR",etRemarks.getText().toString());

        Gson gson = new Gson();
        String jsonStr = gson.toJson(map);
        String params = "jsonparam=" + jsonStr;
        try {
            HttpUtil.sendHttpPostRequest(handler,address,params);
        } catch (Exception e) {
            Toast.makeText(this, "未提交成功",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
