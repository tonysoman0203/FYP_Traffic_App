package com.example.tonyso.TrafficApp.listener;

import com.example.tonyso.TrafficApp.model.RouteCCTV;

import java.util.List;

/**
 * Created by soman on 2016/2/24.
 */
public interface OnCCTVFilterReadyListener {
    void onCCTVReady(List<RouteCCTV> list);
}
