package com.example.tonyso.TrafficApp.model;

/**
 * Created by TonySo on 31/10/15.
 */
public class RouteCCTV extends Route{
    private int id;
    private String key;
    private String[] description;
    private String[] region;
    private double[] latLngs;

    public RouteCCTV() {
    }

    public RouteCCTV(int id, String key, String[] description, String[] region) {
        this.id = id;
        this.key = key;
        this.description = description;
        this.region = region;
    }

    public RouteCCTV(int id, String key, String[] description, String[] region, double[] latLngs) {
        this.id = id;
        this.key = key;
        this.description = description;
        this.region = region;
        this.latLngs = latLngs;
    }

    public double[] getLatLngs() {
        return latLngs;
    }

    public void setLatLngs(double[] latLngs) {
        this.latLngs = latLngs;
    }

    public int getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String[] getDescription() {
        return description;
    }

    public String[] getRegion() {
        return region;
    }

    public void setDescription(String[] description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setRegion(String[] region) {
        this.region = region;
    }
}
