package com.swdn.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 网络Get请求的线程
 * */
public class HttpGetThread implements Runnable {
	
	private final String TAG = HttpGetThread.class.getSimpleName();
	private Handler hand;
	private String url;
//	private MyGet myGet = new MyGet();

	public HttpGetThread(Handler hand, String endParamerse) {
		this.hand = hand;
		// 拼接访问服务器完整的地址
		url = HttpUtil.URL+ endParamerse;
	}

	@Override
	public void run() {
		// 获取我们回调主ui的message
		Message msg = hand.obtainMessage();
		Log.e(TAG, url);
		try {
			String result = HttpUtil.doGet(url);
			msg.what = 200;
			msg.obj = result;
			Log.e(TAG,"get数据成功");
		} catch (Exception e) {
			msg.what = 404;
		} 
		// 给主ui发送消息传递数据
		hand.sendMessage(msg);
	}

}
