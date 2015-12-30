package com.example.tonyso.TrafficApp.rss;

import android.app.ProgressDialog;
import android.util.Log;

import com.example.tonyso.TrafficApp.MainActivity;
import com.example.tonyso.TrafficApp.Singleton.ErrorDialog;
import com.example.tonyso.TrafficApp.Singleton.LanguageSelector;
import com.example.tonyso.TrafficApp.listener.Rss_Listener;
import com.example.tonyso.TrafficApp.listener.WeatherRefreshListener;
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
    WeatherRefreshListener weatherRefreshListener;
    ErrorDialog errorDialog;
    LanguageSelector languageSelector ;

    public RssReader(MainActivity context, WeatherRefreshListener weatherRefreshListener, String fileName) {
        this.context = context;
        this.weatherRefreshListener = weatherRefreshListener;
        this.CURRWEATHER_FILE_NAME = fileName;
        this.URL = URL.concat(CURRWEATHER_FILE_NAME);
        this.languageSelector = LanguageSelector.getInstance(this.context);
        errorDialog = ErrorDialog.getInstance(this.context);
        Log.e(getClass().getSimpleName(), this.URL);
    }

    public void FeedRss() {
        dialog = ProgressDialog.show(context, "Loading", "Loading the Rss");
        final Thread th = new Thread(new Runnable() {
            public void run() {
                RssHandler rssHandler = new RssHandler(errorDialog,languageSelector);
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
                weatherRefreshListener.onRefreshWeather(weathers.get(0).getDegree());
                weatherRefreshListener.onRefreshIcon(weathers.get(0).getWeatherIcon());
                return;
            }
        });
        dialog.dismiss();
    }
}
