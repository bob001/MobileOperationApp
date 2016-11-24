package com.swdn.activity_xunjian;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.google.gson.Gson;
import com.R;
import com.swdn.db_xunjian.TourInspectionDB;
import com.swdn.model_xunjian.TourInspectionDev;
import com.swdn.utils_xunjian.HttpUtil_Qiu;
import com.swdn.utils_xunjian.Utility_qiu;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TourInspectionReplyActivity extends FragmentActivity implements View.OnClickListener{

    /**
     * 时间选择
     */
    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private SlideDateTimeListener listener = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date)
        {
            tourDate.setText(mFormatter.format(date));
//            Toast.makeText(TourInspectionReplyActivity.this,
//                    mFormatter.format(date), Toast.LENGTH_SHORT).show();
        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel()
        {
            Toast.makeText(TourInspectionReplyActivity.this,
                    "Canceled", Toast.LENGTH_SHORT).show();
        }
    };

    public static final String ACTION = "android.intent.action.com.qiu.listviewoftourinspection.TourInspectionReplyActivity";
    private TextView deviceId;
    private TextView deviceName;
    private TextView deviceType;
    private TextView installLocation;
    private TextView tourInspKey;
    private TextView remark;
    private TextView isCommited;
    private EditText tourDate;
    private EditText tourPerson;
    private EditText tourKey;
    private EditText tourEnd;
    private EditText tourRemarks;
    private ImageButton ib_selectTime;
    private ImageButton ib_selectPerson;
    private Button commitBtn;
    private Button saveBtn;
    private Button clearBtn;

    private int id;
    private TourInspectionDev tiDev;
    private int position;

    private TourInspectionDB tourInspectionDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_xunjian_tireply);
        tourInspectionDB = TourInspectionDB.getInstance(this);
        //接收上一个Activity传过来的数据
        tiDev = (TourInspectionDev)getIntent().getParcelableExtra("data");
        String tiExecPerson = getIntent().getStringExtra("tiPerson");
        position = getIntent().getIntExtra("position",-1);

        ImageView backImage = (ImageView)findViewById(R.id.inspection_list_back_btn);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        int isCommitedTest = tiDev.getIsCommited();

        id = tiDev.getId();
        deviceId = (TextView)findViewById(R.id.tv_deviceId);
        deviceName = (TextView)findViewById(R.id.tv_deviceName);
        deviceType = (TextView)findViewById(R.id.tv_deviceType);
        installLocation = (TextView)findViewById(R.id.tv_installLocation);
        tourInspKey = (TextView)findViewById(R.id.tv_tourInspKey);
        remark = (TextView)findViewById(R.id.tv_remark);
        isCommited = (TextView)findViewById(R.id.tv_commitState);
        deviceId.setText(tiDev.getDevId());
        deviceName.setText(tiDev.getDevName());
        deviceType.setText(tiDev.getDevType());
        installLocation.setText(tiDev.getLocation());
        tourInspKey.setText(tiDev.getPretourKey());
        remark.setText(tiDev.getRemarks());

        tourDate = (EditText) findViewById(R.id.et_tiDate);
        tourPerson = (EditText) findViewById(R.id.et_tiPerson);
        tourKey = (EditText) findViewById(R.id.et_tiKey);
        tourEnd = (EditText) findViewById(R.id.et_tiEnd);
        tourRemarks = (EditText) findViewById(R.id.et_tiRemark);

        if (tiDev.getIsCommited() == 0) {
            isCommited.setText("未提交");
        } else if (tiDev.getIsCommited() == 1) {
            isCommited.setText("已提交");
            tourDate.setText(tiDev.getTourDate());
            tourPerson.setText(tiDev.getTourDate());
            tourKey.setText(tiDev.getTourKey());
            tourEnd.setText(tiDev.getTourEnd());
            tourRemarks.setText(tiDev.getTourRemarks());
        }

        ib_selectTime = (ImageButton) findViewById(R.id.ib_selectTime);
        ib_selectTime.setOnClickListener(this);

        ib_selectPerson = (ImageButton) findViewById(R.id.ib_selectPerson);
        ib_selectPerson.setOnClickListener(this);

        //加载页面时预先填写的，运维人员也可修改！
        tourDate.setText(mFormatter.format(new Date()));
        tourPerson.setText(tiExecPerson);

        commitBtn = (Button) findViewById(R.id.btn_commit);
        saveBtn = (Button) findViewById(R.id.btn_saveOffline);
        clearBtn = (Button) findViewById(R.id.btn_clear);
        commitBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        clearBtn.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //将回填信息提交到服务端
            case R.id.btn_commit:
                //判断网络
                if (!Utility_qiu.getNetworkStatus(this)) {
                    Toast.makeText(this, "当前处于无网络状态，无法提交,可离线保存！", Toast.LENGTH_SHORT).show();
                    return;
                }
                String tk = tourKey.getText().toString();
                if (tk.equals("") || tk.equals("null")) {
                    Toast.makeText(this, "请输入巡检重点！",Toast.LENGTH_SHORT).show();
                    return;
                }
                String te = tourEnd.getText().toString();
                if (te.equals("") || te.equals("null")) {
                    Toast.makeText(this, "请输入巡检结果！",Toast.LENGTH_SHORT).show();
                    return;
                }
                String tr = tourRemarks.getText().toString();
                if (tr.equals("") || tr.equals("null")) {
                    Toast.makeText(this, "请输入巡检备注！",Toast.LENGTH_SHORT).show();
                    return;
                }
                commitBackfillInfo();
                break;
            //离线保存 -- 可保存到SharedPreferences文件中
            case R.id.btn_saveOffline:
                saveBackfillInfo();
                Toast.makeText(this, "离线保存成功！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_clear:
                flushEditText();
                break;
            //日期选择
            case R.id.ib_selectTime:
                new SlideDateTimePicker.Builder(getSupportFragmentManager())
                        .setListener(listener)
                        .setInitialDate(new Date())
                        //.setMinDate(minDate)
                        //.setMaxDate(maxDate)
                        //.setIs24HourTime(true)
                        //.setTheme(SlideDateTimePicker.HOLO_DARK)
                        //.setIndicatorColor(Color.parseColor("#990000"))
                        .build()
                        .show();
                break;
            //执行人选择
            case R.id.ib_selectPerson:
                AlertDialog.Builder builder = new AlertDialog.Builder(TourInspectionReplyActivity.this);
                builder.setTitle("请选择巡视人");
                final String[] persons = {"李平","袁文浩","邱海波","蒋超","..."};
                builder.setItems(persons, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String person = persons[which];
                        tourPerson.setText(person);
                    }
                });
                builder.show();
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
                    Toast.makeText(TourInspectionReplyActivity.this, msg, Toast.LENGTH_SHORT).show();
                    //提交成功！
                    if (code == 1) {
                        //更新本地数据库中的该巡检设备的回填字段和提交字段
                        tourInspectionDB.updateBackfill_TIDev(id,tourDate.getText().toString(),
                                tourPerson.getText().toString(),tourKey.getText().toString(),
                                tourEnd.getText().toString(),tourRemarks.getText().toString());
                        Intent intent = new Intent();
                        intent.putExtra("position",position);
                        intent.putExtra("id",id);
                        setResult(RESULT_OK, intent);
                        //返回上一页
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };
    /**
     * 将回填数据提交到服务端，并返回上一页；
     */
    private void commitBackfillInfo() {

//        String address = "http://192.168.1.46:8080/hrkweb/rest/test/setContent";
//        String address = "http://dsm.swdnkj.com:9080/EIS/rest/patrol/setContent";
        String address = "http://61.177.76.218:8090/boyuan/rest/patrol/setContent";
//        String params = "id=" + id +"&ti_execDate=" + tourDate.getText().toString()
//                + "&ti_person=" + tourPerson.getText().toString() + "&ti_key=" + tourKey.getText().toString()
//                + "&ti_result=" + tourEnd.getText().toString() +"&ti_remark=" + tourRemarks.getText().toString();

//        TourInspectionDev tiDataBackfill = new TourInspectionDev();
//        tiDataBackfill.setId(tiDev.getId());
//        tiDataBackfill.setTourDate(tourDate.getText().toString());
//        tiDataBackfill.setTourPerson(tourPerson.getText().toString());
//        tiDataBackfill.setTourKey(tourKey.getText().toString());
//        tiDataBackfill.setTourEnd(tourEnd.getText().toString());
//        tiDataBackfill.setTourRemarks(tourRemarks.getText().toString());

        Map<String, String> map = new HashMap<String, String>();
        map.put("id",String.valueOf(tiDev.getId()));
        map.put("taskId",String.valueOf(0));
        map.put("tourDate",tourDate.getText().toString());
        map.put("tourPerson",tourPerson.getText().toString());
        map.put("tourKey",tourKey.getText().toString());
        map.put("tourEnd",tourEnd.getText().toString());
        map.put("tourRemarks",tourRemarks.getText().toString());

        Gson gson = new Gson();
//        String jsonStr = gson.toJson(tiDataBackfill);
        String jsonStr = gson.toJson(map);
        String params = "jsonparam=" + jsonStr;
        try {
            HttpUtil_Qiu.sendPostRequestByForm(handler,address,params);

        } catch (Exception e) {
            Toast.makeText(this, "未提交成功",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * 将回填信息保存到本地的SharedPreferences文件中,里面加个是否提交到服务端的key--isCommit
     * 需要后台服务检测是否有网络，一旦有网就将其中isCommited为false的数据提交并修改isCommited为true
     */
    private void saveBackfillInfo() {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putBoolean("isCommited", false);
        editor.putInt("id", id);
        editor.putString("tour_date", tourDate.getText().toString());
        editor.putString("tour_person", tourPerson.getText().toString());
        editor.putString("tour_key", tourKey.getText().toString());
        editor.putString("tour_end", tourEnd.getText().toString());
        editor.putString("tour_remarks", tourRemarks.getText().toString());
        editor.commit();
    }

    private void flushEditText() {
        tourDate.setText("");
        tourPerson.setText("");
        tourKey.setText("");
        tourEnd.setText("");
        tourRemarks.setText("");
    }
}
