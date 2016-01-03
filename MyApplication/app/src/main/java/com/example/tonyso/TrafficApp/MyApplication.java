package com.example.tonyso.TrafficApp;

import android.app.Application;

import com.example.tonyso.TrafficApp.model.RouteCCTV;

import java.util.List;

/**
 * Created by TonySo on 17/9/2015.
 */
public class MyApplication extends Application{

    public class Language{
        public static final String ZH_HANT = "zh";
        public static final String ENGLISH = "en";
    }

    public static final String Language_Locale = "Locale";
    public static final String Language_UserPref = "UserPref";

    public static final String WIFI = "WIFI";
    public List<RouteCCTV> list ;

    BookmarkTimeStatusObserver timeStatusObserver;

    public MyApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        timeStatusObserver = new BookmarkTimeStatusObserver();
    }

    public BookmarkTimeStatusObserver getTimeStatusObserver() {
        return timeStatusObserver;
    }

    public void setTimeStatusObserver(BookmarkTimeStatusObserver timeStatusObserver) {
        this.timeStatusObserver = timeStatusObserver;
    }
}
