package com.example.tonyso.TrafficApp.listener;

import com.example.tonyso.TrafficApp.model.RouteCCTV;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soman on 2016/2/14.
 */
public interface OnPathReadyListener {
    void onPathReady(ArrayList<Double[]> list, ArrayList<RouteCCTV> cctvsInPaths);
}
