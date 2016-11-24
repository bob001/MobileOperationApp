package com.swdn.activity_xunjian;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.R;
import com.swdn.adapter_xunjian.TourInspectionAdapter;
import com.swdn.db_xunjian.TourInspectionDB;
import com.swdn.model_xunjian.TourInspectionDev;
import com.swdn.model_xunjian.TourInspectionTask;
import com.swdn.utils_xunjian.HttpCallbackListener;
import com.swdn.utils_xunjian.HttpUtil_Qiu;
import com.swdn.utils_xunjian.Utility_qiu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener{

    private static final String TAG = "com.qiu.listviewoftourinspection.MainActivity";

    private final List<TourInspectionTask> tourInspectionList =  new ArrayList<TourInspectionTask>();
    private final List<TourInspectionDev> tourInspectionDevList =  new ArrayList<TourInspectionDev>();

    private ListView listView;
    private ImageButton ibtn_showCalendarSelect;
    private TextView tv_showCalendarSelect;
    private ImageButton ibtn_showCategory;
    private TextView tv_showCategory;
//    private AlertDialog dialog;
    private TourInspectionDB tourInspectionDB;
    private TourInspectionAdapter tiAdapter;

    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private ImageButton searchButton;

    private SlideDateTimeListener listener = new SlideDateTimeListener() {
        @Override
        public void onDateTimeSet(Date date)
        {
            String dateStr = mFormatter.format(date);
            tv_showCalendarSelect.setText(dateStr);
            List<TourInspectionTask> tasks;
            if ("类型".equals(tv_showCategory.getText().toString())) {
                //从sqlite数据库中查询日期为dateStr的任务列表
                tasks = queryTasksByDate(dateStr);
                //更新当前的tourInspectionList
                tourInspectionList.clear();
                for (TourInspectionTask task : tasks) {
                    tourInspectionList.add(task);
                }
            }else {
                tasks = tourInspectionDB.queryTasksByCateTime(tv_showCategory.getText().toString(),dateStr);
                tourInspectionList.clear();
                for (TourInspectionTask task : tasks) {
                    tourInspectionList.add(task);
                }
            }
            //更新listview的显示
            tiAdapter.notifyDataSetChanged();
        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel()
        {
            Toast.makeText(MainActivity.this,
                    "Canceled", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 从数据库中查询指定日期下的巡检任务
     * @param date
     * @return
     */
    List<TourInspectionTask> queryTasksByDate(String date) {
        return tourInspectionDB.queryTasksByDate(date);
    }


//    String result = null;
//    Handler hand = new Handler() {
//        public void handleMessage(android.os.Message msg) {
//            super.handleMessage(msg);
//            if (msg.what == 404) {
//
//            } else if (msg.what == 200) {
//                result = (String) msg.obj;
////                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
//            }
//        };
//    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_xunjian_main);
        tourInspectionDB = TourInspectionDB.getInstance(this);

        ImageView backImage = (ImageView)findViewById(R.id.inspection_list_back_btn);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_OK);
                finish();
            }
        });

        ibtn_showCalendarSelect = (ImageButton) findViewById(R.id.ibtn_date);
        tv_showCalendarSelect = (TextView) findViewById(R.id.tv_Date);
        ibtn_showCategory = (ImageButton) findViewById(R.id.ibtn_category);
        tv_showCategory = (TextView) findViewById(R.id.tv_category);
        ibtn_showCalendarSelect.setOnClickListener(this);
        ibtn_showCategory.setOnClickListener(this);
        searchButton = (ImageButton) findViewById(R.id.ibtn_search);
        searchButton.setOnClickListener(this);

        listView = (ListView)findViewById(R.id.lv_info);
        tiAdapter = new TourInspectionAdapter(MainActivity.this, R.layout.taskplanitem_listviewitem, tourInspectionList);
        listView.setAdapter(tiAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TourInspectionTask tiTask = tourInspectionList.get(i);
                Intent intent = new Intent(MainActivity.this, DetaiInfoTourInspectionActivity.class);
                //在intent中绑定数据任务和其下的需要巡检的设备
                intent.putExtra("dataTask", tiTask);
                intent.putExtra("position",i);
//                Bundle bundle = new Bundle();
//                ArrayList<TourInspectionDev> tiDevs = queryTIDevsByTaskId(tiTask.getId());
//                bundle.putParcelableArrayList("devs",tiDevs);
//                intent.putExtra("dataDevs", bundle);
                startActivityForResult(intent, 1);
            }
        });
        getFullData();

        //袁文浩
//        String address = "http://wxeis.eis.swdnkj.com/hrkweb/rest/patrol/patrolmission?useroid=%E8%A2%81%E6%96%87%E6%B5%A9";
//        String address = "http://dsm.swdnkj.com:9080/EIS/rest/patrol/patrolmission?useroid=%E8%A2%81%E6%96%87%E6%B5%A9";
//        String address = "http://192.168.1.202:8080/sweisweb/rest/patrol/patrolmission?useroid=%E8%A2%81%E6%96%87%E6%B5%A9";

        //李平
//        String address = "http://dsm.swdnkj.com:9080/EIS/rest/patrol/patrolmission?useroid=%E6%9D%8E%E5%B9%B3";

        /**
         * 直接调用服务接口得到返回数据
         */
//        Utility_qiu.queryTasksFromServer(handler,address);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String jsonStr = (String)msg.obj;//获得json字符串
                Utility_qiu.parseJsonStr(tourInspectionList,tourInspectionDevList,jsonStr);
                tiAdapter.notifyDataSetChanged();//动态加载
            }
        }
    };

    private ArrayList<TourInspectionDev> queryTIDevsByTaskId(int taskId) {
        ArrayList<TourInspectionDev> tiDevs = new ArrayList<TourInspectionDev>();
        for (TourInspectionDev tourInspectionDev : tourInspectionDevList) {
            if (tourInspectionDev.getTaskId() == taskId) {
                tiDevs.add(tourInspectionDev);
            }
        }
        return tiDevs;
    }

    /**
     * 从本地数据库中查询任务信息
     */
    private void queryTasks() {
        List<TourInspectionTask> dataList = tourInspectionDB.loadTourInspectionTasks();
        if (dataList.size() > 0) {
            tourInspectionList.clear();
            for (TourInspectionTask tiTask : dataList) {
                tourInspectionList.add(tiTask);
            }
            tiAdapter.notifyDataSetChanged();//动态加载
        }
    }

    /**
     * 从服务端读取数据并存储到数据库中，并初始化UIlistview的列表数据
     */
    private void getData() {
//        String address = "http://wxeis.eis.swdnkj.com/hrkweb/rest/patrol/patrolmission?useroid=袁文浩";
//        String address = "http://wxeis.eis.swdnkj.com/hrkweb/rest/patrol/patrolmission?useroid=%E8%A2%81%E6%96%87%E6%B5%A9";
//        String address = "http://dsm.swdnkj.com:9080/EIS/rest/patrol/patrolmission?useroid=yuanwh";
//        String baseaddress = "http://dsm.swdnkj.com:9080/EIS/rest/patrol/patrolmission?useroid=";
        String baseaddress = "http://61.177.76.218:8090/boyuan/rest/patrol/patrolmission?useroid=";
        String user = getSharedPreferences("xunjian", MODE_PRIVATE).getString("usercode","");
        String address = baseaddress + user;
        HttpUtil_Qiu.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result = false;
                result = Utility_qiu.handleResponse(tourInspectionDB, response);
                if (result) {
                    //回到主线程处理逻辑
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            queryTasks();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    /**
     * 有网络的话去服务端得到最新数据并同步化到本地然后从本地加载数据
     * 没有网络的话直接从本地加载数据
     */
    private void getFullData() {
        //检测是否有网络
        if (Utility_qiu.getNetworkStatus(MainActivity.this)) {
            //将数据库表中的内容清空
            tourInspectionDB.clearTableContent("tourinspection_task");
            tourInspectionDB.clearTableContent("tourinspection_dev");
            //从web服务端读取数据并将数据写入本地的sqlite数据表中并更新tourInspectionList，动态加载listview
            getData();
        }else{
            Toast.makeText(this, "当前在无网络环境", Toast.LENGTH_SHORT).show();
            queryTasks();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //点击日期选择按钮
            case R.id.ibtn_date:
                //弹出日期选择
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
            //点击分类选择按钮
            case R.id.ibtn_category:
                //弹出分类选择
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("请选择查询类型");
                final String[] categories = {"特巡","故障","巡视"};
                builder.setItems(categories, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String category = categories[which];
                        tv_showCategory.setText(category);
                        List<TourInspectionTask> tasks;
                        if("时间".equals(tv_showCalendarSelect.getText().toString())){
                            //从数据库中查询出类型为category的数据
                            tasks = tourInspectionDB.queryTasksByCategory(category);
                            //重写加载listview中的数据
                            tourInspectionList.clear();
                            for (TourInspectionTask task : tasks) {
                                tourInspectionList.add(task);
                            }
                        }else {
                            //从数据库中查询出类型为category指定时间的数据
                            tasks = tourInspectionDB.queryTasksByCateTime(category,tv_showCalendarSelect.getText().toString());
                            tourInspectionList.clear();
                            for (TourInspectionTask task : tasks) {
                                tourInspectionList.add(task);
                            }
                        }
                        tiAdapter.notifyDataSetChanged();
                    }
                });
                builder.show();
                break;
            //点击搜索按钮
            case R.id.ibtn_search:
                //刷新tourInspectionList，重写加载新数据
                getFullData();
                tv_showCalendarSelect.setText("时间");
                tv_showCategory.setText("类型");
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    /*
                     * 检测该任务下的设备是否都已完成
                     * 如果都已经完成需要更新本地的task表的is_finished字段的和对应的lisnview下的值
                     */
                    int position = data.getIntExtra("position", -1);
                    int taskId = tourInspectionList.get(position).getId();
                    if (checkTaskComplish(taskId)) {
                        tourInspectionDB.updateIsComplished_TITask(taskId);
                        tourInspectionList.get(position).setIsFinished(1);
                        tiAdapter.notifyDataSetChanged();
                    }
                }
                break;
        }
    }

    //检查某个任务下的巡检设备任务都已提交
    private boolean checkTaskComplish(int id) {
        List<TourInspectionDev> tidevs = tourInspectionDB.loadTourInspectionDevs(id);
        for (TourInspectionDev tidev : tidevs) {
            if (tidev.getIsCommited() == 0) {
                return false;
            }
        }
        return true;
    }

}
