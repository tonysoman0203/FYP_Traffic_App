package com.example.tonyso.TrafficApp.listener;

import com.example.tonyso.TrafficApp.model.TimedBookMark;

import java.util.ArrayList;

/**
 * Created by TonySo on 29/12/15.
 */
public interface OnRemainingTimeListener {
    void onRemainingTimeChanged(ArrayList<TimedBookMark> pos);
}
