package com.swdn.fragment;

import com.R;
import com.swdn.activity_qiangxiu.QiangXiu_Main;
import com.swdn.activity_xunjian.MainActivity;
import com.swdn.activity_xunshi.XunShi_Main;
import com.swdn.log.Log;
import com.swdn.model.RMessage;
import com.swdn.util.GsonTools;
import com.swdn.util.HttpGetThread;
import com.swdn.util.ThreadPoolUtils;
import com.swdn.utils.HttpGetThread1;
import com.swdn.utils.ThreadPoolUtils1;
import com.swdn.xunjian.CaptureActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class OperationFragment extends Fragment implements OnClickListener {

    private final String TAG = OperationFragment.class.getSimpleName();
    private ImageButton inspection_btn;
    private ImageButton tour_btn;
    private ImageButton repair_btn;
    private ImageButton qr_btn;
    private Intent intent;
    private TextView inspection_count;
    private TextView xs_count;
    private TextView qx_count;
    private int count = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.operation_fragment, container,
                false);
        inspection_btn = (ImageButton) view.findViewById(R.id.inspection_btn);
        tour_btn = (ImageButton) view.findViewById(R.id.tour_btn);
        repair_btn = (ImageButton) view.findViewById(R.id.repair_btn);
        qr_btn = (ImageButton) view.findViewById(R.id.qr_btn);
        inspection_count = (TextView) view.findViewById(R.id.inspection_count);
        xs_count = (TextView) view.findViewById(R.id.tour_count);
        qx_count = (TextView) view.findViewById(R.id.repair_count);
        inspection_btn.setOnClickListener(this);
        tour_btn.setOnClickListener(this);
        repair_btn.setOnClickListener(this);
        qr_btn.setOnClickListener(this);
        initData(view);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //巡检
            case R.id.inspection_btn:
                intent = new Intent(v.getContext(), MainActivity.class);
                startActivityForResult(intent, 1);
                Log.d(TAG, "跳转巡检信息列表成功", true);
                break;
            //巡视
            case R.id.tour_btn:
                intent = new Intent(v.getContext(), XunShi_Main.class);
                startActivityForResult(intent, 2);
                break;
            //抢修
            case R.id.repair_btn:
                intent = new Intent(v.getContext(), QiangXiu_Main.class);
                startActivityForResult(intent, 3);
                break;
            //二维码
            case R.id.qr_btn:
//                intent = new Intent(v.getContext(), CaptureActivity.class);
//                startActivity(intent);
//                Toast.makeText(this.getActivity(), "正在建设中",
//                        Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "跳转二维码扫描成功", true);
                //测试下图片和视频
//                intent = new Intent(v.getContext(), PhotoVideoTest.class);
//                startActivity(intent);
                break;
            default:
                break;
        }
    }

    public void initData(View view) {
        SharedPreferences sp = view.getContext().getSharedPreferences("xunjian",
                Context.MODE_PRIVATE);
        String usercode = sp.getString("usercode", "");
        Toast.makeText(this.getActivity(), usercode,
                Toast.LENGTH_LONG);
        if (usercode != null && !"".equals(usercode)) {
//			String urlXJCount = "http://wxeis.eis.swdnkj.com/hrkweb/rest/patrol/getInsCount?usercode=" + usercode;
//            String urlXJCount = "http://dsm.swdnkj.com:9080/EIS/rest/patrol/getInsCount?usercode=" + usercode;
            String urlXJCount = "http://61.177.76.218:8090/boyuan/rest/patrol/getInsCount?usercode=" + usercode;
//			String urlXSCount = "http://wxeis.eis.swdnkj.com/hrkweb/rest/patrol/getbdxscount?usercode=" + usercode;
//            String urlXSCount = "http://dsm.swdnkj.com:9080/EIS/rest/patrol/getbdxscount?usercode=" + usercode;
            String urlXSCount = "http://61.177.76.218:8090/boyuan/rest/patrol/getbdxscount?usercode=" + usercode;
//			String urlQXCount = "http://wxeis.eis.swdnkj.com/hrkweb/rest/patrol/getQXcount?usercode=" + usercode;
//            String urlQXCount = "http://dsm.swdnkj.com:9080/EIS/rest/patrol/getQXcount?usercode=" + usercode;
            String urlQXCount = "http://61.177.76.218:8090/boyuan/rest/patrol/getQXcount?usercode=" + usercode;
            List<Runnable> runnables = new ArrayList<Runnable>();
            runnables.add(new HttpGetThread1(hand, urlXJCount, "XJ"));
            runnables.add(new HttpGetThread1(hand, urlXSCount, "XS"));
            runnables.add(new HttpGetThread1(hand, urlQXCount, "QX"));
            ThreadPoolUtils1.executeBatch(runnables);
//			ThreadPoolUtils.execute(new HttpGetThread(hand, url));
        }
    }

    /**
     * 返回信息处理
     */
    Handler hand = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if (msg.what == 404) {
                Log.i(TAG, "请求失败，服务器故障");
            } else if (msg.what == 2001) {//巡检未完成数量的请求
                String result = (String) msg.obj;
                if (result != null) {
                    RMessage r = GsonTools.getObject(result, RMessage.class);
                    String info = r.getMsg().toString();
                    count = Integer.parseInt(info);
                    if (count > 0) {
                        inspection_count.setVisibility(View.VISIBLE);
                        inspection_count.setText("" + count);
                    } else {
                        inspection_count.setVisibility(View.GONE);
                    }
                }
            } else if (msg.what == 2002) {//巡视未完成数量的请求
                String result = (String) msg.obj;
                if (result != null) {
                    RMessage r = GsonTools.getObject(result, RMessage.class);
                    String info = r.getMsg().toString();
                    count = Integer.parseInt(info);
                    if (count > 0) {
                        xs_count.setVisibility(View.VISIBLE);
                        xs_count.setText("" + count);
                    } else {
                        xs_count.setVisibility(View.GONE);
                    }
                }
            } else if (msg.what == 2003) {//抢修未完成数量的请求
                String result = (String) msg.obj;
                if (result != null) {
                    RMessage r = GsonTools.getObject(result, RMessage.class);
                    String info = r.getMsg().toString();
                    count = Integer.parseInt(info);
                    if (count > 0) {
                        qx_count.setVisibility(View.VISIBLE);
                        qx_count.setText("" + count);
                    } else {
                        qx_count.setVisibility(View.GONE);
                    }
                }
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 3://从抢修Activity跳回
                if (resultCode == Activity.RESULT_OK) {
                    //从缓存中取usercode
                    SharedPreferences sp = getActivity().getSharedPreferences("xunjian", Context.MODE_PRIVATE);
                    String usercode = sp.getString("usercode", "");
//					String usercode = "liping";
                    if (usercode != null && !"".equals(usercode)) {
//						String urlQXCount = "http://wxeis.eis.swdnkj.com/hrkweb/rest/patrol/getQXcount?usercode=" + usercode;
//                        String urlQXCount = "http://dsm.swdnkj.com:9080/EIS/rest/patrol/getQXcount?usercode=" + usercode;
                        String urlQXCount = "http://61.177.76.218:8090/boyuan/rest/patrol/getQXcount?usercode=" + usercode;
                        ThreadPoolUtils1.execute(new HttpGetThread1(hand, urlQXCount, "QX"));
                    }
                }
                break;
            case 2://从巡视Activity跳回
                if (resultCode == Activity.RESULT_OK) {
                    if (resultCode == Activity.RESULT_OK) {
                        //从缓存中取usercode
                        SharedPreferences sp = getActivity().getSharedPreferences("xunjian", Context.MODE_PRIVATE);
                        String usercode = sp.getString("usercode", "");
                        if (usercode != null && !"".equals(usercode)) {
//							String urlXSCount = "http://wxeis.eis.swdnkj.com/hrkweb/rest/patrol/getbdxscount?usercode="+ usercode;
//                            String urlXSCount = "http://dsm.swdnkj.com:9080/EIS/rest/patrol/getbdxscount?usercode=" + usercode;
                            String urlXSCount = "http://61.177.76.218:8090/boyuan/rest/patrol/getbdxscount?usercode=" + usercode;
                            ThreadPoolUtils1.execute(new HttpGetThread1(hand, urlXSCount, "XS"));
                        }
                    }
                }
                break;
            case 1://从巡检Activity跳回
                if (resultCode == Activity.RESULT_OK) {
                    if (resultCode == Activity.RESULT_OK) {
                        //从缓存中取usercode
                        SharedPreferences sp = getActivity().getSharedPreferences("xunjian", Context.MODE_PRIVATE);
                        String usercode = sp.getString("usercode", "");
                        if (usercode != null && !"".equals(usercode)) {
//							String urlXJCount = "http://wxeis.eis.swdnkj.com/hrkweb/rest/patrol/getInsCount?usercode="+usercode;
//                            String urlXJCount = "http://dsm.swdnkj.com:9080/EIS/rest/patrol/getInsCount?usercode=" + usercode;
                            String urlXJCount = "http://61.177.76.218:8090/boyuan/rest/patrol/getInsCount?usercode=" + usercode;
                            ThreadPoolUtils1.execute(new HttpGetThread1(hand, urlXJCount, "XJ"));
                        }
                    }
                }
                break;
            default:
                break;
        }
    }
}
