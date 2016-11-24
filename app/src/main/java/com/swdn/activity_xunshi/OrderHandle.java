package com.swdn.activity_xunshi;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.R;
import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.google.gson.Gson;
import com.swdn.db_qiangxiu.QXDB;
import com.swdn.db_xunshi.XSDB;
import com.swdn.model_qiangxiu.RepairInfo;
import com.swdn.model_xunshi.TourInfo;
import com.swdn.utils.HttpUtil;
import com.swdn.utils.Utility;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OrderHandle extends FragmentActivity implements View.OnClickListener{

    private ImageView ivBack;
    private TextView tv_tourNum;
    private TextView tv_pretourDate;
    private TextView tv_tourCategory;
    private TextView tv_pretourPerson;
    private TextView tv_bdsId;
    private TextView tv_bdsName;
    private TextView tv_receiveDate;

    private ImageView iv_weatherImage;
    private TextView tv_weatherSituation;
    private TextView tv_temperatureSituation;

    private EditText et_tourStartTime;
    private EditText et_tourEndTime;
    private EditText et_tourPerson;
    private EditText et_tourSituation;
    private EditText et_remarks;


    private RelativeLayout rl_orderHandle;
    private TextView tv_orderHandle;

    //接收上一Activity传来的数据的实例
    private TourInfo ti;
    private int position;

    private XSDB xsdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_xunshi_order_handle);
        xsdb = XSDB.getInstance(this);
        //获取上一个Activity传来的数据
        Intent i = getIntent();
        ti = i.getParcelableExtra("data");
        position = i.getIntExtra("position",-1);
        initView();
        initData();
        initOnclick();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        ivBack = (ImageView)findViewById(R.id.inspection_list_back_btn);
        tv_tourNum = (TextView) findViewById(R.id.tv_tour_number);
        tv_pretourDate = (TextView) findViewById(R.id.tv_pretour_date);
        tv_tourCategory = (TextView) findViewById(R.id.tv_tour_category);
        tv_pretourPerson = (TextView) findViewById(R.id.tv_pretour_person);
        tv_bdsId = (TextView) findViewById(R.id.tv_bds_id);
        tv_bdsName = (TextView) findViewById(R.id.tv_bds_name);
        tv_receiveDate = (TextView) findViewById(R.id.tv_tour_receive_date);
        iv_weatherImage = (ImageView) findViewById(R.id.iv_weather_image);
        tv_weatherSituation = (TextView) findViewById(R.id.tv_weather_situation);
        tv_temperatureSituation = (TextView) findViewById(R.id.tv_temperature_situation);
        et_tourStartTime = (EditText) findViewById(R.id.et_tourStartTime);
        et_tourEndTime = (EditText) findViewById(R.id.et_tourEndTime);
        et_tourPerson = (EditText) findViewById(R.id.et_tourPerson);
        et_tourSituation = (EditText) findViewById(R.id.et_tourSituation);
        et_remarks = (EditText) findViewById(R.id.et_remarks);
        rl_orderHandle = (RelativeLayout) findViewById(R.id.rl_process);
        tv_orderHandle = (TextView) findViewById(R.id.tv_tour_orderHandle);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        tv_tourNum.setText(ti.getTourNum());
        tv_pretourDate.setText(ti.getPreTourDate());
        tv_tourCategory.setText(ti.getTourCategory());
        tv_pretourPerson.setText(ti.getPreTourPerson());
        tv_bdsId.setText(String.valueOf(ti.getBdsId()));
        tv_bdsName.setText(ti.getBds());
        tv_receiveDate.setText(ti.getReceiveDate());
        tv_weatherSituation.setText(ti.getWeather());
        tv_temperatureSituation.setText(ti.getTemperature());
        if (ti.getIsFinished() == 1) {//任务已完成
            //将曾经提交的信息内容写到edittext中
            et_tourStartTime.setText(ti.getTourStartTime());
            et_tourEndTime.setText(ti.getTourEndTime());
            et_tourPerson.setText(ti.getTourPerson());
            et_tourSituation.setText(ti.getTourSituation());
            et_remarks.setText(ti.getRemarks());
            tv_orderHandle.setTextColor(Color.rgb(0x93, 0x95, 0x9f));
        }
    }

    /**
     * 初始化点击事件
     */
    private void initOnclick() {
        et_tourStartTime.setOnClickListener(this);
        et_tourEndTime.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        rl_orderHandle.setOnClickListener(this);
    }

    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击左上角的返回
            case R.id.inspection_list_back_btn:
                Intent intent = new Intent();
                intent.putExtra("data",ti);
                intent.putExtra("position", position);
                setResult(RESULT_OK,intent);
                finish();
                break;
            //点击选择开始时间
            case R.id.et_tourStartTime:
                new SlideDateTimePicker.Builder(getSupportFragmentManager())
                        .setListener(new SlideDateTimeListener() {
                            @Override
                            public void onDateTimeSet(Date date) {
                                String dateStr = mFormatter.format(date);
                                et_tourStartTime.setText(dateStr);
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
            case R.id.et_tourEndTime:
                new SlideDateTimePicker.Builder(getSupportFragmentManager())
                        .setListener(new SlideDateTimeListener() {
                            @Override
                            public void onDateTimeSet(Date date) {
                                String dateStr = mFormatter.format(date);
                                et_tourEndTime.setText(dateStr);
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
            //点击处理完成
            case R.id.rl_process:
                //将数据提交到服务端，并更新本地数据库
                //判断网络
                if (!Utility.getNetworkStatus(this)) {
                    Toast.makeText(this, "当前处于无网络状态，无法提交！", Toast.LENGTH_SHORT).show();
                    return;
                }
                String tst = et_tourStartTime.getText().toString();
                if (tst.equals("") || tst.equals("null")) {
                    Toast.makeText(this, "请输入到场时间！",Toast.LENGTH_SHORT).show();
                    return;
                }
                String tet = et_tourEndTime.getText().toString();
                if (tet.equals("") || tet.equals("null")) {
                    Toast.makeText(this, "请输入结束时间！",Toast.LENGTH_SHORT).show();
                    return;
                }
                String ts = et_tourSituation.getText().toString();
                if (ts.equals("") || ts.equals("null")) {
                    Toast.makeText(this, "请输入巡视情况！",Toast.LENGTH_SHORT).show();
                    return;
                }
                //提交数据并返回
                commitInfo();
                break;
            default:
                break;
        }
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
                        ti.setIsFinished(1);
                        ti.setTourStartTime(et_tourStartTime.getText().toString());
                        ti.setTourEndTime(et_tourEndTime.getText().toString());
                        ti.setTourPerson(et_tourPerson.getText().toString());
                        ti.setTourSituation(et_tourSituation.getText().toString());
                        ti.setRemarks(et_remarks.getText().toString());
                        //更新本地数据库中的该抢修单的回填字段和处理状态字段
                        xsdb.updateFillbackInfo(ti);
                        //返回上一Activity
                        Intent i = new Intent();
                        i.putExtra("data", ti);
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
//        String address = "http://wxeis.eis.swdnkj.com/hrkweb/rest/patrol/setbdxscontent";
//        String address = "http://dsm.swdnkj.com:9080/EIS/rest/patrol/setbdxscontent";
        String address = "http://61.177.76.218:8090/boyuan/rest/patrol/setbdxscontent";
        Map<String, String> map = new HashMap<String, String>();
        map.put("ID",String.valueOf(ti.getId()));
        String arrivalTime = et_tourStartTime.getText().toString();
        String oArrivalTime = Utility.convertDateString(arrivalTime);
        map.put("XSKSSJ",oArrivalTime);
        String endTime = et_tourEndTime.getText().toString();
        String oEndTime = Utility.convertDateString(endTime);
        map.put("XSJSSJ",oEndTime);
        map.put("XSR",et_tourPerson.getText().toString());
        map.put("XSQK",et_tourSituation.getText().toString());
        map.put("BZ",et_remarks.getText().toString());

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
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("data",ti);
        intent.putExtra("position", position);
        setResult(RESULT_OK,intent);
        finish();
    }

}
