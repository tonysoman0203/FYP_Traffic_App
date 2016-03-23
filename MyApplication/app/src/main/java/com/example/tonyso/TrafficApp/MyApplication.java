package com.example.tonyso.TrafficApp;

import android.app.Application;

import com.example.tonyso.TrafficApp.listener.StatusObserver;
import com.example.tonyso.TrafficApp.model.RouteCCTV;
import com.example.tonyso.TrafficApp.model.RouteSpeedMap;

import java.util.List;

/**
 * Created by TonySo on 17/9/2015.
 */
public class MyApplication extends Application{

    public static final String Language_Locale = "Locale";
    public static final String Language_UserPref = "UserPref";
    public static final String WIFI = "WIFI";
    public static String CURR_LANG;
    public static String KEY_NEAR_IN_KM = "KEY_NEAR_IN_KM";
    public static Integer KM_IN_NEAR;
    public List<RouteCCTV> list ;
    public List<RouteSpeedMap> speedMaps;
    public String locate;
    StatusObserver timeStatusObserver;
    public int _id;


    public MyApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        timeStatusObserver = new StatusObserver();
    }

    public StatusObserver getTimeStatusObserver() {
        return timeStatusObserver;
    }

    public void setTimeStatusObserver(StatusObserver timeStatusObserver) {
        this.timeStatusObserver = timeStatusObserver;
    }

    public class Language {
        public static final String ZH_HANT = "zh";
        public static final String ENGLISH = "en";
    }
}
