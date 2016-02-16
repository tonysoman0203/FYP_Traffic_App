package com.example.tonyso.TrafficApp.listener;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by soman on 2016/2/14.
 */
public interface OnPathReadyListener {
    void onPathReady(List<LatLng> paths, String duration);
}
