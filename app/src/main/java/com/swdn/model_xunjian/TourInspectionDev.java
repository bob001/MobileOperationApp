package com.swdn.model_xunjian;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lenovo on 2016/10/7.
 */

public class TourInspectionDev implements Parcelable {
    private int id;
    private String devId;
    private String devName;
    private String devType;
    private String location;
    private String pretourKey;
    private String remarks;
    private int taskId;//外键
    //回填信息
    private String tourDate;
    private String tourPerson;
    private String tourKey;
    private String tourEnd;
    private String tourRemarks;

    //加一个回填状态
    private int isCommited;

    public int getIsCommited() {
        return isCommited;
    }

    public void setIsCommited(int isCommited) {
        this.isCommited = isCommited;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPretourKey() {
        return pretourKey;
    }

    public void setPretourKey(String pretourKey) {
        this.pretourKey = pretourKey;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTourDate() {
        return tourDate;
    }

    public void setTourDate(String tourDate) {
        this.tourDate = tourDate;
    }

    public String getTourPerson() {
        return tourPerson;
    }

    public void setTourPerson(String tourPerson) {
        this.tourPerson = tourPerson;
    }

    public String getTourKey() {
        return tourKey;
    }

    public void setTourKey(String tourKey) {
        this.tourKey = tourKey;
    }

    public String getTourEnd() {
        return tourEnd;
    }

    public void setTourEnd(String tourEnd) {
        this.tourEnd = tourEnd;
    }

    public String getTourRemarks() {
        return tourRemarks;
    }

    public void setTourRemarks(String tourRemarks) {
        this.tourRemarks = tourRemarks;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(devId);
        parcel.writeString(devName);
        parcel.writeString(devType);
        parcel.writeString(location);
        parcel.writeString(pretourKey);
        parcel.writeString(remarks);
        parcel.writeInt(taskId);
        parcel.writeString(tourDate);
        parcel.writeString(tourPerson);
        parcel.writeString(tourKey);
        parcel.writeString(tourEnd);
        parcel.writeString(tourRemarks);
        parcel.writeInt(isCommited);
    }

    public static final Parcelable.Creator<TourInspectionDev> CREATOR = new Parcelable.Creator <TourInspectionDev>(){

        @Override
        public TourInspectionDev createFromParcel(Parcel parcel) {
            TourInspectionDev tiDev = new TourInspectionDev();
            tiDev.id = parcel.readInt();
            tiDev.devId = parcel.readString();
            tiDev.devName = parcel.readString();
            tiDev.devType = parcel.readString();
            tiDev.location = parcel.readString();
            tiDev.pretourKey = parcel.readString();
            tiDev.remarks = parcel.readString();
            tiDev.taskId = parcel.readInt();
            tiDev.tourDate = parcel.readString();
            tiDev.tourPerson = parcel.readString();
            tiDev.tourKey = parcel.readString();
            tiDev.tourEnd = parcel.readString();
            tiDev.tourRemarks = parcel.readString();
            tiDev.isCommited = parcel.readInt();
            return tiDev;
        }

        @Override
        public TourInspectionDev[] newArray(int i) {
            return new TourInspectionDev[i];
        }
    };
}
