package com.swdn.log;

/**
 * Created by zhaoxuan.li on 2015/9/17.
 */
public class Log {
    /**
     * Log输出机制：  LogLevel：Log输出级别控制
     * if（ 要输出的日志级别  >= LogLevel ）
     *      输出；
     * else
     *      不输出；
     *
     * Log上传和保存机制：
     * if（UploadSwitch == false）
     *      什么都不做
     * else
     *      if  （调用Log上传方法）
     *           上传并保存
     *      else
     *          if（flag>=LogConfig.UploadLevel）
     *                  上传
     *          if(flag>=LogConfig.SaveFileLevel)
     *                  保存
     *
     */

    /**
     * 打印 VERBOSE 信息  4个重载方法
     * @param tagString   Tag标志
     * @param explainString  描述信息
     * @param throwable  异常信息
     * @param isUpLoad   是否上传日志
     */
    public static void v(String tagString , String explainString ,Throwable throwable, boolean isUpLoad){
        println(LogConfig.VERBOSE, tagString, explainString,throwable,isUpLoad);
    }

    public static void v(String tagString , String explainString){
        println(LogConfig.VERBOSE, tagString, explainString,false);
    }

    public static void v(String tagString , String explainString , boolean isUpLoad){
        println(LogConfig.VERBOSE, tagString, explainString,isUpLoad);
    }

    public static void v(String tagString , String explainString,Throwable throwable){
        println(LogConfig.VERBOSE, tagString, explainString,throwable,false);
    }

    /**
     * 打印 DEBUG 信息  4个重载方法
     * @param tagString   Tag标志
     * @param explainString  描述信息
     * @param throwable  异常信息
     * @param isUpLoad   是否上传日志
     */
    public static void d(String tagString , String explainString ,Throwable throwable, boolean isUpLoad){
        println(LogConfig.DEBUG, tagString, explainString,throwable,isUpLoad);
    }

    public static void d(String tagString , String explainString){
        println(LogConfig.DEBUG, tagString, explainString,false);
    }

    public static void d(String tagString , String explainString , boolean isUpLoad){
        println(LogConfig.DEBUG, tagString, explainString,isUpLoad);
    }

    public static void d(String tagString , String explainString,Throwable throwable){
        println(LogConfig.DEBUG, tagString, explainString,throwable,false);
    }
    /**
     * 打印 INFO 信息  4个重载方法
     * @param tagString   Tag标志
     * @param explainString  描述信息
     * @param throwable  异常信息
     * @param isUpLoad   是否上传日志
     */
    public static void i(String tagString , String explainString ,Throwable throwable, boolean isUpLoad){
        println(LogConfig.INFO, tagString, explainString,throwable,isUpLoad);
    }

    public static void i(String tagString , String explainString){
        println(LogConfig.INFO, tagString, explainString,false);
    }

    public static void i(String tagString , String explainString , boolean isUpLoad){
        println(LogConfig.INFO, tagString, explainString,isUpLoad);
    }

    public static void i(String tagString , String explainString,Throwable throwable){
        println(LogConfig.INFO, tagString, explainString,throwable,false);
    }

    /**
     * 打印 WARN 信息  4个重载方法
     * @param tagString   Tag标志
     * @param explainString  描述信息
     * @param throwable  异常信息
     * @param isUpLoad   是否上传日志
     */
    public static void w(String tagString , String explainString ,Throwable throwable, boolean isUpLoad){
        println(LogConfig.WARN, tagString, explainString,throwable,isUpLoad);
    }

    public static void w(String tagString , String explainString){
        println(LogConfig.WARN, tagString, explainString,false);
    }

    public static void w(String tagString , String explainString , boolean isUpLoad){
        println(LogConfig.WARN, tagString, explainString,isUpLoad);
    }

    public static void w(String tagString , String explainString,Throwable throwable){
        println(LogConfig.WARN, tagString, explainString,throwable,false);
    }
    /**
     * 打印 ERROR 信息    4个重载方法
     * @param tagString   Tag标志
     * @param explainString  描述信息
     * @param throwable  异常信息
     * @param isUpLoad   是否上传日志
     */
    public static void e(String tagString , String explainString ,Throwable throwable, boolean isUpLoad){
        println(LogConfig.ERROR, tagString, explainString,throwable,isUpLoad);
    }

    public static void e(String tagString , String explainString){
        println(LogConfig.ERROR, tagString, explainString,false);
    }

    public static void e(String tagString , String explainString , boolean isUpLoad){
        println(LogConfig.ERROR, tagString, explainString,isUpLoad);
    }

    public static void e(String tagString , String explainString,Throwable throwable){
        println(LogConfig.ERROR, tagString, explainString,throwable,false);
    }





    /**
     * 判断输出级别，决定是否输出
     * @param flag   日志级别
     * @param explainString   说明信息
     */
    private static void println(int flag ,String tagString,String explainString,boolean isUpLoad){
        if(flag>=LogConfig.LogLevel){    //符合输出级别，输出
            LogPrint.println(flag, tagString, explainString);
        }
        if(LogConfig.UploadSwitch){   //上传开关已打开
            if(isUpLoad){   //调用上传方法
                LogPrint.saveLogToFile(flag,explainString);
              //  LogPrint.uploadToServer(flag, explainString);
            }else{
                if(flag>=LogConfig.UploadLevel ) {   //符合上传级别
                    LogPrint.saveLogToFile(flag,explainString);
                }
                if(flag>=LogConfig.SaveFileLevel){   //符合保存级别
                    LogPrint.uploadToServer(flag,explainString);
                }
            }
        }
    }

    /**
     * 重载方法 ，判断输出级别，决定是否输出，增加异常信息输出
     * @param flag   日志级别
     * @param explainString   说明信息
     * @param throwable   异常对象
     */
    private static void println(int flag ,String tagString,String explainString,Throwable throwable ,boolean isUpLoad){
        if(flag>=LogConfig.LogLevel){    //符合输出级别，输出
            LogPrint.println(flag, tagString, explainString,throwable);
        }
        if(LogConfig.UploadSwitch){   //上传开关已打开
            if(isUpLoad){   //调用上传方法
                LogPrint.saveLogToFile(flag,explainString,throwable);
                LogPrint.uploadToServer(flag, explainString,throwable);
            }else{
                if(flag>=LogConfig.UploadLevel ) {   //符合上传级别
                    LogPrint.saveLogToFile(flag,explainString,throwable);
                }
                if(flag>=LogConfig.SaveFileLevel){   //符合保存级别
                    LogPrint.uploadToServer(flag,explainString,throwable);
                }
            }
        }
    }












}
