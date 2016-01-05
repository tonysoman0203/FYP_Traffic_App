package com.example.tonyso.TrafficApp.model;

import java.io.Serializable;

/**
 * Created by TonySo on 18/9/2015.
 */
public class TimedBookMark implements Serializable {
    private  int _id;
    private  String[] bkRouteName;
    private  String startTime;
    private  String targetTime;
    private  String routeImageKey;
    private  String[] regions;
    private int remainTime;
    private boolean isTimeOver;
    private double[] latLngs = null;

    public void setIsTimeOver(boolean isTimeOver) {
        this.isTimeOver = isTimeOver;
    }

    public double[] getLatLngs() {
        return latLngs;
    }

    public void setLatLngs(double[] latLngs) {
        this.latLngs = latLngs;
    }

    public int getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(int RemainTime) {
        this.remainTime = RemainTime;
    }

    public int get_id() {
        return _id;
    }

    public String[] getBkRouteName() {
        return bkRouteName;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getTargetTime() {
        return targetTime;
    }

    public String getRouteImageKey() {
        return routeImageKey;
    }

    public String[] getRegions() {
        return regions;
    }

    public boolean isTimeOver() {
        return isTimeOver;
    }

    private TimedBookMark(Builder builder){
        this._id = builder._id;
        this.bkRouteName = builder.bkRouteName;
        this.startTime = builder.timestamp;
        this.targetTime = builder.targetTime;
        this.routeImageKey = builder.routeImageKey;
        this.regions = builder.district;
        this.isTimeOver = builder.isTimeOver;
        this.remainTime = builder.remainTime;
        this.latLngs = builder.latLngs;
    }

    public static class Builder{
        private int _id;
        private String[] bkRouteName;
        private String timestamp;
        private String targetTime;
        private String routeImageKey;
        private double[] latLngs = null;
        private int remainTime;
        private String[] district;
        private boolean isTimeOver;

        public int getRemainTime() {
            return remainTime;
        }

        public Builder setRemainTime(int remainTime) {
            this.remainTime = remainTime;
            return this;
        }

        public double[] getLatLngs() {
            return latLngs;
        }

        public Builder setLatLngs(double[] latLngs) {
            this.latLngs = latLngs;
            return this;
        }

        public Builder() {}

        public TimedBookMark build(){
            return new TimedBookMark(this);
        };

        public int get_id() {
            return _id;
        }

        public Builder set_id(int _id) {
            this._id = _id;
            return this;
        }

        public String[] getBkRouteName() {
            return bkRouteName;
        }

        public Builder setBkRouteName(String[] bkRouteName) {
            this.bkRouteName = bkRouteName;
            return this;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public Builder setTimestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public String getTargetTime() {
            return targetTime;
        }

        public Builder setTargetTime(String targetTime) {
            this.targetTime = targetTime;
            return this;
        }

        public String getRouteImageKey() {
            return routeImageKey;
        }

        public Builder setRouteImageKey(String routeImageKey) {
            this.routeImageKey = routeImageKey;
            return this;
        }

        public String[] getDistrict() {
            return district;
        }

        public Builder setDistrict(String[] district) {
            this.district = district;
            return this;
        }

        public boolean isTimeOver() {
            return isTimeOver;
        }

        public Builder setIsTimeOver(boolean isTimeOver) {
            this.isTimeOver = isTimeOver;
            return this;
        }
    }

}
