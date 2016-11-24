package com.swdn.activity_xunshi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.R;
import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.swdn.adapter_xunshi.XunShiListAdapter;
import com.swdn.db_xunshi.XSDB;
import com.swdn.model_xunshi.TourInfo;
import com.swdn.utils.HttpCallbackListener;
import com.swdn.utils.HttpUtil;
import com.swdn.utils.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class XunShi_Main extends FragmentActivity implements View.OnClickListener{

    ImageView iv_back;
    ImageButton ib_tourDate;
    TextView tv_tourDate;
    ImageButton ib_tourCategory;
    TextView tv_tourCategory;
    ImageButton ib_refresh;
    ListView lv_tourList;

    private XunShiListAdapter xsListAdapter;
    private List<TourInfo> tourInfoList = new ArrayList<TourInfo>();

    private XSDB xsdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_xunshi__main);
        xsdb = XSDB.getInstance(this);
        initView();
        initOnclick();
        xsListAdapter = new XunShiListAdapter(this, R.layout.listviewitem_tourlist, tourInfoList);
        lv_tourList.setAdapter(xsListAdapter);
        lv_tourList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TourInfo tourInfo = tourInfoList.get(position);
                if (tourInfo.getIsReceived() == 0 && tourInfo.getIsFinished() == 0) {//跳转到接单页面
                    Intent intent = new Intent(XunShi_Main.this, OrderReceive.class);
                    intent.putExtra("position", position);
                    intent.putExtra("data", tourInfo);
                    startActivityForResult(intent,1);
                } else if (tourInfo.getIsReceived() == 1) {//跳转到处理页面--包含等待处理和处理完成
                    Intent intent = new Intent(XunShi_Main.this, OrderHandle.class);
                    intent.putExtra("position", position);
                    intent.putExtra("data", tourInfo);
                    startActivityForResult(intent,2);
                }
            }
        });
        //获取巡视信息数据
        getAllXSData();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        iv_back = (ImageView) findViewById(R.id.inspection_list_back_btn);
        ib_tourDate = (ImageButton) findViewById(R.id.ibtn_tour_date);
        tv_tourDate = (TextView) findViewById(R.id.tv_tour_Date);
        ib_tourCategory = (ImageButton) findViewById(R.id.ibtn_tour_category);
        tv_tourCategory = (TextView) findViewById(R.id.tv_tour_category);
        ib_refresh = (ImageButton) findViewById(R.id.ibtn_tour_search);
        lv_tourList = (ListView) findViewById(R.id.lv_tour_info);
    }

    /**
     * 初始化点击事件
     */
    private void initOnclick() {
        iv_back.setOnClickListener(this);
        ib_tourDate.setOnClickListener(this);
        ib_tourCategory.setOnClickListener(this);
        ib_refresh.setOnClickListener(this);
    }

    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private SlideDateTimeListener listener = new SlideDateTimeListener() {
        @Override
        public void onDateTimeSet(Date date) {
            String dateStr = mFormatter.format(date);
            tv_tourDate.setText(dateStr);
            List<TourInfo> tourInfos;
            if ("类型".equals(tv_tourCategory.getText().toString())) {
                //从sqlite数据库中查询接单日期为dateStr的任务列表
                tourInfos = xsdb.queryByDate(dateStr);
                //重写加载listview中的数据
                tourInfoList.clear();
                for (TourInfo ti : tourInfos) {
                    tourInfoList.add(ti);
                }
            } else {
                //从sqlite数据库中通过接单时间和类型查询
                tourInfos = xsdb.queryByCateDate(tv_tourCategory.getText().toString(), dateStr);
                //重写加载listview中的数据
                tourInfoList.clear();
                for (TourInfo ti : tourInfos) {
                    tourInfoList.add(ti);
                }
            }
            //更新listview中的显示
            xsListAdapter.notifyDataSetChanged();
        }
    };

    /**
     * 点击操作
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击back
            case R.id.inspection_list_back_btn:
                setResult(RESULT_OK);
                finish();
                break;
            //点击时间选择
            case R.id.ibtn_tour_date:
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
            //点击类型选择
            case R.id.ibtn_tour_category:
                //弹出分类选择对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(XunShi_Main.this);
                builder.setTitle("请选择查询类型");
                final String[] categories = {"特巡", "故障", "巡视"};
                builder.setItems(categories, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String category = categories[which];
                        tv_tourCategory.setText(category);
                        String time = tv_tourDate.getText().toString();
                        List<TourInfo> tourInfos;
                        if ("时间".equals(time)) {
                            //从sqlite数据库中查询类型为category的任务列表
                            tourInfos = xsdb.queryByCategory(category);
                            //重写加载listview中的数据
                            tourInfoList.clear();
                            for (TourInfo ti : tourInfos) {
                                tourInfoList.add(ti);
                            }
                        } else {
                            //从sqlite数据库中通过接单时间和类型查询
                            tourInfos = xsdb.queryByCateDate(time,category);
                            tourInfoList.clear();
                            for (TourInfo ti : tourInfos) {
                                tourInfoList.add(ti);
                            }
                        }
                        //更新listview中的显示
                        xsListAdapter.notifyDataSetChanged();
                    }
                });
                builder.show();
                break;
            //点击刷新
            case R.id.ibtn_tour_search:
                //更新repairInfoList，重写加载新数据
                getAllXSData();
                tv_tourDate.setText("时间");
                tv_tourCategory.setText("类型");
                Toast.makeText(XunShi_Main.this, "刷新成功", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    /**
     * 有网络的话去服务端得到最新数据并同步化到本地然后从本地加载数据
     * 没有网络的话直接从本地加载数据
     */
    private void getAllXSData() {
        //检测是否有网络
        if (Utility.getNetworkStatus(XunShi_Main.this)) {
            //将数据库表中的内容清空
            xsdb.clearTableContent("tour_info");
            //从web服务端读取数据并将数据写入本地的sqlite数据表中并更新repairInfoList，动态加载listview
            getData();
        }else{
            Toast.makeText(XunShi_Main.this, "当前在无网络环境", Toast.LENGTH_SHORT).show();
            queryTasks();
        }
    }

    /**
     * 从服务端读取数据并存储到数据库中，并初始化UIlistview的列表数据
     */
    private void getData() {
//        String baseaddress = "http://wxeis.eis.swdnkj.com/hrkweb/rest/patrol/getbdxsList?usercode=";
//        String baseaddress = "http://dsm.swdnkj.com:9080/EIS/rest/patrol/getbdxsList?usercode=";
        String baseaddress = "http://61.177.76.218:8090/boyuan/rest/patrol/getbdxsList?usercode=";
        final String user = getSharedPreferences("xunjian", MODE_PRIVATE).getString("usercode","");
        String address = baseaddress + user;
        HttpUtil.sendHttpGetRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result = false;
                result = Utility.handleXSResponse(xsdb, response,user);
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
        List<TourInfo> dataList = xsdb.loadTourInfos();
        if (dataList.size() > 0) {
            tourInfoList.clear();
            for (TourInfo tiTask : dataList) {
                tourInfoList.add(tiTask);
            }
            xsListAdapter.notifyDataSetChanged();//动态加载
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1://从我要接单页面返回
                if (resultCode == RESULT_OK) {
                    //从data中接收返回的数据，更新当前的listview中的对应数据--通过返回的接单状态更新右边的显示
                    TourInfo tourInfo = data.getParcelableExtra("data");
                    int position = data.getIntExtra("position", -1);
                    tourInfoList.get(position).setIsReceived(tourInfo.getIsReceived());
                    tourInfoList.get(position).setIsFinished(tourInfo.getIsFinished());
                    tourInfoList.get(position).setReceiveDate(tourInfo.getReceiveDate());
                    //从处理一面返回到接单页面后再返回所携带回填的数据
                    tourInfoList.get(position).setTourStartTime(tourInfo.getTourStartTime());
                    tourInfoList.get(position).setTourEndTime(tourInfo.getTourEndTime());
                    tourInfoList.get(position).setTourPerson(tourInfo.getTourPerson());
                    tourInfoList.get(position).setTourSituation(tourInfo.getTourSituation());
                    tourInfoList.get(position).setRemarks(tourInfo.getRemarks());
                    xsListAdapter.notifyDataSetChanged();
                }
                break;
            case 2://从处理页面返回
                if (resultCode == RESULT_OK) {
                    //从data中接收返回的数据，更新当前的listview中的对应数据
                    TourInfo tourInfo = data.getParcelableExtra("data");
                    int position = data.getIntExtra("position", -1);
                    tourInfoList.get(position).setIsFinished(tourInfo.getIsFinished());
                    tourInfoList.get(position).setTourStartTime(tourInfo.getTourStartTime());
                    tourInfoList.get(position).setTourEndTime(tourInfo.getTourEndTime());
                    tourInfoList.get(position).setTourPerson(tourInfo.getTourPerson());
                    tourInfoList.get(position).setTourSituation(tourInfo.getTourSituation());
                    tourInfoList.get(position).setRemarks(tourInfo.getRemarks());
                    xsListAdapter.notifyDataSetChanged();
                }
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
