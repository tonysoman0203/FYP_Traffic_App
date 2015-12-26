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
    private  boolean isTimeOver;

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
    }

    public static class Builder{
        private int _id;
        private String[] bkRouteName;
        private String timestamp;
        private String targetTime;
        private String routeImageKey;
        private String[] district;
        private boolean isTimeOver;

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
