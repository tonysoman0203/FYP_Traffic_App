package com.example.tonyso.TrafficApp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.tonyso.TrafficApp.adapter.InfoDetailAdapter;
import com.example.tonyso.TrafficApp.adapter.NearPlaceItemAdpater;
import com.example.tonyso.TrafficApp.model.NearbyLocation;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by soman on 2016/1/3.
 */
public class FindNearLocationAsyncTask extends AsyncTask<Void, Void, List<NearbyLocation>> {
    private InfoDetailAdapter adapter;
    private Context context;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LatLng latlng;
    private GoogleApiClient mGoogleApiClient;
    private String currLocation;

    public FindNearLocationAsyncTask(InfoDetailAdapter adapter,
                                     Context context,
                                     RecyclerView recyclerView,
                                     ProgressBar progressBar, double[] latlng, GoogleApiClient googleApiClient, String s) {
        this.adapter = adapter;
        this.context = context;
        this.recyclerView = recyclerView;
        this.progressBar = progressBar;
        this.latlng = new LatLng(latlng[0], latlng[1]);
        this.mGoogleApiClient = googleApiClient;
        this.currLocation = s;
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
        NearByPlacesJsonParser service = new NearByPlacesJsonParser(context.getString(R.string.place_api_server_key));
        return service.findPlaces(latlng.latitude, latlng.longitude, "", currLocation);
    }

    @Override
    protected void onPostExecute(List<NearbyLocation> aVoid) {
        super.onPostExecute(aVoid);
        progressBar.setIndeterminate(false);
        progressBar.setVisibility(View.GONE);
        recyclerView.setAdapter(new NearPlaceItemAdpater(context, aVoid, mGoogleApiClient));

    }


}
