package com.example.tonyso.TrafficApp.listener;

import android.util.Log;

import java.util.Observable;

/**
 * Created by soman on 2016/1/1.
 */
public class StatusObserver extends Observable {
    private boolean isTimeOverChanged = false;
    private boolean isPathReady = false;

    public StatusObserver() {
        Log.d(StatusObserver.class.getName(), "Building Status Observer....");
    }

    public boolean isTimeOverChanged() {
        return isTimeOverChanged;
    }

    public void setIsTimeOverChanged(boolean isTimeOverChanged) {
        this.isTimeOverChanged = isTimeOverChanged;
        setChanged();
        notifyObservers();
    }

    public boolean isPathReady() {
        return isPathReady;
    }

    public void setIsPathReady(boolean isPathReady) {
        this.isPathReady = isPathReady;
        setChanged();
        notifyObservers();
    }
}
