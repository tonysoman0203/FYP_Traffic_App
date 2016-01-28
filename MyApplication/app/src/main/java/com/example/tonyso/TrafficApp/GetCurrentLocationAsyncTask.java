package com.example.tonyso.TrafficApp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.tonyso.TrafficApp.listener.WeatherRefreshListener;
import com.example.tonyso.TrafficApp.model.Place;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by TonySo on 20/1/16.
 */
public class GetCurrentLocationAsyncTask extends AsyncTask<Void, Place, Place> {
    public Context location;
    private WeatherRefreshListener weatherListener;
    private LatLng latlng;
    private MyApplication context;

    public GetCurrentLocationAsyncTask() {
    }

    @Override
    protected Place doInBackground(Void... params) {
        String url = constructURL();
        Log.e(getClass().getCanonicalName(), url);
        String result = LocationPlacesJsonParser.getUrlContents(url);
        String currentLocation = "";
        Place location = null;
        try {
            JSONObject jsonObject = new JSONObject(result);
            location = new Place();
            String status = jsonObject.getString("status");
            Log.i("status", status);
            if (status.equalsIgnoreCase("ok")) {
                JSONArray results = jsonObject.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject json = results.getJSONObject(i);
                    JSONArray address = json.getJSONArray("address_components");
                    String types = address.getJSONObject(0).getString("types");
                    Log.i(getClass().getCanonicalName(), types);
                    if (types.equalsIgnoreCase("[\"neighborhood\",\"political\"]")) {
                        currentLocation = address.getJSONObject(0).getString("long_name");
                        location.setName(currentLocation);
                        //return location;
                    } else if (types.equalsIgnoreCase("[\"premise\"]")) {
                        location.setAddress(json.getString("formatted_address"));
                        Log.i(getClass().getCanonicalName(), location.getAddress().toString());
                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return location;
    }

    private String constructURL() {
        StringBuffer urlbuffer = new StringBuffer("http://maps.googleapis.com/maps/api/geocode/json?latlng=");
        urlbuffer.append(latlng.latitude);
        urlbuffer.append(",");
        urlbuffer.append(latlng.longitude);
        urlbuffer.append("&sensor=true");
        return urlbuffer.toString();
    }

    @Override
    protected void onPostExecute(Place address) {
        super.onPostExecute(address);
        Log.d(getClass().getCanonicalName(), address.getName().toString());
        if (weatherListener != null) {
            weatherListener.onRefreshLocation(address.getName().toString());
        } else {
            context.locate = address.getAddress().toString();
        }
    }

    public void setWeatherListener(WeatherRefreshListener weatherListener) {
        this.weatherListener = weatherListener;
    }

    public void setApplication(MyApplication context) {
        this.context = context;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }

}
