package com.example.tonyso.TrafficApp.location;

import android.os.AsyncTask;
import android.util.Log;

import com.example.tonyso.TrafficApp.model.Place;
import com.example.tonyso.TrafficApp.model.RouteCCTV;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by xdeveloper on 22/2/16.
 */
public class FetchDistanceCCTVTask extends AsyncTask<String, Void, String> {

    private static final String TAG = FetchDistanceCCTVTask.class.getCanonicalName();
    List<RouteCCTV> cctvList;
    Place origin;
    Place destination;

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

    public Place getOrigin() {
        return origin;
    }

    public void setOrigin(Place origin) {
        this.origin = origin;
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
    protected String doInBackground(String... params) {
        LatLng ori = new LatLng(origin.getLatlngs()[0], origin.getLatlngs()[1]);
        LatLng dest = new LatLng(destination.getLatlngs()[0], destination.getLatlngs()[1]);
        String oriDist = origin.getDistrict().toString();
        for (RouteCCTV c : cctvList) {
            if (c.getRegion()[0].contentEquals(oriDist) || c.getRegion()[1].contentEquals(oriDist)) {
                LatLng cctvDis = new LatLng(c.getLatLngs()[0], c.getLatLngs()[1]);
                double km = getDistanceFromLatLngInKm(ori, cctvDis);
                double cctvTodest = getDistanceFromLatLngInKm(cctvDis, dest);
                Log.e(TAG, String.format("Ori:%s , to cctv, in %.2f km", c.getRegion()[0], km));
            }

        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }


}
