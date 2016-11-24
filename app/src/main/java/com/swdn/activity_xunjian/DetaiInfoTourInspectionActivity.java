package com.swdn.activity_xunjian;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.R;
import com.swdn.adapter_xunjian.DetaiTourInspectionAdapter;
import com.swdn.db_xunjian.TourInspectionDB;
import com.swdn.model_xunjian.TourInspectionDev;
import com.swdn.model_xunjian.TourInspectionTask;

import java.util.ArrayList;
import java.util.List;

public class DetaiInfoTourInspectionActivity extends Activity {

    public static final String ACTION = "android.intent.action.com.qiu.listviewoftourinspection.DetaiInfoTourInspectionActivity";

    private List<TourInspectionDev> tiDeviceList = new ArrayList<TourInspectionDev>();
    private ListView listView;
    private TourInspectionDB tourInspectionDB;
    private TextView taskId;
    private TextView writePerson;
    private TextView execDate;
    private TextView category;
    private TextView execPerson;
    private TextView dept;

    DetaiTourInspectionAdapter dtiAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detai_info__tour_inspection);
        taskId = (TextView) findViewById(R.id.tv_taskId);
        writePerson = (TextView) findViewById(R.id.tv_writePerson);
        execDate = (TextView) findViewById(R.id.tv_exceDate);
        category = (TextView) findViewById(R.id.tv_category);
        execPerson = (TextView) findViewById(R.id.tv_execPerson);
        dept = (TextView) findViewById(R.id.tv_dept);
        tourInspectionDB = TourInspectionDB.getInstance(this);

        //接收上一个activity传过来的数据
        final TourInspectionTask taskInfo = (TourInspectionTask)getIntent().getSerializableExtra("dataTask");
        final int position = getIntent().getIntExtra("position", -1);
        pos = position;

        ImageView backImage = (ImageView)findViewById(R.id.inspection_list_back_btn);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("position", position);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        taskId.setText(String.valueOf(taskInfo.getId()));
        writePerson.setText(taskInfo.getWritePerson());
        execDate.setText(taskInfo.getExecDate());
        category.setText(taskInfo.getCategory());
        execPerson.setText(taskInfo.getExecPerson());
        dept.setText(taskInfo.getDept());
        tiDeviceList = tourInspectionDB.loadTourInspectionDevs(taskInfo.getId());
//        Bundle bundle = getIntent().getBundleExtra("dataDevs");
//        tiDeviceList = bundle.getParcelableArrayList("devs");

        dtiAdapter = new DetaiTourInspectionAdapter(DetaiInfoTourInspectionActivity.this,
                R.layout.listviewitem_detail_info_tourinspection,tiDeviceList);
        listView = (ListView)findViewById(R.id.detailInfo_tourInspection);
        listView.setAdapter(dtiAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final TourInspectionDev tiDev = tiDeviceList.get(i);

                int isCommited = tiDev.getIsCommited();

                Intent intentToReply = new Intent(DetaiInfoTourInspectionActivity.this,TourInspectionReplyActivity.class);
                //向intent中填充数据
                intentToReply.putExtra("data",tiDev);
                intentToReply.putExtra("tiPerson",taskInfo.getExecPerson());
                intentToReply.putExtra("position",i);
                startActivityForResult(intentToReply, 1);
//                startActivity(intentToReply);

            }
        });
    }

    private int pos;
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("position", pos);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    int position = data.getIntExtra("position",-1);
                    int id = data.getIntExtra("id",0);
                    tiDeviceList.get(position).setIsCommited(1);
                    TourInspectionDev tiDev1 = tourInspectionDB.loadTourInspectionDevById(id);
                    tiDeviceList.get(position).setTourDate(tiDev1.getTourDate());
                    tiDeviceList.get(position).setTourPerson(tiDev1.getTourPerson());
                    tiDeviceList.get(position).setTourKey(tiDev1.getTourKey());
                    tiDeviceList.get(position).setTourEnd(tiDev1.getTourEnd());
                    tiDeviceList.get(position).setTourRemarks(tiDev1.getTourRemarks());
                    dtiAdapter.notifyDataSetChanged();
                }
                break;
            default:
                break;
        }
    }
}
