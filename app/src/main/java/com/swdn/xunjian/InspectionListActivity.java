package com.swdn.xunjian;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import com.R;
import com.swdn.adapter.InspectionListAdapter;
import com.swdn.entity.InspectionPlan;
import com.swdn.log.Log;
import com.swdn.util.HttpGetThread;
import com.swdn.util.MyJson;
import com.swdn.util.ThreadPoolUtils;
import com.swdn.widget.InspectionListView;
import com.swdn.widget.InspectionListView.OnRefreshListener;
import com.swdn.widget.WheelView;
import com.swdn.xunjian.MainActivity;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


/**
 * 热门的fragment
 * */
public class InspectionListActivity extends Activity implements OnClickListener {

	private final String TAG = InspectionListActivity.class.getSimpleName();
	private Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);
	private String hotUrl = "127.0.0.1";
	private ImageView mTopImg;
	private ImageView mSendAshamed;
	private InspectionListView myListView;
	private LinearLayout mLinearLayout, load_progressBar;
	private TextView mListNoValue;
	private MyJson myJson = new MyJson();
	private List<InspectionPlan> list = new ArrayList<InspectionPlan>();
	private InspectionListAdapter mAdapter = null;
	private Button ListBottem = null;
	private int mStart = 0;
	private int mEnd = 5;
	private String url = null;
	private boolean flag = true;
	private boolean loadflag = false;
	private boolean listBottemFlag = true;
	private ImageButton date_btn;    //日期选择按钮
	private ImageButton type_btn;    //类型选择按钮
	private SearchView search_btn;   //搜索框
	private WheelView mWheelView;
	private List<String> mArrayList = new ArrayList<String>();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.inspectation_list_layout);
		myListView = new InspectionListView(InspectionListActivity.this);
		initView();
	}


	private void initView() {
		initData(0,5);
		load_progressBar = (LinearLayout) findViewById(R.id.load_progressBar);
		mLinearLayout = (LinearLayout) findViewById(R.id.ListGroup);
		myListView.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		//添加每项直接分割线
		//myListView.setDivider(R.drawable.list_item_divider);
		myListView.setDividerHeight(1);
		mLinearLayout.addView(myListView);
		mTopImg = (ImageView) findViewById(R.id.inspection_list_back_btn);
		mSendAshamed = (ImageView) findViewById(R.id.SendAshamed);
		mListNoValue = (TextView) findViewById(R.id.ListNoValue);
		mTopImg.setOnClickListener(this);
		mSendAshamed.setOnClickListener(this);
		date_btn = (ImageButton)findViewById(R.id.date_btn);
		search_btn = (SearchView)findViewById(R.id.search_btn);
		date_btn.setOnClickListener(this);
		type_btn.setOnClickListener(this);
		search_btn.setOnClickListener(this);
		createTextColor();
		mAdapter = new InspectionListAdapter(InspectionListActivity.this, list);
		ListBottem = new Button(InspectionListActivity.this);
		ListBottem.setText("点击加载更多");
		ListBottem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (flag && listBottemFlag) {
					url = hotUrl + "start=" + mStart + "&end=" + mEnd;
					ThreadPoolUtils.execute(new HttpGetThread(hand, url));
					listBottemFlag = false;
				} else if (!listBottemFlag)
					Toast.makeText(InspectionListActivity.this, "正在加载中...", 1).show();
			}
		});
		myListView.addFooterView(ListBottem, null, false);
		ListBottem.setVisibility(View.GONE);
		myListView.setAdapter(mAdapter);
		myListView.setOnItemClickListener(new MainListOnItemClickListener());
		url = hotUrl + "start=" + mStart + "&end=" + mEnd;
		ThreadPoolUtils.execute(new HttpGetThread(hand, url));
		myListView.setonRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {

				if (loadflag == true) {
					mStart = 6;
					mEnd = 10;
					url = hotUrl + "start=" + mStart + "&end=" + mEnd;
					ListBottem.setVisibility(View.GONE);
					ThreadPoolUtils.execute(new HttpGetThread(hand, url));
					initData(mStart,mEnd);
					loadflag = false;
				} else {
					Toast.makeText(InspectionListActivity.this, "正在加载中，请勿重复刷新", 1).show();
				}

			}
		});
		mWheelView = (WheelView) findViewById(R.id.wheelView);
		 // 初始化仿IOS滚轮效果的数据
        // 在这里可以设置滚轮的偏移量 
        mWheelView.setOffset(2);
        //设置每一个Item中的数据 mArrayList中装着的是一堆String字符串
        mWheelView.setItems(mArrayList);
        mWheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
        @Override
        public void onSelected(int selectedIndex, String item) {
           //selectedIndex当前高亮的位置
           //item当前高亮的位置的内容
        }
    });
		createListModel();
	}

	@Override
	public void onClick(View view) {
		int mID = view.getId();
		switch (mID) {
		case R.id.inspection_list_back_btn:
			this.finish();
			break;
		case R.id.SendAshamed:
			break;
		case R.id.date_btn:
			Log.d(TAG,"日期控件开始");  
            DatePickerDialog  dateDlg = new DatePickerDialog(InspectionListActivity.this,
            		datePickerDialog,
                    dateAndTime.get(Calendar.YEAR),
                    dateAndTime.get(Calendar.MONTH),
                    dateAndTime.get(Calendar.DAY_OF_MONTH));
          
            dateDlg.show();            
            Log.d(TAG,"日期控件结束");
			break;
		case R.id.type_btn:
			mWheelView.setVisibility(View.VISIBLE);
			break;
		case R.id.search_btn:
			break;
		default:
			break;
		}
	}

	private void createListModel() {
		ListBottem.setVisibility(View.VISIBLE);
		mLinearLayout.setVisibility(View.VISIBLE);
		load_progressBar.setVisibility(View.GONE);
		loadflag = false;
		mStart = 0;
		mEnd = 5;
		url = hotUrl + "start=" + mStart + "&end=" + mEnd;
		ThreadPoolUtils.execute(new HttpGetThread(hand, url));
	}

	/*
	 * List Item 点击事件
	 */
	private class MainListOnItemClickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> adapter, View view, int index,
				long arg3) {
			Intent intent = new Intent(InspectionListActivity.this, MainActivity.class);
			Bundle bund = new Bundle();
			bund.putSerializable("InspectionPlan", list.get(index - 1));
			intent.putExtra("value", bund);
			startActivity(intent);
		}
	}
	
	private void createTextColor() {
		mListNoValue.setVisibility(View.GONE);
	}

	private void initData(int to ,int from){
		for(int i = to;i<from;i++){
			InspectionPlan info = new InspectionPlan();
			info.setState(i%2);
			info.setName("zh"+i);
			info.setType("巡检");
			info.setDate("2016-01-0"+i);
			list.add(info);
		}
		mArrayList.add("巡检");
		mArrayList.add("巡视");
	}
	
	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				Toast.makeText(InspectionListActivity.this, "找不到地址", 1).show();
				listBottemFlag = true;
			} else if (msg.what == 100) {
				Toast.makeText(InspectionListActivity.this, "传输失败", 1).show();
				listBottemFlag = true;
			} else if (msg.what == 200) {
				String result = (String) msg.obj;
				if (result != null) {
					List<InspectionPlan> newList = myJson.getInspectionPlanList(result);
					if (newList != null) {
						if (newList.size() == 5) {
							ListBottem.setVisibility(View.VISIBLE);
							mStart += 5;
							mEnd += 5;
						} else if (newList.size() == 0) {
							if (list.size() == 0)
								mListNoValue.setVisibility(View.VISIBLE);
							ListBottem.setVisibility(View.GONE);
							Toast.makeText(InspectionListActivity.this, "已经没有了...", 1).show();
						} else {
							ListBottem.setVisibility(View.GONE);
						}
						if (!loadflag) {
							list.removeAll(list);
						}
						for (InspectionPlan info : newList) {
							list.add(info);
						}
						listBottemFlag = true;
					} else {
						if (list.size() == 0)
							mListNoValue.setVisibility(View.VISIBLE);
					}
				}
				mLinearLayout.setVisibility(View.VISIBLE);
				load_progressBar.setVisibility(View.GONE);
				myListView.onRefreshComplete();
				mAdapter.notifyDataSetChanged();
				loadflag = true;
			}
		};
	};
	
	//当点击DatePickerDialog控件的设置按钮时，调用该方法
    DatePickerDialog.OnDateSetListener datePickerDialog = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                int dayOfMonth) {
            //修改日历控件的年，月，日
            //这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);    
            //将页面TextView的显示更新为最新时间
            Toast.makeText(InspectionListActivity.this,dateAndTime.get(Calendar.YEAR), Toast.LENGTH_LONG);           
        }        
    };
}
