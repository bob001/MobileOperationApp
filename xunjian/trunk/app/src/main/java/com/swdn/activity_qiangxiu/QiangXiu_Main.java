package com.swdn.activity_qiangxiu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
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
import com.swdn.R;
import com.swdn.adapter_qiangxiu.QiangXiuListAdapter;
import com.swdn.db_qiangxiu.QXDB;
import com.swdn.model_qiangxiu.AlarmInfo;
import com.swdn.model_qiangxiu.RepairInfo;
import com.swdn.model_xunjian.TourInspectionTask;
import com.swdn.utils.HttpCallbackListener;
import com.swdn.utils.HttpUtil;
import com.swdn.utils.Utility;
import com.swdn.xunjian.HomeActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QiangXiu_Main extends FragmentActivity implements View.OnClickListener {

    private ImageView ivBack;
    private ImageButton ibDate;
    private TextView tvDate;
    private ImageButton ibCategory;
    private TextView tvCategory;
    private ImageButton ibSearch;
    private ListView lvInfo;

    private QiangXiuListAdapter qxlistAdapter;
    private List<RepairInfo> repairInfoList = new ArrayList<RepairInfo>();

    private QXDB qxdb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_qiangxiu_main);
        qxdb = QXDB.getInstance(this);
        initView();
        initOnClick();

        //使用本地的测试数据运行
//        initList();

        qxlistAdapter = new QiangXiuListAdapter(this, R.layout.repair_list_listviewitem, repairInfoList);
        lvInfo.setAdapter(qxlistAdapter);
        lvInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RepairInfo ri = repairInfoList.get(position);
                if (ri.getReceiveState() == 0 && ri.getProcessState() == 0) {//跳转到接单页面
                    Intent intent = new Intent(QiangXiu_Main.this, OrderReceive.class);
                    intent.putExtra("position", position);
                    intent.putExtra("data", ri);
                    startActivityForResult(intent,1);
                } else if (ri.getReceiveState() == 1) {//跳转到处理页面--包含等待处理和处理完成
                    Intent intent = new Intent(QiangXiu_Main.this, OrderHandle.class);
                    intent.putExtra("position", position);
                    intent.putExtra("data", ri);
                    startActivityForResult(intent,2);
                }
            }
        });
        //获取抢修信息数据
        getAllQXData();
    }

    /**
     * 有网络的话去服务端得到最新数据并同步化到本地然后从本地加载数据
     * 没有网络的话直接从本地加载数据
     */
    private void getAllQXData() {
        //检测是否有网络
        if (Utility.getNetworkStatus(this)) {
            //将数据库表中的内容清空
            qxdb.clearTableContent("repair_info");
            //从web服务端读取数据并将数据写入本地的sqlite数据表中并更新repairInfoList，动态加载listview
            getData();
        }else{
            Toast.makeText(this, "当前在无网络环境", Toast.LENGTH_SHORT).show();
            queryTasks();
        }
    }

    /**
     * 从服务端读取数据并存储到数据库中，并初始化UIlistview的列表数据
     */
    private void getData() {
        String baseaddress = "http://wxeis.eis.swdnkj.com/hrkweb/rest/patrol/getQXList?usercode=";
        final String user = getSharedPreferences("xunjian", MODE_PRIVATE).getString("usercode","");
        String address = baseaddress + user;
//        String address = "http://wxeis.eis.swdnkj.com/hrkweb/rest/patrol/getQXList?usercode=liping";
        HttpUtil.sendHttpGetRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result = false;
                result = Utility.handleResponse(qxdb, response,user);
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
                e.printStackTrace();
            }
        });
    }

    /**
     * 从本地数据库中查询任务信息
     */
    private void queryTasks() {
        List<RepairInfo> dataList = qxdb.loadRepairInfos();
        if (dataList.size() > 0) {
            repairInfoList.clear();
            for (RepairInfo tiTask : dataList) {
                repairInfoList.add(tiTask);
            }
            qxlistAdapter.notifyDataSetChanged();//动态加载
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1://从我要接单页面返回
                if (resultCode == RESULT_OK) {
                    //从data中接收返回的数据，更新当前的listview中的对应数据--通过返回的接单状态更新右边的显示
                    RepairInfo repairInfo = data.getParcelableExtra("data");
                    int position = data.getIntExtra("position", -1);
                    repairInfoList.get(position).setReceiveState(repairInfo.getReceiveState());
                    repairInfoList.get(position).setProcessState(repairInfo.getProcessState());
                    repairInfoList.get(position).setReceiveDate(repairInfo.getReceiveDate());
                    repairInfoList.get(position).setArrivalTime(repairInfo.getArrivalTime());
                    repairInfoList.get(position).setEndTime(repairInfo.getEndTime());
                    repairInfoList.get(position).setRepairPersons(repairInfo.getRepairPersons());
                    repairInfoList.get(position).setFailureCause(repairInfo.getFailureCause());
                    repairInfoList.get(position).setExecuteSituation(repairInfo.getExecuteSituation());
                    repairInfoList.get(position).setRemarks(repairInfo.getRemarks());
                    qxlistAdapter.notifyDataSetChanged();
                }
                break;
            case 2://从处理页面返回
                if (resultCode == RESULT_OK) {
                    //从data中接收返回的数据，更新当前的listview中的对应数据
                    RepairInfo repairInfo = data.getParcelableExtra("data");
                    int position = data.getIntExtra("position", -1);
                    repairInfoList.get(position).setReceiveState(repairInfo.getReceiveState());
                    repairInfoList.get(position).setProcessState(repairInfo.getProcessState());
                    repairInfoList.get(position).setReceiveDate(repairInfo.getReceiveDate());
                    repairInfoList.get(position).setArrivalTime(repairInfo.getArrivalTime());
                    repairInfoList.get(position).setEndTime(repairInfo.getEndTime());
                    repairInfoList.get(position).setRepairPersons(repairInfo.getRepairPersons());
                    repairInfoList.get(position).setFailureCause(repairInfo.getFailureCause());
                    repairInfoList.get(position).setExecuteSituation(repairInfo.getExecuteSituation());
                    repairInfoList.get(position).setRemarks(repairInfo.getRemarks());
                    qxlistAdapter.notifyDataSetChanged();
                }
            default:
                break;
        }
    }


    private void initView() {
        ivBack = (ImageView) findViewById(R.id.inspection_list_back_btn);
        ibDate = (ImageButton) findViewById(R.id.ibtn_date);
        ibCategory = (ImageButton) findViewById(R.id.ibtn_category);
        ibSearch = (ImageButton) findViewById(R.id.ibtn_search);
        tvDate = (TextView) findViewById(R.id.tv_Date);
        tvCategory = (TextView) findViewById(R.id.tv_category);
        lvInfo = (ListView) findViewById(R.id.lv_info);
    }

    private void initOnClick() {
        ivBack.setOnClickListener(this);
        ibDate.setOnClickListener(this);
        ibCategory.setOnClickListener(this);
        ibSearch.setOnClickListener(this);
    }

    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private SlideDateTimeListener listener = new SlideDateTimeListener() {
        @Override
        public void onDateTimeSet(Date date) {
            String dateStr = mFormatter.format(date);
            tvDate.setText(dateStr);
            List<RepairInfo> repairInfos;
            if ("类型".equals(tvCategory.getText().toString())) {
                //从sqlite数据库中查询日期为dateStr的任务列表
                repairInfos = qxdb.queryByDate(dateStr);
                //重写加载listview中的数据
                repairInfoList.clear();
                for (RepairInfo ri : repairInfos) {
                    repairInfoList.add(ri);
                }
            } else {
                //从sqlite数据库中通过时间和类型查询
                repairInfos = qxdb.queryByCateDate(tvCategory.getText().toString(), dateStr);
                //重写加载listview中的数据
                repairInfoList.clear();
                for (RepairInfo ri : repairInfos) {
                    repairInfoList.add(ri);
                }
            }
            //更新listview中的显示
            qxlistAdapter.notifyDataSetChanged();
        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel() {
            Toast.makeText(QiangXiu_Main.this,
                    "Canceled", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击左上角返回
            case R.id.inspection_list_back_btn:
//                Intent intent = new Intent(this, HomeActivity.class);
//                startActivity(intent);
                setResult(RESULT_OK);
                finish();
                break;
            //点击选择时间
            case R.id.ibtn_date:
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
            //点击选择类型
            case R.id.ibtn_category:
                //弹出分类选择对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(QiangXiu_Main.this);
                builder.setTitle("请选择查询类型");
                final String[] categories = {"特巡", "故障", "巡视"};
                builder.setItems(categories, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String category = categories[which];
                        tvCategory.setText(category);
                        String time = tvDate.getText().toString();
                        List<RepairInfo> repairInfos;
                        if ("时间".equals(time)) {
                            //从sqlite数据库中查询类型为category的任务列表
                            repairInfos = qxdb.queryByCategory(category);
                            //重写加载listview中的数据
                            repairInfoList.clear();
                            for (RepairInfo ri : repairInfos) {
                                repairInfoList.add(ri);
                            }
                        } else {
                            //从sqlite数据库中通过时间和类型查询
                            repairInfos = qxdb.queryByCateDate(time,category);
                            repairInfoList.clear();
                            for (RepairInfo ri : repairInfos) {
                                repairInfoList.add(ri);
                            }
                        }
                        //更新listview中的显示
                        qxlistAdapter.notifyDataSetChanged();
                    }
                });
                builder.show();
                break;
            //点击刷新
            case R.id.ibtn_search:
                //更新repairInfoList，重写加载新数据
                getAllQXData();
                tvDate.setText("时间");
                tvCategory.setText("类型");
                Toast.makeText(this, "刷新成功", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }
}
