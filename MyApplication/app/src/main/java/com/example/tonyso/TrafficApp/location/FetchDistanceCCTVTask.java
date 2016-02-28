package com.example.tonyso.TrafficApp.location;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.tonyso.TrafficApp.adapter.CCTVListAdapter;
import com.example.tonyso.TrafficApp.fragment.NavSuggestMapFragment;
import com.example.tonyso.TrafficApp.fragment.NavTrafficSuggestCCTVFragment;
import com.example.tonyso.TrafficApp.model.Place;
import com.example.tonyso.TrafficApp.model.RouteCCTV;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xdeveloper on 22/2/16.
 */
public class FetchDistanceCCTVTask extends AsyncTask<String, Void, List<RouteCCTV>> {

    private static final String TAG = FetchDistanceCCTVTask.class.getCanonicalName();
    List<RouteCCTV> cctvList;
    Place origin;
    Place destination;
    double totat_distance;
    private RecyclerView recyclerView;
    private NavTrafficSuggestCCTVFragment fragment;

    public FetchDistanceCCTVTask() {

    }

    public static double getDistanceFromLatLngInKm(LatLng c1, LatLng c2) {
        int R = 6371; // Radius of the earth in km

        double lat1 = c1.latitude;
        double lat2 = c2.latitude;

        double lon1 = c1.longitude;
        double lon2 = c2.longitude;

        double dLat = deg2rad(lat2 - lat1);
        double dLon = deg2rad(lon2 - lon1);

        double a =
                Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                        Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                                Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c; // Distance in km
        return d;
    }

    public static double deg2rad(double deg) {
        return deg * (Math.PI / 180);
    }

    public List<RouteCCTV> getCctvList() {
        return cctvList;
    }

    public void setCctvList(List<RouteCCTV> cctvList) {
        this.cctvList = cctvList;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public void setFragment(NavTrafficSuggestCCTVFragment fragment) {
        this.fragment = fragment;
    }

    public Place getOrigin() {
        return origin;
    }

    public void setOrigin(Place origin) {
        this.origin = origin;
    }

    public double getTotat_distance() {
        return totat_distance;
    }

    public void setTotat_distance(double totat_distance) {
        this.totat_distance = totat_distance;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    public Place getDestination() {
        return destination;
    }

    public void setDestination(Place destination) {
        this.destination = destination;
    }

    @Override
    protected List<RouteCCTV> doInBackground(String... params) {
        LatLng ori = new LatLng(origin.getLatlngs()[0], origin.getLatlngs()[1]);
        LatLng dest = new LatLng(destination.getLatlngs()[0], destination.getLatlngs()[1]);
        List<LatLng>paths = NavSuggestMapFragment.cctvLatLng;

        for(LatLng latLng : paths){
            Log.e(TAG,String.format("%.2f",getDistanceFromLatLngInKm(latLng,ori)));
        }
        return null;

    }

    @Override
    protected void onPostExecute(List<RouteCCTV> s) {
        super.onPostExecute(s);
//        CCTVListAdapter cctvListAdapter = new CCTVListAdapter(s, fragment.getContext());
//        cctvListAdapter.setImageLoader(fragment.getImageLoader());
//        cctvListAdapter.setDisplayImageOptions(fragment.getImageOptions());
//        recyclerView.setAdapter(cctvListAdapter);
//        for (RouteCCTV r : s) {
//            MarkerOptions markerOptions = new MarkerOptions();
//            markerOptions.position(new LatLng(r.getLatLngs()[0], r.getLatLngs()[1]));
//            NavSuggestMapFragment.getGoogleMap().addMarker(markerOptions);
//        }
    }


}
