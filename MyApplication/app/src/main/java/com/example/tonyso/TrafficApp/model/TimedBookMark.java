package com.example.tonyso.TrafficApp.model;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * Created by TonySo on 18/9/2015.
 */
public class TimedBookMark implements Serializable {
    private int _id;
    private String bkRouteName;
    private Timestamp timestamp;
    private Time targetTime;
    private String routeImageKey;
    private String district;
    private boolean isTimeOver;

    public TimedBookMark(int _id, String bk_Route_Name, Timestamp timestamp, String routeImageURL, String district) {
        this._id = _id;
        bkRouteName = bk_Route_Name;
        this.timestamp = timestamp;
        this.routeImageKey = routeImageURL;
        //SatuationLevel = satuationLevel;
        this.district = district;
    }

    public TimedBookMark(int _id, String bkRouteName, Timestamp timestamp, Time targetTime, String routeImageKey, String district) {
        this._id = _id;
        this.bkRouteName = bkRouteName;
        this.timestamp = timestamp;
        this.targetTime = targetTime;
        this.routeImageKey = routeImageKey;
        this.district = district;
    }

    public TimedBookMark(int _id, String bkRouteName, Timestamp timestamp, Time targetTime, String routeImageKey, String district, boolean isTimeOver) {
        this._id = _id;
        this.bkRouteName = bkRouteName;
        this.timestamp = timestamp;
        this.targetTime = targetTime;
        this.routeImageKey = routeImageKey;
        this.district = district;
        this.isTimeOver = isTimeOver;
    }


    public boolean isTimeOver() {
        return isTimeOver;
    }

    public void setIsTimeOver(boolean isTimeOver) {
        this.isTimeOver = isTimeOver;
    }

    public TimedBookMark() {}

    public Time getTargetTime() {
        return targetTime;
    }

    public void setTargetTime(Time targetTime) {
        this.targetTime = targetTime;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getBkRouteName() {
        return bkRouteName;
    }

    public void setBkRouteName(String bkRouteName) {
        this.bkRouteName = bkRouteName;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getRouteImageKey() {
        return routeImageKey;
    }

    public void setRouteImageKey(String routeImageURL) {
        this.routeImageKey = routeImageURL;
    }

}
