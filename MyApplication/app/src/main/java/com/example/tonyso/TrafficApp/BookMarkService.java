package com.example.tonyso.TrafficApp;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import com.example.tonyso.TrafficApp.model.TimedBookMark;

import java.util.ArrayList;

public class BookMarkService extends Service {

    String startTime, endTime;
    ArrayList<TimedBookMark> timedBookMarkArrayList;
    LocalBroadcastManager localBroadcastManager;
    RemainTimeTask remainTimeTask;

    static final public String CURR_TIME_RESULT = "com.example.tonyso.myapplication.BookmarkService.RESULT";

    static final public String MESSAGE = "com.example.tonyso.myapplication.BookmarkService.MSG";

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
            remainTimeTask = new RemainTimeTask(
                    new String[]{timedBookMarkArrayList.get(i).getStartTime(),
                            timedBookMarkArrayList.get(i).getTargetTime()},
                    i, localBroadcastManager);
        }
        remainTimeTask.execute();

        return super.onStartCommand(intent, flags, startId);
    }

    public static class RemainTimeTask extends AsyncTask<String, Void, Integer> {
        String startTime, endTime;
        int pos;
        LocalBroadcastManager localBroadcastManager;

        public RemainTimeTask(String[] strings, int pos, LocalBroadcastManager localBroadcastManager) {
            startTime = strings[0];
            endTime = strings[1];
            this.pos = pos;
            this.localBroadcastManager = localBroadcastManager;
        }

        @Override
        protected Integer doInBackground(String... params) {
            //int result = Integer.parseInt(endTime)-Integer.parseInt(startTime);
            //if (String.valueOf(result)=="")
            return -1;

            //return result;
        }

        @Override
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);
            Intent intent = new Intent(CURR_TIME_RESULT);
            intent.putExtra(MESSAGE, s);
            localBroadcastManager.sendBroadcast(intent);
        }
    }
}
