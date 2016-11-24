package com.swdn.widget;

import com.R;
import com.swdn.xunjian.CaptureActivity;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class TitleLayout extends LinearLayout {
	private ImageButton qr_btn;

	public TitleLayout(Context context, AttributeSet attrs) {
		
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.main_title, this);
		qr_btn = (ImageButton) findViewById(R.id.title_qr);
		
		qr_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(),
						CaptureActivity.class);
				v.getContext().startActivity(intent);
			}
		});
	}

}
