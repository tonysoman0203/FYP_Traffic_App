package com.example.tonyso.TrafficApp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.example.tonyso.TrafficApp.utility.CommonUtils;

import java.util.Timer;
import java.util.TimerTask;

public class CountDownService extends Service {

    MyTimerTask myTimerTask;

    LocalBroadcastManager localBroadcastManager;

    static final public String CURR_TIME_RESULT = "com.example.tonyso.myapplication.CountDownService.TIMERESULT";

    static final public String MESSAGE = "com.example.tonyso.myapplication.CountDownService.MSG";


    public CountDownService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimerTask(localBroadcastManager),0,1000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private class MyTimerTask extends TimerTask{

        LocalBroadcastManager localBroadcastManager;
        public MyTimerTask(LocalBroadcastManager localBroadcastManager){
            this.localBroadcastManager = localBroadcastManager;
        }

        @Override
        public void run(){
            String currTime = CommonUtils.getTime();
            sendResult(currTime);
        }
        public void sendResult(String message) {
            Intent intent = new Intent(CURR_TIME_RESULT);
            if(message != null)
                intent.putExtra(MESSAGE, message);
            localBroadcastManager.sendBroadcast(intent);
        }
    }
}
