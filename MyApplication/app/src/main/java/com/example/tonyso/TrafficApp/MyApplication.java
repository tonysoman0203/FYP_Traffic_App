package com.example.tonyso.TrafficApp;

import android.app.Application;

import com.example.tonyso.TrafficApp.model.RouteCCTV;

import java.util.ArrayList;
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

    public static final String fileName_zh = "CurrentWeather_uc.xml";
    public static final String fileName = "CurrentWeather.xml";

    public static final String WIFI = "WIFI";

    public static double Lat ;
    public static double lng;

    public List<RouteCCTV> list ;
}
