package com.swdn.fragment;

import com.swdn.R;
import com.swdn.log.Log;
import com.swdn.model.RMessage;
import com.swdn.util.GsonTools;
import com.swdn.util.HttpGetThread;
import com.swdn.util.ThreadPoolUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;

public class UserFragment extends Fragment implements OnClickListener{
	private final String TAG = UserFragment.class.getSimpleName();
	private Button logout;
	private String account;
	private SharedPreferences sp ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.user_fragment,
				container, false);
		logout = (Button) view.findViewById(R.id.btn_logout);
		logout.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View view) {
		sp = view.getContext().getSharedPreferences("xunjian",
				Context.MODE_PRIVATE);
		account = sp.getString("usercode", "");
		//清空数据控制channel_id
		String url = "rest/mainpage/logout_android?account=" + account ;


		ThreadPoolUtils.execute(new HttpGetThread(hand, url));

		SharedPreferences.Editor editor = sp.edit();
		editor.clear();
		editor.commit();
		System.exit(0);
	}
	
	/**
	 * 返回信息处理
	 */
	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			Log.i(TAG,"退出登录开始");
			if (msg.what == 404) {
				Log.i(TAG, "请求失败，服务器故障");
			} else if (msg.what == 200) {
				String result = (String) msg.obj;
				//Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT);
				if (result != null) {
					RMessage r= GsonTools.getObject(result,RMessage.class);
					String info = r.getMsg().toString();
					Log.i(TAG, info);
					if (r.getCode() == 1) {
						SharedPreferences.Editor editor = sp.edit();
						editor.clear();
						editor.commit();
						System.exit(0);
					} else {
						Log.i(TAG, "退出失败");
					}
				}
			}
			Log.i(TAG, "退出登陆结束");
		};
	};

}
