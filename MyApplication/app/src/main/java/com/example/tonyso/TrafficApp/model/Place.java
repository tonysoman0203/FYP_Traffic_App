package com.example.tonyso.TrafficApp.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by TonySo on 6/1/16.
 */
public class Place {

    String placeId;
    CharSequence name;
    LatLng latlngs;
    CharSequence address;
    CharSequence phoneno;

    public Place() {
    }

    private Place(Place place) {
        this.placeId = place.placeId;
        this.name = place.name;
        this.latlngs = place.latlngs;
        this.address = place.address;
        this.phoneno = place.phoneno;
    }

    public Place build() {
        return new Place(this);
    }

    public String getPlaceId() {
        return placeId;
    }

    public Place setPlaceId(String placeId) {
        this.placeId = placeId;
        return this;
    }

    public CharSequence getName() {
        return name;
    }

    public Place setName(CharSequence name) {
        this.name = name;
        return this;
    }

    public LatLng getLatlngs() {
        return latlngs;
    }

    public Place setLatlngs(LatLng latlngs) {
        this.latlngs = latlngs;
        return this;
    }

    public CharSequence getAddress() {
        return address;
    }

    public Place setAddress(CharSequence address) {
        this.address = address;
        return this;
    }

    public CharSequence getPhoneno() {
        return phoneno;
    }

    public Place setPhoneno(CharSequence phoneno) {
        this.phoneno = phoneno;
        return this;
    }
}
