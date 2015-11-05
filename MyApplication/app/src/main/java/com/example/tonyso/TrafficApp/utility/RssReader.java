package com.example.tonyso.TrafficApp.utility;

import android.app.ProgressDialog;
import android.util.Log;

import com.example.tonyso.TrafficApp.Interface.WeatherRefreshHandler;
import com.example.tonyso.TrafficApp.Interface.Rss_Listener;
import com.example.tonyso.TrafficApp.MainActivity;
import com.example.tonyso.TrafficApp.model.Weather;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

//import java.util.Date;

public class RssReader implements Rss_Listener {
    private ProgressDialog dialog;
    private String URL = "http://rss.weather.gov.hk/rss/";
    String CURRWEATHER_FILE_NAME = "";
    MainActivity context;
    WeatherRefreshHandler weatherRefreshHandler;
    ErrorDialog errorDialog;

    public RssReader(MainActivity context, WeatherRefreshHandler weatherRefreshHandler, String fileName) {
        this.context = context;
        this.weatherRefreshHandler = weatherRefreshHandler;
        this.CURRWEATHER_FILE_NAME = fileName;
        this.URL = URL.concat(CURRWEATHER_FILE_NAME);
        errorDialog = new ErrorDialog(context);
        Log.e(getClass().getSimpleName(), this.URL);
    }

    public void FeedRss() {
        dialog = ProgressDialog.show(context, "Loading", "Loading the Rss");
        Thread th = new Thread(new Runnable() {
            public void run() {
                RssHandler rssHandler = new RssHandler(errorDialog);
                rssHandler.setListener(RssReader.this);
                //rssHandler.setErrorListener(errorDialog);
                try {
                    rssHandler.processWeatherFeed(context, new URL(URL));
                } catch (MalformedURLException e) {
                    errorDialog.displayAlertDialog(e.getMessage());
//                    e.printStackTrace();
                }
            }
        });
        th.start();
    }

    public void ParsedInfo(final List weatherList) {
        context.rss_Handler.post(new Runnable() {
            public void run() {
                List<Weather>weathers = weatherList;
                weatherRefreshHandler.onRefreshWeather(weathers.get(0).getDegree());
                weatherRefreshHandler.onRefreshIcon(weathers.get(0).getWeatherIcon());
                return;
            }
        });
        dialog.dismiss();
    }
}
