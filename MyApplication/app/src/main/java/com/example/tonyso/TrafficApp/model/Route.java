package com.example.tonyso.TrafficApp.model;

import java.io.Serializable;

/**
 * Created by TonySo on 16/9/2015.
 */
public class Route implements Serializable{
    protected int route_id;
    protected String name;
    protected String sat_level;
    protected String ref_key; //RD659F
    protected double[] latLngs;
    protected String type;

    public Route(int route_id, String name, String sat_level, String refkey, double[] latLng) {
        this.route_id = route_id;
        this.name = name;
        this.sat_level = sat_level;
        ref_key = refkey;
        this.latLngs = latLng;
    }

    public Route() {
    }

    public Route(int i, String s) {
        this.route_id = i;
        this.name = s;
    }

    public double[] getLatLngs() {
        return latLngs;
    }

    public Route setLatLngs(double[] latLngs) {
        this.latLngs = latLngs;
        return this;
    }

    public Route setLatLng(double[] latLng) {
        this.latLngs = latLng;
        return this;
    }

    public String getType() {
        return type;
    }

    public Route setType(String type) {
        this.type = type;
        return this;
    }

    public long getRoute_id() {
        return route_id;
    }

    public void setRoute_id(int route_id) {
        this.route_id = route_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSat_level() {
        return sat_level;
    }

    public void setSat_level(String sat_level) {
        this.sat_level = sat_level;
    }

    public String getRef_key() {
        return ref_key;
    }

    public void setRef_key(String ref_key) {
        this.ref_key = ref_key;
    }
}
