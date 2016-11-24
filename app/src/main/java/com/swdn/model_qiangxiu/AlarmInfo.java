package com.swdn.model_qiangxiu;

/**
 * Created by lenovo on 2016/10/21.
 */

public class AlarmInfo {
    //告警id
    private int id;
    //报警设备位置
    private String alarmDevLoc;
    //报警内容
    private String alarmContent;
    //报警类型
    private String alarmType;
    //报警发生时间
    private String alarmOccurtime;
    //报警级别 -- 重大报警：3 ；一般告警：4； 注意：5；  较重大报警：2  特重大报警：1
    private String alarmLevel;
    //处理标志 1:已处理  0.未处理
    private int processFlag;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAlarmDevLoc() {
        return alarmDevLoc;
    }

    public void setAlarmDevLoc(String alarmDevLoc) {
        this.alarmDevLoc = alarmDevLoc;
    }

    public String getAlarmContent() {
        return alarmContent;
    }

    public void setAlarmContent(String alarmContent) {
        this.alarmContent = alarmContent;
    }

    public String getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    public String getAlarmOccurtime() {
        return alarmOccurtime;
    }

    public void setAlarmOccurtime(String alarmOccurtime) {
        this.alarmOccurtime = alarmOccurtime;
    }

    public String getAlarmLevel() {
        return alarmLevel;
    }

    public void setAlarmLevel(String alarmLevel) {
        this.alarmLevel = alarmLevel;
    }

    public int getProcessFlag() {
        return processFlag;
    }

    public void setProcessFlag(int processFlag) {
        this.processFlag = processFlag;
    }

}
