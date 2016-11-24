package com.swdn.xunjian;

import com.swdn.R;
import com.swdn.widget.FinderView;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import android.app.Activity;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

public class CaptureActivity extends Activity implements SurfaceHolder.Callback {
	private static String TAG = "xiaoqiang";
	private Camera mCamera;
	private SurfaceHolder mHolder;
	private SurfaceView surface_view;
	private ImageScanner scanner;
	private Handler autoFocusHandler;
	private AsyncDecode asyncDecode;
	private ImageButton btn_back;
	private FinderView finder_view;
	private TextView textview;
	static {
		System.loadLibrary("iconv");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.ac_zbar_finder);
		init();
	}

	private void init() {
		surface_view = (SurfaceView) findViewById(R.id.surface_view);
		finder_view = (FinderView) findViewById(R.id.finder_view);
		textview = (TextView) findViewById(R.id.textview);
		btn_back = (ImageButton)findViewById(R.id.capture_imageview_back);
		mHolder = surface_view.getHolder();
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		mHolder.addCallback(this);
		scanner = new ImageScanner();
		scanner.setConfig(0, Config.X_DENSITY, 3);
		scanner.setConfig(0, Config.Y_DENSITY, 3);
		autoFocusHandler = new Handler();
		asyncDecode = new AsyncDecode();
		
		btn_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		if (mHolder.getSurface() == null) {
			return;
		}
		try {
			mCamera.stopPreview();
		} catch (Exception e) {
		}
		try {
			mCamera.setDisplayOrientation(90);
			mCamera.setPreviewDisplay(mHolder);
			mCamera.setPreviewCallback(previewCallback);
			mCamera.startPreview();
			mCamera.autoFocus(autoFocusCallback);
		} catch (Exception e) {
			Log.d("DBG", "Error starting camera preview: " + e.getMessage());
		}
	}

	/**
	 * 预览数据
	 */
	PreviewCallback previewCallback = new PreviewCallback() {
		public void onPreviewFrame(byte[] data, Camera camera) {
			if (asyncDecode.isStoped()) {
				//				Camera.Parameters parameters = camera.getParameters();
				//				Size size = parameters.getPreviewSize();
				//				Image barcode = new Image(size.width, size.height, "Y800");
				//				barcode.setData(data);
				//				asyncDecode = new AsyncDecode();
				//				asyncDecode.execute(barcode);
				Camera.Parameters parameters = camera.getParameters();
				Size size = parameters.getPreviewSize();
				//图片是被旋转了90度的
				Image source = new Image(size.width, size.height, "Y800");
				Rect scanImageRect = finder_view.getScanImageRect(size.height, size.width);
				//图片旋转了90度，将扫描框的TOP作为left裁剪
				source.setCrop(scanImageRect.top, scanImageRect.left, scanImageRect.bottom, scanImageRect.right);
				source.setData(data);
				asyncDecode = new AsyncDecode();
				asyncDecode.execute(source);

			}
		}
	};

	private class AsyncDecode extends AsyncTask<Image, Void, Void> {
		private boolean stoped = true;
		private String str = "";

		@Override
		protected Void doInBackground(Image... params) {
			stoped = false;
			StringBuilder sb = new StringBuilder();
			Image barcode = params[0];
			int result = scanner.scanImage(barcode);
			if (result != 0) {
				//				mCamera.setPreviewCallback(null);
				//				mCamera.stopPreview();
				SymbolSet syms = scanner.getResults();
				for (Symbol sym : syms) {
					switch (sym.getType()) {
						case Symbol.CODE128:
							//128编码格式二维码
							Log.d(TAG, "128编码格式二维码:  " + sym.getData());
							sb.append(sym.getData() + "\n");
							break;
						case Symbol.QRCODE:
							//QR码二维码  
							Log.d(TAG, "QR码二维码  :" + sym.getData());
							sb.append(sym.getData() + "\n");
							break;			
					}
				}
			}
			str = sb.toString();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			stoped = true;
			if (null == str || str.equals("")) {
			} else {
				textview.setText(str);
			}
		}

		public boolean isStoped() {
			return stoped;
		}
	}

	/**
	 * 自动对焦回调
	 */
	AutoFocusCallback autoFocusCallback = new AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			autoFocusHandler.postDelayed(doAutoFocus, 1000);
		}
	};

	//自动对焦
	private Runnable doAutoFocus = new Runnable() {
		public void run() {
			if (null == mCamera || null == autoFocusCallback) {
				return;
			}
			mCamera.autoFocus(autoFocusCallback);
		}
	};

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			mCamera = Camera.open();
		} catch (Exception e) {
			mCamera = null;
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (mCamera != null) {
			mCamera.setPreviewCallback(null);
			mCamera.release();
			mCamera = null;
		}
	}
}
