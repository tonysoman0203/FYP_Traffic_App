package com.example.tonyso.TrafficApp.listener;

import java.util.Observable;

/**
 * Created by soman on 2016/1/1.
 */
public class BookmarkTimeStatusObserver extends Observable {
    private boolean isTimeOverChanged = false;

    public boolean isTimeOverChanged() {
        return isTimeOverChanged;
    }

    public void setIsTimeOverChanged(boolean isTimeOverChanged) {
        this.isTimeOverChanged = isTimeOverChanged;
        setChanged();
        notifyObservers();
    }

}
