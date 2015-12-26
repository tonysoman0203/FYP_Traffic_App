package com.example.tonyso.TrafficApp.model;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by TonySo on 31/10/15.
 */
public class RouteCCTV extends Route implements Serializable{
    //private int id;
    //private String key;
    private String[] description = null;
    private String[] region = null;
    private double[] latLngs = null;
//    private Bitmap bitmap = null;

//    public Bitmap getBitmap() {
//        return bitmap;
//    }
//
//    public void setBitmap(Bitmap bitmap) {
//        this.bitmap = bitmap;
//    }

    private RouteCCTV(Builder builder) {
        route_id = builder.id;
        ref_key = builder.key;
        this.description = builder.description;
        this.region = builder.region;
        this.latLngs = builder.latLngs;
//        this.bitmap = builder.bitmap;
    }

    public static class Builder {
        private int id;
        private String key;
        private String [] description;
        private String[] region;
        private double[] latLngs;
//        private Bitmap bitmap;

//        public Bitmap getBitmap() {
//            return bitmap;
//        }
//
//        public Builder setBitmap(Bitmap bitmap) {
//            this.bitmap = bitmap;
//            return this;
//        }

        public RouteCCTV build(){
            return new RouteCCTV(this);
        }

        public int getId() {
            return id;
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public String getKey() {
            return key;
        }

        public Builder setKey(String key) {
            this.key = key;
            return this;
        }

        public String[] getDescription() {
            return description;
        }

        public Builder setDescription(String[] description) {
            this.description = description;
            return this;
        }

        public String[] getRegion() {
            return region;
        }

        public Builder setRegion(String[] region) {
            this.region = region;
            return this;
        }

        public double[] getLatLngs() {
            return latLngs;
        }

        public Builder setLatLngs(double[] latLngs) {
            this.latLngs = latLngs;
            return this;
        }
    }

    public RouteCCTV() {
    }

//    public RouteCCTV(int id, String key, String[] description, String[] region) {
//        this.id = id;
//        this.key = key;
//        this.description = description;
//        this.region = region;
//    }
//
//    public RouteCCTV(int id, String key, String[] description, String[] region, double[] latLngs) {
//        this.id = id;
//        this.key = key;
//        this.description = description;
//        this.region = region;
//        this.latLngs = latLngs;
//    }

    public double[] getLatLngs() {
        return latLngs;
    }
//
//    public void setLatLngs(double[] latLngs) {
//        this.latLngs = latLngs;
//    }

//    public int getId() {
//        return id;
//    }

//    public String getKey() {
//        return key;
//    }

    public String[] getDescription() {
        return description;
    }

    public String[] getRegion() {
        return region;
    }

//    public void setDescription(String[] description) {
//        this.description = description;
//    }

//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public void setKey(String key) {
//        this.key = key;
//    }
//
//    public void setRegion(String[] region) {
//        this.region = region;
//    }
}
