package com.example.tonyso.TrafficApp.model;

/**
 * Created by soman on 2015/11/1.
 */
public class RouteSpeedMap extends RouteCCTV {
    static final public String TYPE_Speed_Map = "SpeedMap";
    private String[] regions;

    public RouteSpeedMap() {
    }

    private RouteSpeedMap(RouteSpeedMap routeSpeedMap, int id) {
        this.route_id = id;
        this.latLngs = routeSpeedMap.latLngs;
        this.route_id = routeSpeedMap.route_id;
        this.region = routeSpeedMap.regions;
        this.name = routeSpeedMap.name;
        this.ref_key = routeSpeedMap.ref_key;
        this.description = routeSpeedMap.description;
        this.type = TYPE_Speed_Map;
    }

    @Override
    public String[] getDescription() {
        return description;
    }

    public RouteSpeedMap setDescription(String[] description) {
        this.description = description;
        return this;
    }

    public RouteSpeedMap setRegions(String[] regions) {
        this.regions = regions;
        return this;
    }

    public RouteSpeedMap build(int id) {
        return new RouteSpeedMap(this, id);
    }
}
