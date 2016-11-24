package com.swdn.utils_xunjian;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;


import com.swdn.db_xunjian.TourInspectionDB;
import com.swdn.model_xunjian.TourInspectionDev;
import com.swdn.model_xunjian.TourInspectionTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by lenovo on 2016/10/7.
 */

public class Utility_qiu {

    /**
     * 查询当前网络状态
     */
    public static boolean getNetworkStatus(Activity activity) {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
//                    System.out.println(i + "===状态===" + networkInfo[i].getState());
//                    System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 将服务器返回的json字符串封装进tourInspectionList和tourInspectionDevList对象中
     *
     * @param tourInspectionList
     * @param tourInspectionDevList
     * @param jsonStr
     */
    public synchronized static void parseJsonStr(final List<TourInspectionTask> tourInspectionList,
                                                 final List<TourInspectionDev> tourInspectionDevList,
                                                 String jsonStr) {
        if (!TextUtils.isEmpty(jsonStr)) {
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                JSONArray dataArray = jsonObject.getJSONArray("msg");
                for (int i = 0; i < dataArray.length(); i++) {
                    TourInspectionTask tourInspectionTask = new TourInspectionTask();
                    JSONObject data = dataArray.getJSONObject(i);
                    int taskId = data.getInt("id");
                    tourInspectionTask.setId(taskId);
                    tourInspectionTask.setCategory(data.getString("xslx"));
                    tourInspectionTask.setWritePerson(data.getString("bzr"));
                    tourInspectionTask.setExecPerson(data.getString("yxr"));
                    tourInspectionTask.setExecDate(data.getString("bzsj"));
                    tourInspectionTask.setIsFinished(0);//暂时用0，目前字段中没有回填字段
                    tourInspectionTask.setDept(data.getString("bzbm"));
                    tourInspectionList.add(tourInspectionTask);
                    JSONArray dataDevArr = data.getJSONArray("xssb");
                    for (int j = 0; j < dataDevArr.length(); j++) {
                        JSONObject dataDev = dataDevArr.getJSONObject(j);
                        TourInspectionDev tourInspectionDev = new TourInspectionDev();
                        tourInspectionDev.setId(dataDev.getInt("id"));
                        tourInspectionDev.setDevId(dataDev.getString("dev_id"));
                        tourInspectionDev.setDevName(dataDev.getString("dev_name"));
                        tourInspectionDev.setDevType(dataDev.getString("dev_type"));
                        tourInspectionDev.setLocation(dataDev.getString("dev_loc"));
                        tourInspectionDev.setPretourKey(dataDev.getString("yxzd"));
                        tourInspectionDev.setRemarks(dataDev.getString("yxbz"));
                        tourInspectionDev.setTaskId(taskId);
                        tourInspectionDev.setTourDate(dataDev.getString("xsrq"));
                        tourInspectionDev.setTourPerson(dataDev.getString("xsr"));
                        tourInspectionDev.setTourKey(dataDev.getString("xszd"));
                        tourInspectionDev.setTourEnd(dataDev.getString("xsjg"));
                        tourInspectionDev.setTourRemarks(dataDev.getString("xsbz"));
                        tourInspectionDevList.add(tourInspectionDev);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 调用服务接口得到返回数据
     */
    public synchronized static void queryTasksFromServer(final Handler handler, final String address) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
//                    connection.setDoInput(true);
//                    connection.setDoOutput(true);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    Message message = new Message();
                    message.what = 1;
                    message.obj = response.toString();
                    //先将上句注释，然后使用固定数据调试
//                    String str = "{\"code\":1,\"msg\":[{\"id\":213,\"xslx\":\"计划\",\"yxr\":\"袁文浩\",\"bzr\":\"管理员\",\"bzbm\":\"苏文电能公司\",\"bzsj\":\"2016-09-28 14:56:27\",\"xssb\":[{\"id\":0,\"order\":0,\"dev_id\":167677,\"dev_name\":\"10kV进线111\",\"dev_type\":\"电力\",\"dev_loc\":\"苏文\",\"yxzd\":null,\"yxbz\":null,\"ycrq\":null,\"xsrq\":\"2016-09-28 14:54:32\",\"xszd\":\"无\",\"xsjg\":null,\"xsr\":null,\"xsbz\":null},{\"id\":0,\"order\":0,\"dev_id\":167677,\"dev_name\":\"变压器\",\"dev_type\":\"电力\",\"dev_loc\":\"无\",\"yxzd\":null,\"yxbz\":null,\"ycrq\":null,\"xsrq\":\"2016-09-28 15:31:24\",\"xszd\":\"无\",\"xsjg\":null,\"xsr\":null,\"xsbz\":\"无\"}]},{\"id\":215,\"xslx\":\"计划\",\"yxr\":\"袁文浩\",\"bzr\":\"管理员\",\"bzbm\":\"苏文电能公司\",\"bzsj\":\"2016-09-28 16:22:09\",\"xssb\":[{\"id\":0,\"order\":0,\"dev_id\":167677,\"dev_name\":\"无\",\"dev_type\":\"无\",\"dev_loc\":\"无\",\"yxzd\":null,\"yxbz\":null,\"ycrq\":null,\"xsrq\":\"2016-09-28 16:20:05\",\"xszd\":\"无\",\"xsjg\":null,\"xsr\":null,\"xsbz\":\"无\"}]}],\"url\":null}";
//                    message.obj = str;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    /**
     * 解析和处理服务器返回的json数据，并将解析出来的数据存储到数据库中
     */
    public synchronized static boolean handleResponse(TourInspectionDB tourInspectionDB, String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
//            JSONObject data = jsonObject.getJSONObject("msg");
                JSONArray dataArray = jsonObject.getJSONArray("msg");
                for (int i = 0; i < dataArray.length(); i++) {
                    TourInspectionTask tourInspectionTask = new TourInspectionTask();
                    JSONObject data = dataArray.getJSONObject(i);
                    int taskId = data.getInt("id");
                    tourInspectionTask.setId(taskId);
                    tourInspectionTask.setCategory(data.getString("xslx"));
                    tourInspectionTask.setWritePerson(data.getString("bzr"));
                    tourInspectionTask.setExecPerson(data.getString("yxr"));
                    tourInspectionTask.setExecDate(data.getString("bzsj"));
                    tourInspectionTask.setIsFinished(data.getInt("isComplish"));//暂时用0，目前后端字段中没有任务是否完成字段，需要添加
                    tourInspectionTask.setDept(data.getString("bzbm"));
                    tourInspectionDB.saveTourInspectionTask(tourInspectionTask);
                    JSONArray dataDevArr = data.getJSONArray("xssb");
                    for (int j = 0; j < dataDevArr.length(); j++) {
                        JSONObject dataDev = dataDevArr.getJSONObject(j);
                        TourInspectionDev tourInspectionDev = new TourInspectionDev();
                        tourInspectionDev.setId(dataDev.getInt("id"));
                        tourInspectionDev.setDevId(dataDev.getString("dev_id"));
                        tourInspectionDev.setDevName(dataDev.getString("dev_name"));
                        tourInspectionDev.setDevType(dataDev.getString("dev_type"));
                        tourInspectionDev.setLocation(dataDev.getString("dev_loc"));
                        tourInspectionDev.setPretourKey(dataDev.getString("yxzd"));
                        tourInspectionDev.setRemarks(dataDev.getString("yxbz"));
                        tourInspectionDev.setTaskId(taskId);
                        tourInspectionDev.setTourDate(dataDev.getString("xsrq"));
                        tourInspectionDev.setTourPerson(dataDev.getString("xsr"));
                        tourInspectionDev.setTourKey(dataDev.getString("xszd"));
                        tourInspectionDev.setTourEnd(dataDev.getString("xsjg"));
                        tourInspectionDev.setTourRemarks(dataDev.getString("xsbz"));
                        tourInspectionDev.setIsCommited(dataDev.getInt("htzt"));
                        tourInspectionDB.saveTourInspectionDev(tourInspectionDev);
                    }
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
