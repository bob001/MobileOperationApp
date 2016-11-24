package com.swdn.log;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhaoxuan.li on 2015/9/17.
 */
public class LogPrint  {
    /** Log日志的tag String : TAG */
    private static final String TAG = LogPrint.class.getSimpleName();
    /**时间格式**/
    private static final SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**设备信息**/
    private static StringBuilder deviceMessage = null;

    /**
     * 拼接异常对象成字符串
     * @param throwable  异常对象
     * @return
     */
    protected static StringBuilder createStackTrace(Throwable throwable){
        StringBuilder builder = new StringBuilder();
        if(throwable == null)
            return builder;
        builder.append("\n        "+throwable.getClass()+":  "+throwable.getMessage());
        int length = throwable.getStackTrace().length;
        for(int i =0;i<length;i++){
            builder.append("\n\t\t"+throwable.getStackTrace()[i]);
        }
        return builder;
    }

    /**
     * 无异常对象情况下输出Log
     * @param logLevel
     * @param tagString
     * @param explainString
     */
    protected  static void println(int logLevel, String tagString ,String explainString){
        Log.println(logLevel, tagString, explainString);
    }

    /**
     * 有异常对象情况下输出Log
     * @param logLevel
     * @param tagString
     * @param explainString
     * @param throwable
     */
    protected  static void println(int logLevel, String tagString ,String explainString,Throwable throwable){
        switch (logLevel){
            case LogConfig.VERBOSE:
                Log.v(tagString,explainString,throwable); break;
            case LogConfig.DEBUG:
                Log.d(tagString,explainString,throwable); break;
            case LogConfig.INFO:
                Log.i(tagString,explainString,throwable); break;
            case LogConfig.WARN:
                Log.w(tagString,explainString,throwable); break;
            case LogConfig.ERROR:
                Log.e(tagString, explainString, throwable); break;
        }
    }

    /**
     * 生成上传服务器日志格式
     */
    protected static String createMessageToServer(Throwable throwable){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("time" + "=" + mSimpleDateFormat.format(new Date()) + "\r\n");
        //stringBuilder.append(getDeviceInfo(paramContext));

        if(throwable==null)
            return stringBuilder.toString();

        Writer mWriter = new StringWriter();
        PrintWriter mPrintWriter = new PrintWriter(mWriter);
        throwable.printStackTrace(mPrintWriter);
        // paramThrowable.printStackTrace();
        Throwable mThrowable = throwable.getCause();
        // 迭代栈队列把所有的异常信息写入writer中
        while (mThrowable != null) {
            mThrowable.printStackTrace(mPrintWriter);
            // 换行 每个个异常栈之间换行
            mPrintWriter.append("\r\n");
            mThrowable = mThrowable.getCause();
        }
        // 记得关闭
        mPrintWriter.close();
        String mResult = mWriter.toString();
        stringBuilder.append(mResult);
        return stringBuilder.toString();
    }


    protected static StringBuilder getDeviceInfo(Context paramContext){
        //设备信息为空时，读取设备信息
        if(deviceMessage==null||deviceMessage.equals("")){
            deviceMessage = new StringBuilder();
            try {
                // 获得包管理器
                PackageManager mPackageManager = paramContext.getPackageManager();
                // 得到该应用的信息，即主Activity
                PackageInfo mPackageInfo = mPackageManager.getPackageInfo(paramContext.getPackageName(), PackageManager.GET_ACTIVITIES);
                if (mPackageInfo != null) {
                    String versionName = mPackageInfo.versionName == null ? "null" : mPackageInfo.versionName;
                    String versionCode = mPackageInfo.versionCode + "";
                    deviceMessage.append("versionName" + "=" + versionName + "\r\n");
                    deviceMessage.append("versionCode" + "=" + versionCode + "\r\n");
                }
                //mLogInfo.put("ENV_CODE", PropertiesUtils.get(paramContext, "CURRENT_ENV_CODE"));
            } catch (PackageManager.NameNotFoundException e) {
                com.swdn.log.Log.e(TAG, "获取版本信息出错", e);
            }

            Field[] mFields = Build.class.getDeclaredFields();
            // 迭代Build的字段key-value 此处的信息主要是为了在服务器端手机各种版本手机报错的原因
            for (Field field : mFields) {
                try {
                    field.setAccessible(true);
                    if(field.getName().equals("BRAND")||field.getName().equals("DEVICE")||
                            field.getName().equals("MODEL")){
                        deviceMessage.append(field.getName() + "=" + field.get("").toString() + "\r\n");
                    }
                } catch (Exception e) {
                	com.swdn.log.Log.e(TAG, "获取设备信息出错", e);
                }
            }
        }

        if(deviceMessage!=null)
            return deviceMessage;
        else
            return new StringBuilder();
    }


    /**
     * 保存日志到文件
     * @param flag
     * @param explainString
     */
    protected static void saveLogToFile(int flag,String explainString){
        StringBuilder message = new StringBuilder();
        message.append("time" + "=" + mSimpleDateFormat.format(new Date()) + "\r\n");
        message.append(LogConfig.getLogLevelName(flag)+"------"+explainString+"\n");

        /*-----------------------------以下调用 具体保存本地方法---------------------*/
        /*-----------------------------不同应用保存方式不用，此处不具体写---------------------*/
        Log.println(LogConfig.INFO, "Log保存到文件", message.toString());
    }
    /**
     * 保存日志到文件
     * @param flag
     * @param explainString
     */
    protected static void saveLogToFile(int flag,String explainString,Throwable throwable){
        //Context context = MyApplication.getInstence();
        StringBuilder message = new StringBuilder();
        message.append("time" + "=" + mSimpleDateFormat.format(new Date()) + "\r\n");
       // message.append(getDeviceInfo(context));
        message.append(LogConfig.getLogLevelName(flag)+"------"+explainString+"\n");
        message.append(createMessageToServer(throwable));


        /*-----------------------------以下调用 具体保存本地方法---------------------*/
        /*-----------------------------不同应用保存方式不用，此处不具体写---------------------*/
        Log.println(LogConfig.INFO, "Log保存到文件", message.toString());

    }

    /**
     * 上传日志到服务器
     * 当没有错误信息时，你需要考虑是否需要上传设备信息。
     * @param flag
     * @param explainString
     */
    protected static void uploadToServer(int flag,String explainString){
       // Context context = MyApplication.getInstence();
        StringBuilder message = new StringBuilder();
        message.append("time" + "=" + mSimpleDateFormat.format(new Date()) + "\r\n");
        //message.append(getDeviceInfo(context));  //是否需要上传设备信息呢
        message.append(LogConfig.getLogLevelName(flag)+"------"+explainString+"\n");

        /*-----------------------------以下调用 具体上传服务器方法---------------------*/
        /*-----------------------------不同应用上传方式不用，此处不具体写---------------------*/
        Log.println(LogConfig.INFO, "Log上传到服务器", message.toString());
    }
    /**
     * 上传日志到服务器
     * @param flag
     * @param explainString
     */
    protected static void uploadToServer(int flag,String explainString,Throwable throwable){
        //Context context = MyApplication.getInstence();
        StringBuilder message = new StringBuilder();
        message.append("time" + "=" + mSimpleDateFormat.format(new Date()) + "\r\n");
       // message.append(getDeviceInfo(context));
        message.append(LogConfig.getLogLevelName(flag)+"------"+explainString+"\n");
        message.append(createMessageToServer(throwable));

        /*-----------------------------以下调用 具体上传服务器方法---------------------*/
        /*-----------------------------不同应用上传方式不用，此处不具体写---------------------*/
        Log.println(LogConfig.INFO,"Log上传到服务器",message.toString());
    }
}
