package com.swdn.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtil1 {
	private static final int TIMEOUT_IN_MILLIONS = 5000;  
	/**
	 * 通过get方法获取服务器数据
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String get(String url) throws Exception {
		StringBuffer buffer = new StringBuffer();
		// 建立HTTP Get联机
		HttpGet httpRequest = new HttpGet(url);
		// 取得默认的HttpClient
		HttpClient httpClient = new DefaultHttpClient();
		
		// 设置网络超时参数
		HttpParams params = httpClient.getParams();		
		HttpConnectionParams.setConnectionTimeout(params, 1000);
		HttpConnectionParams.setSoTimeout(params, 5000);

		// 取得HttpResponse
		HttpResponse response = httpClient.execute(httpRequest);
		HttpEntity entity = response.getEntity();
		// 若状态码为200,表示连接成功
		if (response.getStatusLine().getStatusCode() == 200) {
			if (entity != null) {
				// entity.getContent()取得返回的字符串
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(entity.getContent()));
				String line = null;
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
				}
			}
		}
		return buffer.toString();
	}

	/**
	 * 通过post获取服务器方法
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String post(String url,Map<String,String> rawParams) throws Exception {
		StringBuffer buffer = new StringBuffer();
		// 建立HTTP Post联机
		HttpPost httpRequest = new HttpPost(url);
		// 取得默认的HttpClient
		HttpClient httpClient = new DefaultHttpClient();

		 // 如果传递参数个数比较多的话可以对传递的参数进行封装
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (String key : rawParams.keySet()) {
            // 封装请求参数
            params.add(new BasicNameValuePair(key, rawParams.get(key)));
        }
        // 设置请求参数
        httpRequest.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        
		// 设置网络超时参数
        HttpParams httpParams = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 1000);
		HttpConnectionParams.setSoTimeout(httpParams, 5000);

		// 取得HttpResponse
		HttpResponse response = httpClient.execute(httpRequest);
		HttpEntity entity = response.getEntity();
		// 若状态码为200,表示连接成功
		if (response.getStatusLine().getStatusCode() == 200) {
			if (entity != null) {
				// entity.getContent()取得返回的字符串
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(entity.getContent()));
				String line = null;
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
				}
			}
		}
		return buffer.toString();
	}
	
	/**
	 * 通过post获取服务器方法
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String post(String url,String rawParams) throws Exception {
		StringBuffer buffer = new StringBuffer();
		// 建立HTTP Post联机
		HttpPost httpRequest = new HttpPost(url);
		// 取得默认的HttpClient
		HttpClient httpClient = new DefaultHttpClient();

		 // 如果传递参数个数比较多的话可以对传递的参数进行封装
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // 封装请求参数
        params.add(new BasicNameValuePair("paramJson", rawParams));
        // 设置请求参数
        httpRequest.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        
		// 设置网络超时参数
        HttpParams httpParams = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 1000);
		HttpConnectionParams.setSoTimeout(httpParams, 5000);

		// 取得HttpResponse
		HttpResponse response = httpClient.execute(httpRequest);
		HttpEntity entity = response.getEntity();
		// 若状态码为200,表示连接成功
		if (response.getStatusLine().getStatusCode() == 200) {
			if (entity != null) {
				// entity.getContent()取得返回的字符串
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(entity.getContent()));
				String line = null;
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
				}
			}
		}
		return buffer.toString();
	}
	   /** 
     * Get请求，获得返回数据 
     *  
     * @param urlStr 
     * @return 
     * @throws Exception 
     */  
    public static String doGet(String urlStr)   
    {  
        URL url = null;  
        HttpURLConnection conn = null;  
        InputStream is = null;  
        ByteArrayOutputStream baos = null;  
        try  
        {  
            url = new URL(urlStr);  
            conn = (HttpURLConnection) url.openConnection();  
            conn.setReadTimeout(TIMEOUT_IN_MILLIONS);  
            conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);  
            conn.setRequestMethod("GET");  
            conn.setRequestProperty("accept", "*/*");  
            conn.setRequestProperty("connection", "Keep-Alive");  
            if (conn.getResponseCode() == 200)
            {
                is = conn.getInputStream();  
                baos = new ByteArrayOutputStream();  
                int len = -1;  
                byte[] buf = new byte[128];  
  
                while ((len = is.read(buf)) != -1)  
                {  
                    baos.write(buf, 0, len);  
                }  
                baos.flush();  
                return baos.toString();  
            } else
            {
                throw new RuntimeException(" responseCode is not 200 ... ");
            }
  
        } catch (Exception e)  
        {  
            e.printStackTrace();  
        } finally  
        {  
            try  
            {  
                if (is != null)  
                    is.close();  
            } catch (IOException e)  
            {  
            }  
            try  
            {  
                if (baos != null)  
                    baos.close();  
            } catch (IOException e)  
            {  
            }  
            conn.disconnect();  
        }  
          
        return null ;  
  
    }  
  
    /**  
     * 向指定 URL 发送POST方法的请求  
     *   
     * @param url  
     *            发送请求的 URL  
     * @param param  
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。  
     * @return 所代表远程资源的响应结果  
     * @throws Exception  
     */  
    public static String doPost(String url, String param)   
    {  
        PrintWriter out = null;  
        BufferedReader in = null;  
        String result = "";  
        try  
        {  
            URL realUrl = new URL(url);  
            // 打开和URL之间的连接  
            HttpURLConnection conn = (HttpURLConnection) realUrl  
                    .openConnection();  
            // 设置通用的请求属性  
            conn.setRequestProperty("accept", "*/*");  
            conn.setRequestProperty("connection", "Keep-Alive");  
            conn.setRequestMethod("POST");  
            conn.setRequestProperty("Content-Type",  
                    "application/x-www-form-urlencoded");  
            conn.setRequestProperty("charset", "utf-8");  
            conn.setUseCaches(false);  
            // 发送POST请求必须设置如下两行  
            conn.setDoOutput(true);  
            conn.setDoInput(true);  
            conn.setReadTimeout(TIMEOUT_IN_MILLIONS);  
            conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);  
  
            if (param != null && !param.trim().equals(""))  
            {  
                // 获取URLConnection对象对应的输出流  
                out = new PrintWriter(conn.getOutputStream());  
                // 发送请求参数  
                out.print(param);  
                // flush输出流的缓冲  
                out.flush();  
            }  
            // 定义BufferedReader输入流来读取URL的响应  
            in = new BufferedReader(  
                    new InputStreamReader(conn.getInputStream()));  
            String line;  
            while ((line = in.readLine()) != null)  
            {  
                result += line;  
            }  
        } catch (Exception e)  
        {  
            e.printStackTrace();  
        }  
        // 使用finally块来关闭输出流、输入流  
        finally  
        {  
            try  
            {  
                if (out != null)  
                {  
                    out.close();  
                }  
                if (in != null)  
                {  
                    in.close();  
                }  
            } catch (IOException ex)  
            {  
                ex.printStackTrace();  
            }  
        }  
        return result;  
    }  
}  
