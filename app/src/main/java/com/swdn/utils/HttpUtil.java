package com.swdn.utils;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lenovo on 2016/11/9.
 */

public class HttpUtil {
    private static final int TIMEOUT_IN_MILLIONS = 5000;

    /**
     * 发送HTTP的get请求
     * @param address
     * @param listener
     */
    public static void sendHttpGetRequest(final String address,final HttpCallbackListener listener) {
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

                    if (listener != null) {
                        //回调onFinish()方法
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        listener.onError(e);
                    }
                }finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private static PrintWriter out;
    private static InputStream inStream;
    /**
     * 通过HttpURLConnection模拟post表单提交
     *提交格式为json字符串
     * @param path
     * @param param
     * @return
     * @throws Exception
     */
    public static void sendHttpPostRequest(final Handler handler, final String path, final String param){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    // 设置通用的请求属性
                    conn.setRequestProperty("accept", "*/*");
                    conn.setRequestProperty("connection", "Keep-Alive");
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type",
                            "application/x-www-form-urlencoded");
                    conn.setRequestProperty("charset", "utf-8");
                    conn.setUseCaches(false);
                    // 发送POST请求必须设置如下两行
                    conn.setDoOutput(true);// 是否输出参数
                    conn.setDoInput(true);
                    conn.setReadTimeout(TIMEOUT_IN_MILLIONS);
                    conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);
                    if (param != null && !param.trim().equals("")) {
                        // 获取URLConnection对象对应的输出流
                        out = new PrintWriter(conn.getOutputStream());
                        // 发送请求参数
                        out.print(param);
                        // flush输出流的缓冲
                        out.flush();
                    }
                    Message message = new Message();
                    message.what = 1;
                    InputStream inStream = conn.getInputStream();
                    String response = StreamTool.readInputStream(inStream);
                    message.obj = response;
                    handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                        if (inStream != null) {
                            inStream.close();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 从网络中获取图片，以字节数组的形式返回
     */


}

class StreamTool {
    /**
     * 从输入流中读取数据
     * @param inStream
     * @return
     * @throws Exception
     */
    public static String readInputStream(InputStream inStream) throws Exception{
        StringBuilder data = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
        String line;
        while ((line = reader.readLine()) != null) {
            data.append(line);
        }
        return data.toString();
    }


}
