package com.swdn.activity_qiangxiu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amapsw.MapMainActivity;
import com.R;
import com.swdn.db_qiangxiu.QXDB;
import com.swdn.model_qiangxiu.*;
import com.swdn.utils.HttpUtil;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 订单接单Activity
 */
public class OrderReceive extends Activity implements View.OnClickListener{

    private ImageView ivBack;
    private TextView tvTaskCode;
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

    private RelativeLayout rlReceiveOrder;
    private TextView tvReceiveOrder;

    //接收上一Activity传来的数据的实例
    private RepairInfo ri;
    private int position;

    private QXDB qxdb;
    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_qiangxiu_order_receive);
        qxdb = QXDB.getInstance(this);
        //获取上一页面传来的数据
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
        tvTaskCode = (TextView) findViewById(R.id.tv_taskId);
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

        rlReceiveOrder = (RelativeLayout) findViewById(R.id.rl_orderReceive);
        tvReceiveOrder = (TextView) findViewById(R.id.tv_repair_orderReceive);
    }

    /**
     * 初始化数据
     */
    private void initData() {
//        tvTaskCode.setText(ri.getTaskCode());
        tvTaskCode.setText(String.valueOf(ri.getId()));
        tvOrganization.setText(ri.getAddress());
        tvManager.setText(ri.getManager());
        if ("null".equals(ri.getGroupInfo())) {
            tvGroup.setText("");
        }else {
            tvGroup.setText(ri.getGroupInfo());
        }
        tvCategory.setText(ri.getCategory());
        tvAddress.setText(ri.getAddress());
        if (ri.getProcessState() == 0) {
            tvProcessState.setText("未处理");
        } else if (ri.getProcessState() == 1) {
            tvProcessState.setText("已处理");
        }
        if (ri.getReceiveState() == 0) {
            tvReceiveOrder.setText("我要接单");
        } else if (ri.getReceiveState() == 1) {
            tvReceiveOrder.setText("已接单");
            tvReceiveOrder.setTextColor(Color.rgb(0x93, 0x95, 0x9f));
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

        rlReceiveOrder.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击左上角的返回
            case R.id.inspection_list_back_btn:
                Intent intent = new Intent();
                intent.putExtra("position", position);
                intent.putExtra("data",ri);
                setResult(RESULT_OK,intent);
                finish();
                break;
            //点击告警详情
            case R.id.tv_alarmInfo:
                Intent intentAlarm = new Intent(this,AlarmInfo.class);
                intentAlarm.putExtra("alarmId",ri.getAlarmId());
                intentAlarm.putExtra("precessState",ri.getProcessState());
                startActivity(intentAlarm);
                break;
            //点击预案
            case R.id.tv_preplanning:
                Intent intentPreplanning = new Intent(this,PreplanningInfo.class);
                startActivity(intentPreplanning);
                break;
            //点击工具提醒
            case R.id.tv_toolRemind:
                Intent intentToolsRemind = new Intent(this,ToolsRemind.class);
                startActivity(intentToolsRemind);
                break;
            //点击到这里去
            case R.id.tv_comehere:
                Intent i = new Intent(this, MapMainActivity.class);
                i.putExtra("lon",ri.getLon());
                i.putExtra("lat",ri.getLat());
                startActivity(i);
                Toast.makeText(this, "到这里去", Toast.LENGTH_SHORT).show();
                break;
            //点击我要接单跳转到相应的处理Activity
            case R.id.rl_orderReceive:
                if (ri.getReceiveState() == 0) {
                    //提交到服务端，成功后跳转到订单处理Activity
                    commitOrder();
                }
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
                    Toast.makeText(OrderReceive.this, msg, Toast.LENGTH_SHORT).show();
                    //提交成功！
                    if (code == 1) {
                        //更新ri实例
                        ri.setReceiveState(1);
                        ri.setReceiveDate(mFormatter.format(new Date()));
                        //更新本地数据库中的该抢修任务的接单状态和接单时间
                        qxdb.updateReceiveStateAndDate(ri.getId(),1,ri.getReceiveDate());
                        //跳转到订单处理Activity
                        Intent i = new Intent(OrderReceive.this,OrderHandle.class);
                        i.putExtra("data", ri);
                        i.putExtra("position", position);
                        startActivityForResult(i,1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };
    /**
     * 告知服务端已接收订单，成功后跳转到订单处理Activity
     */
    private void commitOrder() {
//        String address = "http://wxeis.eis.swdnkj.com/hrkweb/rest/patrol/updateJD";
//        String address = "http://dsm.swdnkj.com:9080/EIS/rest/patrol/updateJD";
        String address = "http://61.177.76.218:8090/boyuan/rest/patrol/updateJD";
        String params = "ID=" + ri.getId();
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
        intent.putExtra("position", position);
        intent.putExtra("data",ri);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {//处理Activity提交成功后返回
                    //更新ri
                    ri = data.getParcelableExtra("data");
                    position = data.getIntExtra("position",-1);
                    if (ri.getProcessState() == 1) {
                        tvProcessState.setText("已处理");
                    }
                    if (ri.getReceiveState() == 1) {
                        tvReceiveOrder.setText("已接单");
                        tvReceiveOrder.setTextColor(Color.rgb(0x93, 0x95, 0x9f));
                    }
                }
                break;
            default:
                break;
        }
    }
}
