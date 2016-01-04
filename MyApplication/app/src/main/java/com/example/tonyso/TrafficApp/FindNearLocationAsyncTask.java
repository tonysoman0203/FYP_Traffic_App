package com.example.tonyso.TrafficApp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.tonyso.TrafficApp.adapter.InfoDetailAdapter;
import com.example.tonyso.TrafficApp.adapter.NearPlaceItemAdpater;
import com.example.tonyso.TrafficApp.model.NearbyLocation;

import java.util.List;

/**
 * Created by soman on 2016/1/3.
 */
public class FindNearLocationAsyncTask extends AsyncTask<Void, Void, List<NearbyLocation>> {
    private InfoDetailAdapter adapter;
    private Context context;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private double[] latlng;

    public FindNearLocationAsyncTask(InfoDetailAdapter adapter,
                                     Context context,
                                     RecyclerView recyclerView,
                                     ProgressBar progressBar, double[] latlng) {
        this.adapter = adapter;
        this.context = context;
        this.recyclerView = recyclerView;
        this.progressBar = progressBar;
        this.latlng = latlng;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setIndeterminate(true);
    }

    @Override
    protected List<NearbyLocation> doInBackground(Void... params) {
        return findNearLocation();
    }

    private List<NearbyLocation> findNearLocation() {
        NearbyPlacesHandler service = new NearbyPlacesHandler("AIzaSyAlwq8W2lK64AMJe7dbTh4babKOGXlSi5Y");
        List<NearbyLocation> findPlaces = service.findPlaces(latlng[0], latlng[1], "");
        return findPlaces;
    }

    @Override
    protected void onPostExecute(List aVoid) {
        super.onPostExecute(aVoid);
        progressBar.setIndeterminate(false);
        progressBar.setVisibility(View.GONE);
        recyclerView.setAdapter(new NearPlaceItemAdpater(context, aVoid));

    }


}
