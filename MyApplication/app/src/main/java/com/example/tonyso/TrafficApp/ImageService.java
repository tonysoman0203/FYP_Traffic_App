package com.example.tonyso.TrafficApp;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.tonyso.TrafficApp.Singleton.RouteMapping;
import com.example.tonyso.TrafficApp.utility.CommonUtils;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public class ImageService extends Service {

    private static final long FIVE_MINUTES = 300000 ;
    RouteMapping routeMapping;
    ImageDownloadTask imageDownloadTask;
    private static final String TAG = ImageService.class.getName();

    public ImageService() {}

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        routeMapping = RouteMapping.getInstance(this);
        imageDownloadTask = new ImageDownloadTask(this,routeMapping);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        imageDownloadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"");

        return super.onStartCommand(intent, flags, startId);
    }
}
