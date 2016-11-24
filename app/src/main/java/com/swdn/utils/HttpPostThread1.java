package com.swdn.utils;

import android.os.Handler;
import android.os.Message;

import com.swdn.util.HttpUtil;

/**
 * 网络Post请求的线程
 * */

public class HttpPostThread1 implements Runnable {

	private Handler hand;
	private String url;
	private String value;

	public HttpPostThread1(Handler hand, String url, String value) {
		this.hand = hand;
		this.url = url;
		this.value = value;
	}

	@Override
	public void run() {
		// 获取我们回调主ui的message
		Message msg = hand.obtainMessage();
		String result = null;
		try {
			result = HttpUtil1.doPost(url, value);
			msg.what = 200;
			msg.obj = result;
		} catch (Exception e) {
			msg.what = 404;
			e.printStackTrace();
		}	
		// 给主ui发送消息传递数据
		hand.sendMessage(msg);

	}
}
