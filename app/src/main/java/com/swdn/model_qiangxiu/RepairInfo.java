package com.swdn.model_qiangxiu;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lenovo on 2016/10/21.
 */

public class RepairInfo implements Parcelable{
    /*---------------------------抢修单的概要信息------------------------------------*/
    //抢修单ID
    private int id;
    //告警ID，外键ID,通过此id可查询出该抢修任务对应的告警信息
    private int alarmId;
    //任务编码
    private String taskCode;
    //负责人
    private  String manager;
    //故障类别
    private String category;
    //故障地点
    private String address;
    //接单状态：等待接单(0)、等待处理(1)
    private int receiveState;
    //经度
    private double lon;
    //纬度
    private double lat;

    /*-------------------------------------------------*/
    //接单人(根据接单状态判断是否显示)
    private String receiver;
    //接单时间(根据接单状态判断是否显示)(yyyy-MM-dd hh:mm:ss)
    private String receiveDate;

    /*----------------------------抢修单补充和回填信息-----------------------------------*/
    //班组信息
    private String groupInfo;
    //处理状态:已处理(1)、未处理(0)
    private int processState;

    /*---------回填---------*/
    //到场时间
    private String arrivalTime;
    //结束时间
    private String endTime;
    //抢修人员
    private String repairPersons;
    //故障原因
    private String failureCause;
    //执行情况
    private String executeSituation;
    //备注
    private String remarks;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getReceiveState() {
        return receiveState;
    }

    public void setReceiveState(int receiveState) {
        this.receiveState = receiveState;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(String receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getGroupInfo() {
        return groupInfo;
    }

    public void setGroupInfo(String groupInfo) {
        this.groupInfo = groupInfo;
    }

    public int getProcessState() {
        return processState;
    }

    public void setProcessState(int processState) {
        this.processState = processState;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRepairPersons() {
        return repairPersons;
    }

    public void setRepairPersons(String repairPersons) {
        this.repairPersons = repairPersons;
    }

    public String getFailureCause() {
        return failureCause;
    }

    public void setFailureCause(String failureCause) {
        this.failureCause = failureCause;
    }

    public String getExecuteSituation() {
        return executeSituation;
    }

    public void setExecuteSituation(String executeSituation) {
        this.executeSituation = executeSituation;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(alarmId);
        dest.writeString(manager);
        dest.writeString(category);
        dest.writeString(address);
        dest.writeString(taskCode);
        dest.writeInt(processState);
        dest.writeInt(receiveState);
        dest.writeDouble(lon);
        dest.writeDouble(lat);
        dest.writeString(groupInfo);
        dest.writeString(receiver);
        dest.writeString(receiveDate);
        dest.writeString(arrivalTime);
        dest.writeString(endTime);
        dest.writeString(repairPersons);
        dest.writeString(failureCause);
        dest.writeString(executeSituation);
        dest.writeString(remarks);
    }

    public static final Creator<RepairInfo> CREATOR = new Creator<RepairInfo>() {
        @Override
        public RepairInfo createFromParcel(Parcel in) {
            RepairInfo ri = new RepairInfo();
            ri.id = in.readInt();
            ri.alarmId = in.readInt();
            ri.manager = in.readString();
            ri.category = in.readString();
            ri.address = in.readString();
            ri.taskCode = in.readString();
            ri.processState = in.readInt();
            ri.receiveState = in.readInt();
            ri.lon = in.readDouble();
            ri.lat = in.readDouble();
            ri.groupInfo = in.readString();
            ri.receiver = in.readString();
            ri.receiveDate = in.readString();
            ri.arrivalTime = in.readString();
            ri.endTime = in.readString();
            ri.repairPersons = in.readString();
            ri.failureCause = in.readString();
            ri.executeSituation = in.readString();
            ri.remarks = in.readString();
            return ri;
        }

        @Override
        public RepairInfo[] newArray(int size) {
            return new RepairInfo[size];
        }
    };

}
