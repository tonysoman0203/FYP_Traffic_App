package com.example.tonyso.TrafficApp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.example.tonyso.TrafficApp.model.TimedBookMark;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class BookMarkService extends Service {

    String startTime, endTime;
    ArrayList<TimedBookMark> timedBookMarkArrayList;
    LocalBroadcastManager localBroadcastManager;
    RemainTimeTask remainTimeTask;

    static final public String CURR_TIME_RESULT = "com.example.tonyso.myapplication.BookmarkService.RESULT";

    static final public String MESSAGE = "com.example.tonyso.myapplication.BookmarkService.MSG";
    static final public String POS = "com.example.tonyso.myapplication.BookmarkService.POSITION";

    public BookMarkService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        timedBookMarkArrayList = (ArrayList<TimedBookMark>) intent.getSerializableExtra(Tab_BookMarkFragment.LIST);
        for (int i = 0; i < timedBookMarkArrayList.size(); i++) {
            remainTimeTask = new RemainTimeTask(timedBookMarkArrayList.get(i).getRemainTime(),
                    i, localBroadcastManager);
        }
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(remainTimeTask, 0, 360000);
        return super.onStartCommand(intent, flags, startId);
    }

    public static class RemainTimeTask extends TimerTask {
        int remainTime;
        int pos;
        LocalBroadcastManager localBroadcastManager;

        public RemainTimeTask(int remainTime, int i, LocalBroadcastManager localBroadcastManager) {
            System.out.print("RemainTimeTask");
            this.remainTime = remainTime;
            this.pos = i;
            this.localBroadcastManager = localBroadcastManager;
        }

        @Override
        public void run() {
            while (remainTime != 0) {
                sendResult(remainTime);
                remainTime--;
            }
            this.cancel();
        }

        public void sendResult(int message) {
            Intent intent = new Intent(CURR_TIME_RESULT);
            if (message != -1) {
                intent.putExtra(MESSAGE, message);
                intent.putExtra(POS, pos);
            }
            localBroadcastManager.sendBroadcast(intent);
        }
    }
}
