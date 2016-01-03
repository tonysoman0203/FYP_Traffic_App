package com.example.tonyso.TrafficApp.Singleton;

import android.app.ProgressDialog;
import android.util.Log;

import com.example.tonyso.TrafficApp.MainActivity;
import com.example.tonyso.TrafficApp.MyApplication;
import com.example.tonyso.TrafficApp.listener.RssHandler;
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
    String filename = "";
    MainActivity context;
    WeatherRefreshListener weatherRefreshListener;
    ErrorDialog errorDialog;
    LanguageSelector languageSelector ;

    private static final String fileName_zh = "CurrentWeather_uc.xml";
    private static final String fileName = "CurrentWeather.xml";

    public static RssReader rssReader;

    public RssReader(MainActivity context, WeatherRefreshListener weatherRefreshListener) {
        this.context = context;
        this.weatherRefreshListener = weatherRefreshListener;
        this.languageSelector = LanguageSelector.getInstance(this.context);
        this.errorDialog = ErrorDialog.getInstance(this.context);
        if (languageSelector.getLanguage().equals(MyApplication.Language.ENGLISH))
            this.filename = fileName;
        else
            this.filename = fileName_zh;
        this.URL = URL.concat(filename);
        Log.e(getClass().getSimpleName(), this.URL);
    }


    @Override
    public String toString() {
        return "RssReader{" +
                "dialog=" + dialog +
                ", URL='" + URL + '\'' +
                ", filename='" + filename + '\'' +
                ", context=" + context +
                ", weatherRefreshListener=" + weatherRefreshListener +
                ", errorDialog=" + errorDialog +
                ", languageSelector=" + languageSelector +
                '}';
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

    @Override
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
