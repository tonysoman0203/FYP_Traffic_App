package com.example.tonyso.TrafficApp.model;

import android.util.Log;

import com.google.android.gms.location.places.Place;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by soman on 2016/1/3.
 */
public class NearbyLocation {
    static String TAG = NearbyLocation.class.getName();
    private String id;
    private String icon;
    private byte[] bytes;
    private String name;
    private String vicinity;
    private Double latitude;
    private Double longitude;
    private String placeId;
    private String distanceInKm;
    private String photoReference;

    public String getDistanceInKm() {
        return distanceInKm;
    }

    public void setDistanceInKm(String distanceInKm) {
        this.distanceInKm = distanceInKm;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public static NearbyLocation jsonToPontoReferencia(JSONObject pontoReferencia) {
        try {
            NearbyLocation result = new NearbyLocation();
            JSONObject geometry = (JSONObject) pontoReferencia.get("geometry");
            JSONObject location = (JSONObject) geometry.get("location");
            result.setLatitude((Double) location.get("lat"));
            result.setLongitude((Double) location.get("lng"));
            result.setIcon(pontoReferencia.getString("icon"));
            result.setName(pontoReferencia.getString("name"));
            result.setPlaceId(pontoReferencia.getString("place_id"));
            result.setId(pontoReferencia.getString("id"));
            if (!pontoReferencia.isNull("photos")) {
                JSONArray photos = pontoReferencia.getJSONArray("photos");
                result.setPhotoReference(photos.getJSONObject(0).getString("photo_reference"));
            }
            return result;
        } catch (JSONException ex) {
            Logger.getLogger(Place.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String toString() {
        return "NearbyLocation{" +
                "id='" + id + '\'' +
                ", icon='" + icon + '\'' +
                ", bytes=" + Arrays.toString(bytes) +
                ", name='" + name + '\'' +
                ", vicinity='" + vicinity + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", placeId='" + placeId + '\'' +
                ", distanceInKm='" + distanceInKm + '\'' +
                ", photoReference='" + photoReference + '\'' +
                '}';
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public static String getDistanceByJSON(String jsonString) {
        String status = null;
        try {
            final JSONObject jsonObject = new JSONObject(jsonString);
            status = jsonObject.getString("status");
            Log.e(TAG, status);
            JSONArray routeArray = jsonObject.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONArray newTempARr = routes.getJSONArray("legs");
            JSONObject newDisTimeOb = newTempARr.getJSONObject(0);
            JSONObject distOb = newDisTimeOb.getJSONObject("distance");
            JSONObject timeOb = newDisTimeOb.getJSONObject("duration");
            Log.i("Diatance :", distOb.getString("text"));
            Log.i("Time :", timeOb.getString("text"));
            return distOb.getString("text");
        } catch (JSONException e) {
            return status;
        }
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public String getPhotoReference() {
        return photoReference;
    }
}
