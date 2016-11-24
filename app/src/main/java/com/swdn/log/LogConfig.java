package com.swdn.log;

/**
 * Log 级别信息
 * Created by zhaoxuan.li on 2015/9/17.
 */
public class LogConfig {
    /*  Log输出级别 */
    protected static int LogLevel = LogConfig.DEBUG;

    protected static int UploadLevel = LogConfig.ERROR;
    protected static int SaveFileLevel = LogConfig.ERROR;
    protected static boolean UploadSwitch = true;


    public static final int NO_OUTPUT = 1;  //什么都不输出
    public static final int VERBOSE = 2;
    public static final int DEBUG = 3;
    public static final int INFO = 4;
    public static final int WARN =5;
    public static final int ERROR = 6;

    protected static final String DEFAULT_TAG = "COM.PATH";

    /**
     * 在赋值同时，做一些权限判断
     * @param level  要设定的Log输出级别
     */
    public static void setLogLevel(int level){
        LogLevel = level;
    }
    public static int getLogLevel(){
        return LogLevel;
    }
    public static void setUploadLevel(int level){
        UploadLevel = level;
    }
    public static int getUploadLevel(){
        return UploadLevel;
    }
    public static void setSaveFileLevel(int level){
        SaveFileLevel = level;
    }
    public static int getSaveFileLevel(){
        return SaveFileLevel;
    }



    public static String getLogLevelName(int flag){
        if(flag==-1)
            flag= LogLevel;
        switch (flag){
            case NO_OUTPUT:return "NO_OUTPUT";
            case VERBOSE:return "VERBOSE";
            case DEBUG:return "DEBUG";
            case INFO:return "INFO";
            case WARN:return "WARN";
            case ERROR:return "ERROR";
            default:return "NO_OUTPUT";
        }
    }


}
