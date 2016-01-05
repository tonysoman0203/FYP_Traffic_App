package com.example.tonyso.TrafficApp.model;

import java.io.Serializable;

/**
 * Created by TonySo on 31/10/15.
 */
public class RouteCCTV extends Route implements Serializable{
    private String[] description = null;
    private String[] region = null;
    private double[] latLngs = null;
    private RouteCCTV(Builder builder) {
        route_id = builder.id;
        ref_key = builder.key;
        this.description = builder.description;
        this.region = builder.region;
        this.latLngs = builder.latLngs;
    }

    public static class Builder {
        private int id;
        private String key;
        private String [] description;
        private String[] region;
        private double[] latLngs;

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

    public double[] getLatLngs() {
        return latLngs;
    }

    public String[] getDescription() {
        return description;
    }

    public String[] getRegion() {
        return region;
    }

}
