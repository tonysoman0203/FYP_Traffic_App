package com.example.tonyso.TrafficApp.model;

import java.io.Serializable;

/**
 * Created by TonySo on 16/9/2015.
 */
public class Route implements Serializable{
    private long route_id;
    private String Name;
    private String SatuationLevel;
    private String Ref_ID; //RD659F
    private String imageURL;

    public Route(long route_id, String name, String satuationLevel, String ref_ID, String imageURL) {
        this.route_id = route_id;
        Name = name;
        SatuationLevel = satuationLevel;
        Ref_ID = ref_ID;
        this.imageURL = imageURL;
    }

    public Route() {

    }

    public long getRoute_id() {
        return route_id;
    }

    public void setRoute_id(long route_id) {
        this.route_id = route_id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getSatuationLevel() {
        return SatuationLevel;
    }

    public void setSatuationLevel(String satuationLevel) {
        this.SatuationLevel = satuationLevel;
    }

    public String getRef_ID() {
        return Ref_ID;
    }

    public void setRef_ID(String ref_ID) {
        this.Ref_ID = ref_ID;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
