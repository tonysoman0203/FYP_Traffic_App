package com.example.tonyso.TrafficApp.model;

import java.io.Serializable;

/**
 * Created by TonySo on 31/10/15.
 */
public class RouteCCTV extends Route implements Serializable{
    static public final String TYPE_CCTV = "cctv";
    protected String[] description = null;
    protected String[] region = null;
    public String distance;

    private RouteCCTV(Builder builder) {
        route_id = builder.id;
        ref_key = builder.key;
        this.description = builder.description;
        this.region = builder.region;
        this.latLngs = builder.latLngs;
        this.type = builder.type;
        this.distance = builder.distance;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public RouteCCTV() {
    }

    public double[] getLatLngs() {
        return latLngs;
    }

    public String[] getDescription() {
        return description;
    }

    public String[] getRegion() {
        return region;
    }

    public static class Builder {
        private int id;
        private String key;
        private String [] description;
        private String[] region;
        private double[] latLngs;
        private String type;
        public String distance;

        public RouteCCTV build(){
            return new RouteCCTV(this);
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
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

        public String getType() {
            return type;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }
    }

}
