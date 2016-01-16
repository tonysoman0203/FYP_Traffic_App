package com.example.tonyso.TrafficApp.model;

/**
 * Created by soman on 2015/11/1.
 */
public class RouteSpeedMap extends RouteCCTV {
    private String[] regions;

    public RouteSpeedMap() {
    }

    private RouteSpeedMap(RouteSpeedMap routeSpeedMap, int id) {
        this.route_id = id;
        this.latLng = routeSpeedMap.latLng;
        this.route_id = routeSpeedMap.route_id;
        this.regions = routeSpeedMap.regions;
        this.name = routeSpeedMap.name;
        this.ref_key = routeSpeedMap.ref_key;
        this.type = "SpeedMap";
    }

    public String[] getRegions() {
        return regions;
    }

    public RouteSpeedMap setRegions(String[] regions) {
        this.regions = regions;
        return this;
    }

    public RouteSpeedMap build(int id) {
        return new RouteSpeedMap(this, id);
    }
}
