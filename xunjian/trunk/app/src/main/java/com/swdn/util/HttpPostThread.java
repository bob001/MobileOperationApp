package com.swdn.util;

import android.os.Handler;
import android.os.Message;

/**
 * 网络Post请求的线程
 * */

public class HttpPostThread implements Runnable {

	private Handler hand;
	private String url;
	private String value;

	public HttpPostThread(Handler hand, String endParamerse, String value) {
		this.hand = hand;
		// 拼接访问服务器完整的地址
		url = HttpUtil.URL+endParamerse;
		this.value = value;
	}

	@Override
	public void run() {
		// 获取我们回调主ui的message
		Message msg = hand.obtainMessage();
		String result = null;
		try {
			result = HttpUtil.doPost(url, value);
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
