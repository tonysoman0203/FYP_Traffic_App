package com.example.tonyso.TrafficApp.model;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by TonySo on 6/1/16.
 */
public class Place implements Serializable {

    String placeId;
    CharSequence name;
    double[] latlngs;
    String type;
    CharSequence address;
    CharSequence phoneno;
    String lastUpdateTime;
    Bitmap bitmap;
    CharSequence district;

    public Place() {
    }

    @Override
    public String toString() {
        return "Place{" +
                "placeId='" + placeId + '\'' +
                ", name=" + name +
                ", latlngs=" + Arrays.toString(latlngs) +
                ", type='" + type + '\'' +
                ", address=" + address +
                ", phoneno=" + phoneno +
                ", lastUpdateTime='" + lastUpdateTime + '\'' +
                ", bitmap=" + bitmap +
                ", district=" + district +
                '}';
    }

    public CharSequence getDistrict() {
        return district;
    }

    public Place setDistrict(CharSequence district) {
        this.district = district;
        return this;
    }

    public Place(Place place) {
        this.placeId = place.placeId;
        this.name = place.name;
        this.latlngs = place.latlngs;
        this.address = place.address;
        this.phoneno = place.phoneno;
        this.lastUpdateTime = place.lastUpdateTime;
        this.type = place.type;
        this.district = place.district;
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

    public double[] getLatlngs() {
        return latlngs;
    }

    public Place setLatlngs(double[] latlngs) {
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
