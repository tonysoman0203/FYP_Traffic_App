package com.example.tonyso.TrafficApp.model;

/**
 * Created by soman on 2016/1/3.
 */
public class NearbyLocation extends Place {
    private String vicinity;
    private String distanceInKm;
    private String photoReference;
    private String icon;

    private NearbyLocation(NearbyLocation nearbyLocation) {
        this.name = nearbyLocation.name;
        this.distanceInKm = nearbyLocation.distanceInKm;
        this.icon = nearbyLocation.icon;
        this.vicinity = nearbyLocation.vicinity;
        this.photoReference = nearbyLocation.photoReference;
        this.lastUpdateTime = nearbyLocation.lastUpdateTime;
        this.placeId = nearbyLocation.placeId;
        this.name = nearbyLocation.name;
        this.latlngs = nearbyLocation.latlngs;
        this.address = nearbyLocation.address;
        this.phoneno = nearbyLocation.phoneno;
        this.lastUpdateTime = nearbyLocation.lastUpdateTime;
        this.type = nearbyLocation.type;
    }

    public NearbyLocation() {
    }

    public NearbyLocation build() {
        return new NearbyLocation(this);
    }

    public String getIcon() {
        return icon;
    }

    public NearbyLocation setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public String getVicinity() {
        return vicinity;
    }

    public NearbyLocation setVicinity(String vicinity) {
        this.vicinity = vicinity;
        return this;
    }

    public String getDistanceInKm() {
        return distanceInKm;
    }

    public NearbyLocation setDistanceInKm(String distanceInKm) {
        this.distanceInKm = distanceInKm;
        return this;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public NearbyLocation setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
        return this;
    }
}
