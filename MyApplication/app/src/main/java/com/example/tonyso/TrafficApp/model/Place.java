package com.example.tonyso.TrafficApp.model;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by TonySo on 6/1/16.
 */
public class Place {

    String placeId;
    CharSequence name;
    LatLng latlngs;
    String type;
    CharSequence address;
    CharSequence phoneno;
    String lastUpdateTime;
    Bitmap bitmap;

    public Place() {
    }

    public Place(Place place) {
        this.placeId = place.placeId;
        this.name = place.name;
        this.latlngs = place.latlngs;
        this.address = place.address;
        this.phoneno = place.phoneno;
        this.lastUpdateTime = place.lastUpdateTime;
        this.type = place.type;
    }


    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getType() {
        return type;
    }

    public Place setType(String type) {
        this.type = type;
        return this;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public Place setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
        return this;
    }

    protected Place build() {
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
