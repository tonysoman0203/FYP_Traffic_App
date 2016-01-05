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

    public Route(int route_id, String name, String sat_level, String refkey) {
        this.route_id = route_id;
        this.name = name;
        this.sat_level = sat_level;
        ref_key = refkey;
    }

    public Route() {
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
