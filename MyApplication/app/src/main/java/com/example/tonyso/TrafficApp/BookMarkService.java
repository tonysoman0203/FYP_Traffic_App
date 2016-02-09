package com.example.tonyso.TrafficApp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.example.tonyso.TrafficApp.adapter.InfoDetailAdapter;
import com.example.tonyso.TrafficApp.fragment.Tab_BookMarkFragment;
import com.example.tonyso.TrafficApp.model.TimedBookMark;
import com.example.tonyso.TrafficApp.utility.DateTime;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class BookMarkService extends Service {
    static final public String CURR_TIME_RESULT = "com.example.tonyso.myapplication.BookmarkService.RESULT";
    static final public String MESSAGE = "com.example.tonyso.myapplication.BookmarkService.MSG";
    static final public String CHECK_SUM_WITH_DATA = "com.example.tonyso.myapplication.BookmarkService.CHECK_SUM";
    static final public int CHECK_SUM_WITHOUT_DATA = -1;
    ArrayList<TimedBookMark> timedBookMarkArrayList;
    LocalBroadcastManager localBroadcastManager;
    RemainTimeTask remainTimeTask;
    Timer timer;

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
        if (intent.getSerializableExtra(Tab_BookMarkFragment.LIST) != null) {
            timedBookMarkArrayList = (ArrayList<TimedBookMark>) intent.getSerializableExtra(Tab_BookMarkFragment.LIST);
            if (timedBookMarkArrayList.size() > 0) {
                remainTimeTask = new RemainTimeTask(timedBookMarkArrayList, localBroadcastManager);
                timer = new Timer();
                timer.scheduleAtFixedRate(remainTimeTask, 0, 60000);
            }
        } else {
            return super.onStartCommand(intent, flags, startId);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {

            timer.cancel();
        }

    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    //Calculate Remaining Time based on Current Time and Target
    public static class RemainTimeTask extends TimerTask {
        LocalBroadcastManager localBroadcastManager;
        ArrayList<TimedBookMark> list;

        public RemainTimeTask(ArrayList<TimedBookMark> arrayList, LocalBroadcastManager localBroadcastManager) {
            System.out.print("RemainTimeTask Constructor ");
            this.list = arrayList;
            this.localBroadcastManager = localBroadcastManager;
        }

        @Override
        public void run() {
            //Calculate TargetTime And Current Time / StartTime;
            this.list = calculateDateTimeOnEachBookmark();
            sendResult(list);
        }

        private ArrayList calculateDateTimeOnEachBookmark() {
            ArrayList<TimedBookMark> timedBookMarks = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                TimedBookMark timedBookMark = list.get(i);
                String currTime = DateTime.getCurrentDateTime();
                String targetTime = timedBookMark.getTargetTime();
                timedBookMark.setRemainTime(InfoDetailAdapter.getRemainTime(currTime, targetTime));
                timedBookMarks.add(timedBookMark);
            }
            return timedBookMarks;
        }

        public void sendResult(ArrayList<TimedBookMark> list) {
            Intent intent = new Intent(CURR_TIME_RESULT);
            if (list.size() > 0) {
                intent.putExtra(MESSAGE, list);
                intent.putExtra(CHECK_SUM_WITH_DATA, 0);
            }
            localBroadcastManager.sendBroadcast(intent);
        }
    }

}
