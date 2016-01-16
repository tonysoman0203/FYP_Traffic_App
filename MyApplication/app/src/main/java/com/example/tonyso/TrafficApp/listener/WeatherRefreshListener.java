package com.example.tonyso.TrafficApp.listener;

/**
 * Created by TonySoMan on 1/9/2015.
 */
public interface WeatherRefreshListener {
    void onRefreshWeather(String s);
    void onRefreshIcon(String URL);
    void onRefreshLocation(String address);
}
