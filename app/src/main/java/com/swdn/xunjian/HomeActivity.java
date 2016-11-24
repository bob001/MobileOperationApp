package com.swdn.xunjian;

import java.util.ArrayList;
import java.util.List;

import com.amapsw.MapMainActivity;
import com.R;
import com.swdn.fragment.OperationFragment;
import com.swdn.fragment.AlarmFragment;
import com.swdn.fragment.TestFragment;
import com.swdn.fragment.UserFragment;
import com.swdn.widget.ChangeColorIconWithText;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Toast;

/**
 * 主页
 * @author wuxian
 *
 */
public class HomeActivity extends FragmentActivity implements OnClickListener,
		OnPageChangeListener {
	private static final String TAG = HomeActivity.class.getSimpleName();
	private ViewPager mViewPager;
	private List<Fragment> mTabs = new ArrayList<Fragment>();
	private FragmentPagerAdapter mAdapter;
	private List<ChangeColorIconWithText> mTabIndicators = new ArrayList<ChangeColorIconWithText>();
	//private ImageButton titleButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		setContentView(R.layout.home_layout);
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title);
		//titleButton = (ImageButton)findViewById(R.id.title_qr);
		initView();
		initDatas();
		mViewPager.setAdapter(mAdapter);
		initEvent();
	}

	/**
	 * 初始化所有事件
	 */
	private void initEvent() {
//		mViewPager.addOnPageChangeListener(this);
		mViewPager.setOnPageChangeListener(this);
	}

	private void initDatas() {
		// 绑定4个fragment
		OperationFragment homeFragment = new OperationFragment();
		//InspectionListFragment homeFragment = new InspectionListFragment();
		mTabs.add(homeFragment);
		AlarmFragment networkFragment = new AlarmFragment();
		mTabs.add(networkFragment);
		TestFragment homeFragment1 = new TestFragment();
		mTabs.add(homeFragment1);
		UserFragment userFragment = new UserFragment();
		mTabs.add(userFragment);

		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return mTabs.size();
			}

			@Override
			public Fragment getItem(int position) {
				return mTabs.get(position);
			}
		};
	}

	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.main_content_viewpager);

		// 关联每个tab的
		ChangeColorIconWithText tab_operation = (ChangeColorIconWithText) findViewById(R.id.tab_operation);
		mTabIndicators.add(tab_operation);
		ChangeColorIconWithText tab_alarm = (ChangeColorIconWithText) findViewById(R.id.tab_alarm);
		mTabIndicators.add(tab_alarm);
		ChangeColorIconWithText tab_test = (ChangeColorIconWithText) findViewById(R.id.tab_test);
		mTabIndicators.add(tab_test);
		ChangeColorIconWithText tab_personal = (ChangeColorIconWithText) findViewById(R.id.tab_personal);
		mTabIndicators.add(tab_personal);

		tab_operation.setOnClickListener(this);
		tab_alarm.setOnClickListener(this);
		tab_test.setOnClickListener(this);
		tab_personal.setOnClickListener(this);

		tab_operation.setIconAlpha(1.0f);
	}

	@Override
	public void onClick(View v) {
		clickTab(v);
	}

	/**
	 * 点击Tab按钮，响应事件
	 * 
	 * @param v
	 */
	private void clickTab(View v) {

		resetOtherTabs();
		switch (v.getId()) {
		case R.id.tab_operation:
			mTabIndicators.get(0).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(0, false);
			break;
		case R.id.tab_alarm:
			mTabIndicators.get(1).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(1, false);
			Toast.makeText(HomeActivity.this, "正在建设中，敬请期待", Toast.LENGTH_SHORT).show();
			break;
		case R.id.tab_test:
			mTabIndicators.get(2).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(2, false);
			Toast.makeText(HomeActivity.this, "正在建设中，敬请期待", Toast.LENGTH_SHORT).show();
			break;
		case R.id.tab_personal:
			mTabIndicators.get(3).setIconAlpha(1.0f);
			mViewPager.setCurrentItem(3, false);

			break;
		}
	}

	/**
	 * 重置其他的TabIndicator的颜色
	 */
	private void resetOtherTabs() {
		for (int i = 0; i < mTabIndicators.size(); i++) {
			mTabIndicators.get(i).setIconAlpha(0);
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		if (positionOffset > 0) {
			ChangeColorIconWithText left = mTabIndicators.get(position);
			ChangeColorIconWithText right = mTabIndicators.get(position + 1);
			left.setIconAlpha(1 - positionOffset);
			right.setIconAlpha(positionOffset);
		}

	}

	@Override
	public void onPageSelected(int position) {

	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}
	
	public List<Fragment> getmTabs() {
		return mTabs;
	}


}
