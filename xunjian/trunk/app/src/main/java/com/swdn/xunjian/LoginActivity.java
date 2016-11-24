package com.swdn.xunjian;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.swdn.R;
import com.swdn.log.Log;
import com.swdn.model.RMessage;
import com.swdn.util.GsonTools;
import com.swdn.util.HttpPostThread;
import com.swdn.util.ThreadPoolUtils;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * 登录活动
 * @author wuxian
 *
 */
public class LoginActivity extends Activity implements OnClickListener {

	private static final String TAG = LoginActivity.class.getSimpleName();
	private static final String KEY ="wW4rOPc0r5GocFHwvoDG4Uc8";
	private EditText accountEdit;
	private EditText pwdEdit;
	private Button loginBtn;
	private String account;
	private String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		SharedPreferences sp = getSharedPreferences("xunjian",
				Context.MODE_PRIVATE);
		if (!"".equals(sp.getString("usercode", ""))) {
			Intent intent = new Intent(LoginActivity.this,
					HomeActivity.class);
			startActivity(intent);
			finish();
		}
		setContentView(R.layout.login_layout);
		PushManager.startWork(getApplicationContext(),
				PushConstants.LOGIN_TYPE_API_KEY, KEY);
		initView();
	}
	
	/**
	 * 初始化视图
	 */
	private void initView(){
		accountEdit = (EditText) findViewById(R.id.accountEdit);
		pwdEdit = (EditText) findViewById(R.id.passwordEdit);
		loginBtn = (Button) findViewById(R.id.loginButton);
		loginBtn.setOnClickListener(this);	
	}
	
	/**
	 * 登录按钮响应事件
	 */
	@Override
	public void onClick(View v) {
		//Toast.makeText(LoginActivity.this, R.string.webserverurl,Toast.LENGTH_SHORT).show();
		SharedPreferences sp = getSharedPreferences("xunjian",
				Context.MODE_PRIVATE);
		account = accountEdit.getText().toString().trim();
		if("".equals(pwdEdit.getText().toString())){
			password = pwdEdit.getText().toString();
		}else{
			password = stringToMD5(pwdEdit.getText().toString().trim());
		}
		
		if ("".equals(account) && "".equals(password)) {
			Toast.makeText(LoginActivity.this, "请输入账号和密码", Toast.LENGTH_LONG)
					.show();
		}
		if (checkNetworkState()) {
			String channel_id = sp.getString("channelId", "");
			String url = "rest/mainpage/login_android";
			String value = "account=" + account + "&pwd=" + password
					+ "&channel_id=" + channel_id;
			ThreadPoolUtils.execute(new HttpPostThread(hand, url, value));
		}
	}

	private String stringToMD5(String string) {  
	    byte[] hash;  
	  
	    try {  
	        hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));  
	    } catch (NoSuchAlgorithmException e) {  
	        e.printStackTrace();  
	        return null;  
	    } catch (UnsupportedEncodingException e) {  
	        e.printStackTrace();  
	        return null;  
	    }  
	  
	    StringBuilder hex = new StringBuilder(hash.length * 2);  
	    for (byte b : hash) {  
	        if ((b & 0xFF) < 0x10)  
	            hex.append("0");  
	        hex.append(Integer.toHexString(b & 0xFF));  
	    }  
	  
	    return hex.toString();  
	}  
	
	
	/** 
     * 检测网络是否连接 
     * @return 
     */  
    private boolean checkNetworkState() {  
        boolean flag = false;  
        //得到网络连接信息  
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);  
        //去进行判断网络是否连接  
        if (manager.getActiveNetworkInfo() != null) {  
            flag = manager.getActiveNetworkInfo().isAvailable();  
        }  
        if (!flag) {  
            setNetwork();  
        } 
  
        return flag;  
    }  
      
  
    /** 
     * 网络未连接时，调用设置方法 
     */  
    private void setNetwork(){  
          
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  
        builder.setIcon(R.drawable.ic_launcher);  
        builder.setTitle("网络提示信息");  
        builder.setMessage("网络不可用，如果继续，请先设置网络！");  
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
                Intent intent = null;  
                /** 
                 * 判断手机系统的版本！如果API大于10 就是3.0+ 
                 * 因为3.0以上的版本的设置和3.0以下的设置不一样，调用的方法不同 
                 */  
                if (android.os.Build.VERSION.SDK_INT > 10) {  
                    intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);  
                } else {     //打开网络设置
                    intent = new Intent();  
                    ComponentName component = new ComponentName(  
                            "com.android.settings",  
                            "com.android.settings.WirelessSettings");  
                    intent.setComponent(component);  
                    intent.setAction("android.intent.action.VIEW");  
                }  
                startActivity(intent);  
            }

        });  
  
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
  
            }
        });  
        builder.create();  
        builder.show();  
    }  
    
	/**
	 * 返回信息处理
	 */
	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			SharedPreferences sp = getSharedPreferences("xunjian",
					Context.MODE_PRIVATE);
			super.handleMessage(msg);
			Log.i(TAG,"登录开始");
			if (msg.what == 404) {
				Toast.makeText(LoginActivity.this, "请求失败，服务器故障", Toast.LENGTH_SHORT).show();
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
						editor.putString("usercode", account);
						editor.commit();
						editor.putString("password", password);
						editor.commit();
						Intent intent = new Intent(LoginActivity.this,
								HomeActivity.class);
						startActivity(intent);
						finish();
					} else {
						if (info.equalsIgnoreCase("NORIGHT")) {
							accountEdit.setText("");
							pwdEdit.setText("");
							Toast.makeText(LoginActivity.this, "用户名或密码不存在", Toast.LENGTH_SHORT).show();
						} 
						else{
							Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
						}
					}
				}
			}
			Log.i(TAG, "登陆结束");
		};
	};
}