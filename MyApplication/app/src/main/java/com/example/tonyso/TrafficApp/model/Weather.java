package com.example.tonyso.TrafficApp.model;

/**
 * Created by TonySoMan on 6/9/2015.
 */
public class Weather {

    private int weather_Id;
    private String weatherIcon;
    private String degree;

    public Weather() {}

    public Weather(int weather_Id, String weatherIcon, String degree) {
        this.weather_Id = weather_Id;
        this.weatherIcon = weatherIcon;
        this.degree = degree;
    }

    public Weather(int weather_Id) {
        this.weather_Id = weather_Id;
    }

    public int getweather_Id() {
        return weather_Id;
    }

    public void setWeather_Id(int weather_Id) {
        this.weather_Id = weather_Id;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }
}
