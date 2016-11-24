package com.swdn.model_qiangxiu;

/**
 * Created by lenovo on 2016/10/21.
 */

public class AlarmInfo {
    //告警id
    private int id;
    //报警位置
    private String alarmAddress;
    //报警
    private String alarm;
    //报警状态
    private String alarmState;
    //报警内容
    private String alarmContent;
    //当前值
    private int currentValue;
    //处理标志
    private String handlingMark;
    //发生时间
    private String occurTime;
    //事件类型--考虑是否要通过这个属性来确定需要携带的工具
    private String eventType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAlarmAddress() {
        return alarmAddress;
    }

    public void setAlarmAddress(String alarmAddress) {
        this.alarmAddress = alarmAddress;
    }

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }

    public String getAlarmState() {
        return alarmState;
    }

    public void setAlarmState(String alarmState) {
        this.alarmState = alarmState;
    }

    public String getAlarmContent() {
        return alarmContent;
    }

    public void setAlarmContent(String alarmContent) {
        this.alarmContent = alarmContent;
    }

    public int getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }

    public String getHandlingMark() {
        return handlingMark;
    }

    public void setHandlingMark(String handlingMark) {
        this.handlingMark = handlingMark;
    }

    public String getOccurTime() {
        return occurTime;
    }

    public void setOccurTime(String occurTime) {
        this.occurTime = occurTime;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
