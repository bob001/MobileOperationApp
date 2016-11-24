package com.swdn.model_xunshi;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lenovo on 2016/11/15.
 */

public class TourInfo implements Parcelable {
    //ID
    private int id;
    //巡视类型
    private String tourCategory;
    //巡视编号
    private String tourNum;
    //预巡人
    private String preTourPerson;
    //预巡日期
    private String preTourDate;
    //变电所ID
    private int bdsId;
    //变电所(巡视地点)
    private String bds;

    //接单人
    private String receivePerson;
    //接单时间
    private String receiveDate;
    //天气
    private String weather;
    //温度
    private String temperature;

    //是否接单
    private int isReceived;
    //是否完成
    private int isFinished;

    //-----------------回填信息---------------------
    //巡视开始时间
    private String tourStartTime;
    //巡视结束时间
    private String tourEndTime;
    //巡视人
    private String tourPerson;

    public String getTourPerson() {
        return tourPerson;
    }

    public void setTourPerson(String tourPerson) {
        this.tourPerson = tourPerson;
    }
    //巡视情况
    private String tourSituation;
    //备注
    private String remarks;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTourCategory() {
        return tourCategory;
    }

    public void setTourCategory(String tourCategory) {
        this.tourCategory = tourCategory;
    }

    public String getTourNum() {
        return tourNum;
    }

    public void setTourNum(String tourNum) {
        this.tourNum = tourNum;
    }

    public String getPreTourPerson() {
        return preTourPerson;
    }

    public void setPreTourPerson(String preTourPerson) {
        this.preTourPerson = preTourPerson;
    }

    public String getPreTourDate() {
        return preTourDate;
    }

    public void setPreTourDate(String preTourDate) {
        this.preTourDate = preTourDate;
    }

    public int getBdsId() {
        return bdsId;
    }

    public void setBdsId(int bdsId) {
        this.bdsId = bdsId;
    }

    public String getBds() {
        return bds;
    }

    public void setBds(String bds) {
        this.bds = bds;
    }

    public String getReceivePerson() {
        return receivePerson;
    }

    public void setReceivePerson(String receivePerson) {
        this.receivePerson = receivePerson;
    }

    public String getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(String receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public int getIsReceived() {
        return isReceived;
    }

    public void setIsReceived(int isReceived) {
        this.isReceived = isReceived;
    }

    public int getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(int isFinished) {
        this.isFinished = isFinished;
    }

    public String getTourStartTime() {
        return tourStartTime;
    }

    public void setTourStartTime(String tourStartTime) {
        this.tourStartTime = tourStartTime;
    }

    public String getTourEndTime() {
        return tourEndTime;
    }

    public void setTourEndTime(String tourEndTime) {
        this.tourEndTime = tourEndTime;
    }

    public String getTourSituation() {
        return tourSituation;
    }

    public void setTourSituation(String tourSituation) {
        this.tourSituation = tourSituation;
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
        dest.writeString(tourCategory);
        dest.writeString(tourNum);
        dest.writeString(preTourPerson);
        dest.writeString(preTourDate);
        dest.writeInt(bdsId);
        dest.writeString(bds);
        dest.writeString(receivePerson);
        dest.writeString(receiveDate);
        dest.writeString(weather);
        dest.writeString(temperature);
        dest.writeInt(isReceived);
        dest.writeInt(isFinished);
        dest.writeString(tourStartTime);
        dest.writeString(tourEndTime);
        dest.writeString(tourPerson);
        dest.writeString(tourSituation);
        dest.writeString(remarks);
    }

    public static final Creator<TourInfo> CREATOR = new Creator<TourInfo>() {
        @Override
        public TourInfo createFromParcel(Parcel source) {
            TourInfo ti = new TourInfo();
            ti.id = source.readInt();
            ti.tourCategory = source.readString();
            ti.tourNum = source.readString();
            ti.preTourPerson = source.readString();
            ti.preTourDate = source.readString();
            ti.bdsId = source.readInt();
            ti.bds = source.readString();
            ti.receivePerson = source.readString();
            ti.receiveDate = source.readString();
            ti.weather = source.readString();
            ti.temperature = source.readString();
            ti.isReceived = source.readInt();
            ti.isFinished = source.readInt();
            ti.tourStartTime = source.readString();
            ti.tourEndTime = source.readString();
            ti.tourPerson = source.readString();
            ti.tourSituation = source.readString();
            ti.remarks = source.readString();
            return ti;
        }

        @Override
        public TourInfo[] newArray(int size) {
            return new TourInfo[size];
        }
    };
}
