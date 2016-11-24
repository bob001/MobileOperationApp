package com.swdn.xunjian;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.R;

public class MainActivity extends Activity
{
	private TextView mTextView;
	private static final String KEY ="wW4rOPc0r5GocFHwvoDG4Uc8";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		//autoBind();
	}

	private void initView()
	{
		mTextView = (TextView) findViewById(R.id.id_textview);
	}

	@Override
	protected void onNewIntent(Intent intent)
	{
		String result = intent.getStringExtra("result");
		mTextView.setText("---");
		if (result != null)
		{
			mTextView.setText(result);
		}
		// super.onNewIntent(intent);
	}



}
