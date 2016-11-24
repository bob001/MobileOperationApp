package com.swdn.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 网络Get请求的线程
 * */
public class HttpGetThread1 implements Runnable {
	
	private final String TAG = HttpGetThread1.class.getSimpleName();
	private Handler hand;
	private String url;
	//为了进行批量的发出get未处理数量的请求而设置的不同get请求的标志位
	private String tag_getCount;
//	private MyGet myGet = new MyGet();

	public HttpGetThread1(Handler hand, String url,String tag) {
		this.hand = hand;
		this.url = url;
		this.tag_getCount = tag;
	}

	@Override
	public void run() {
		// 获取我们回调主ui的message
		Message msg = hand.obtainMessage();
		Log.e(TAG,url);
		try {
			String result = HttpUtil1.doGet(url);
			if(tag_getCount.equals("XJ")){//发出的是巡检未完成数量的请求
				msg.what = 2001;
			}else if(tag_getCount.equals("XS")){//发出的是巡视未完成数量的请求
				msg.what = 2002;
			} else if (tag_getCount.equals("QX")) {//发出的是抢修未完成数量的请求
				msg.what = 2003;
			}
			msg.obj = result;
			Log.e(TAG,"get数据成功");
		} catch (Exception e) {
			msg.what = 404;
		} 
		// 给主ui发送消息传递数据
		hand.sendMessage(msg);
	}

}
