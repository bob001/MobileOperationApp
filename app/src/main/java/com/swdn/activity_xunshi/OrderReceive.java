package com.swdn.activity_xunshi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.R;
import com.swdn.db_xunshi.XSDB;
import com.swdn.model_xunshi.TourInfo;
import com.swdn.utils.HttpUtil;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderReceive extends Activity implements View.OnClickListener{

    private ImageView ivBack;
    private TextView tv_tourNum;
    private TextView tv_pretourDate;
    private TextView tv_tourCategory;
    private TextView tv_pretourPerson;
    private TextView tv_bdsId;
    private TextView tv_bdsName;
    private TextView tv_receiveDate;
    private TableRow tr_receiveDate;

    private ImageView iv_weatherImage;
    private TextView tv_weatherSituation;
    private TextView tv_temperatureSituation;

    private RelativeLayout rl_orderReceive;
    private TextView tv_orderReceive;

    //接收上一Activity传来的数据的实例
    private TourInfo ti;
    private int position;

    private XSDB xsdb;
    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_xunshi_order_receive);
        xsdb = XSDB.getInstance(this);
        //获取上一页面传来的数据
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
        tr_receiveDate = (TableRow) findViewById(R.id.tr_tour_receive_time);
        iv_weatherImage = (ImageView) findViewById(R.id.iv_weather_image);
        tv_weatherSituation = (TextView) findViewById(R.id.tv_weather_situation);
        tv_temperatureSituation = (TextView) findViewById(R.id.tv_temperature_situation);
        rl_orderReceive = (RelativeLayout) findViewById(R.id.rl_orderReceive);
        tv_orderReceive = (TextView) findViewById(R.id.tv_tour_orderReceive);
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
        setReceiveDate();
        tv_weatherSituation.setText(ti.getWeather());
        tv_temperatureSituation.setText(ti.getTemperature());
    }

    /**
     * 设置接收时间VIEW
     */
    private void setReceiveDate() {
        if (ti.getIsReceived() == 0) {
            tr_receiveDate.setVisibility(View.GONE);
        }else if(ti.getIsReceived() == 1){
            tr_receiveDate.setVisibility(View.VISIBLE);
            tv_receiveDate.setText(ti.getReceiveDate());
        }
    }

    private void initOnclick() {
        rl_orderReceive.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }


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
            //点击我要接单
            case R.id.rl_orderReceive:
                if (ti.getIsReceived() == 0) {
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
                        //更新ti实例
                        ti.setIsReceived(1);
                        ti.setReceiveDate(mFormatter.format(new Date()));
                        //更新本地数据库中的该抢修任务的接单状态和接单时间
                        xsdb.updateReceiveStateAndDate(ti.getId(),1,ti.getReceiveDate());
                        //跳转到订单处理Activity
                        Intent i = new Intent(OrderReceive.this, OrderHandle.class);
                        i.putExtra("data", ti);
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
//        String address = "http://wxeis.eis.swdnkj.com/hrkweb/rest/patrol/updatebdxsJD";
//        String address = "http://dsm.swdnkj.com:9080/EIS/rest/patrol/updatebdxsJD";
        String address = "http://61.177.76.218:8090/boyuan/rest/patrol/updatebdxsJD";
        String params = "ID=" + ti.getId();
        try {
            HttpUtil.sendHttpPostRequest(handler,address,params);
        } catch (Exception e) {
            Toast.makeText(this, "未提交成功",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {//处理Activity提交成功后返回
                    //更新ti
                    ti = data.getParcelableExtra("data");
                    position = data.getIntExtra("position", -1);
                    setReceiveDate();
                    if (ti.getIsReceived() == 1) {
                        tv_orderReceive.setText("已接单");
                        tv_orderReceive.setTextColor(Color.rgb(0x93, 0x95, 0x9f));
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("position", position);
        intent.putExtra("data",ti);
        setResult(RESULT_OK,intent);
        finish();
    }

}
