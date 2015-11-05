package com.example.tonyso.TrafficApp.Interface;

/**
 * Created by TonySoMan on 1/9/2015.
 */
public interface WeatherRefreshHandler {
    void onRefreshWeather(String s);
    void onRefreshIcon(String URL);
    void onRefreshLocation(String address);
}
