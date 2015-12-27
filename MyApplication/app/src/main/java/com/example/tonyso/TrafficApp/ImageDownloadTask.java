package com.example.tonyso.TrafficApp;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.tonyso.TrafficApp.Singleton.LanguageSelector;
import com.example.tonyso.TrafficApp.Singleton.RouteMapping;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

/**
 * Created by soman on 2015/11/24.
 */
public class ImageDownloadTask extends AsyncTask<String,Void,Integer>{
    RouteMapping routeMapping;
    private static final String TRAFFIC_URL = "http://tdcctv.data.one.gov.hk/";
    private static final String JPG = ".JPG";
    MyApplication myApplication;
    LanguageSelector languageSelector;
    Activity a;
    Service context;
    boolean isDone = false;

    public ImageDownloadTask(Activity context,RouteMapping routeMapping) {
        this.a =context;
        this.routeMapping = routeMapping;
        languageSelector = LanguageSelector.getInstance(context);
        myApplication = (MyApplication) a.getApplication();
    }

    public ImageDownloadTask(Service service , RouteMapping routeMapping){
        this.context = service;
        this.routeMapping = routeMapping;
        languageSelector = LanguageSelector.getInstance(context);
        myApplication = (MyApplication) context.getApplication();
    }

    @Override
    protected Integer doInBackground(String... params) {
        downloadBitmapIntoCache();
        return 1;
    }

    @Override
    protected void onPostExecute(Integer i) {
        super.onPostExecute(i);
        routeMapping.getPutCount();
        Log.e(ImageDownloadTask.class.getCanonicalName(), "Download Image Into Cache Complete");
        isDone = true;
    }

    private void downloadBitmapIntoCache() {
        try {
            for (int n = 0 ; n < myApplication.list.size();n++){
                String imgKey = myApplication.list.get(n).getRef_key();
                String name;
                if (languageSelector.getLanguage().equals(MyApplication.Language.ENGLISH)){
                    name = myApplication.list.get(n).getDescription()[0];
                }else{
                    name = myApplication.list.get(n).getDescription()[1];
                }
                URL url = new URL(TRAFFIC_URL.concat(imgKey).concat(JPG));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                //myApplication.list.get(n).setBitmap(myBitmap);
                routeMapping.addBitmapToMemoryCache(name,myBitmap);
            }
        } catch (IOException e) {
            // Log exception
            Logger logger = Logger.getLogger(ImageDownloadTask.class.getName());
            logger.info("Cannot Not Image from URL");
            e.printStackTrace();
        }
    }

}
