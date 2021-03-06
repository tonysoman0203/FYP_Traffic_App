package com.example.tonyso.TrafficApp.location;

import android.util.Log;

import com.example.tonyso.TrafficApp.MyApplication;
import com.example.tonyso.TrafficApp.model.NearbyLocation;
import com.example.tonyso.TrafficApp.model.Place;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by soman on 2016/1/3.
 */
public class LocationPlacesJsonParser {
    static String TAG = NearbyLocation.class.getName();
    private String API_KEY;

    public LocationPlacesJsonParser(String API_KEY) {
        this.API_KEY = API_KEY;
    }

    public static String getUrlContents(String theUrl) {
        StringBuilder content = new StringBuilder();

        try {
            URL url = new URL(theUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()), 8);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();

        }

        return content.toString();
    }

    public String getJSON(String url) {
        return getUrlContents(url);
    }

    public List<NearbyLocation> findPlaces(double latitude, double longitude, String placeSpacification, String currLocation) {
        String urlString = makePlacesApiUrl(latitude, longitude, placeSpacification);
        try {
            String json = getJSON(urlString);

            System.out.println(json);
            JSONObject object = new JSONObject(json);
            JSONArray array = object.getJSONArray("results");
            ArrayList<NearbyLocation> arrayList = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                try {
                    NearbyLocation place = getNearByLocation(array.getJSONObject(i));
                    Log.v("Places Services ", "" + place);
                    arrayList.add(place);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            for (int i = 0; i < arrayList.size(); i++) {
                String distanceUrl = makeDistanceURL(currLocation, String.valueOf(arrayList.get(i).getName()));
                try {
                    json = getJSON(distanceUrl);
                    Log.d("JSON Distance= ", json);
                    arrayList.get(i).setDistanceInKm(getDistanceByJSON(json));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return arrayList;
        } catch (JSONException ex) {
            Logger.getLogger(LocationPlacesJsonParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String makeDistanceURL(String currentLocation, String destination) {
        StringBuilder urlString = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        try {
            if (MyApplication.CURR_LANG.equals(MyApplication.Language.ZH_HANT)) {
                urlString.append("origin=" + URLEncoder.encode(currentLocation, "UTF-8"));
                urlString.append("&");
                urlString.append("destination=" + URLEncoder.encode(destination, "UTF-8"));
                urlString.append("&sensor=false");
                //urlString.append("&key=" + API_KEY);
                urlString.append("&language=zh-tw");
            } else {
                urlString.append("origin=" + URLEncoder.encode(currentLocation, "UTF-8"));
                urlString.append("&");
                urlString.append("destination=" + URLEncoder.encode(destination, "UTF-8"));
                urlString.append("&sensor=false&mode=driving&alternatives=true");
                //urlString.append("&key=" + API_KEY);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return urlString.toString();
    }

    //https://maps.googleapis.com/maps/api/place/search/json?location=28.632808,77.218276&radius=500&types=atm&sensor=false&key=<key>
    private String makePlacesApiUrl(double latitude, double longitude, String place) {
        StringBuilder urlString = new StringBuilder("https://maps.googleapis.com/maps/api/place/search/json?");

        if (place.equals("")) {
            urlString.append("&location=");
            urlString.append(Double.toString(latitude));
            urlString.append(",");
            urlString.append(Double.toString(longitude));
            urlString.append("&radius=500");
            //   urlString.append("&types="+place);
            urlString.append("&sensor=false&key=" + API_KEY);
        } else {
            urlString.append("&location=");
            urlString.append(Double.toString(latitude));
            urlString.append(",");
            urlString.append(Double.toString(longitude));
            urlString.append("&radius=1000");
            urlString.append("&types=" + place);
            urlString.append("&sensor=false&key=" + API_KEY);
        }


        return urlString.toString();
    }

    private NearbyLocation getNearByLocation(JSONObject jsonObject) {
        try {
            NearbyLocation result = new NearbyLocation();
            JSONObject geometry = (JSONObject) jsonObject.get("geometry");
            JSONObject location = (JSONObject) geometry.get("location");
            result.setLatlngs(new double[]{(Double) location.get("lat"), (Double) location.get("lng")});
            result.setIcon(jsonObject.getString("icon"));
            result.setName(jsonObject.getString("name"));
            result.setPlaceId(jsonObject.getString("place_id"));
            if (!jsonObject.isNull("vicinity")) {
                result.setVicinity(jsonObject.getString("vicinity"));
            }
            if (!jsonObject.isNull("photos")) {
                JSONArray photos = jsonObject.getJSONArray("photos");
                result.setPhotoReference(photos.getJSONObject(0).getString("photo_reference"));
            }
            return result.build();
        } catch (JSONException ex) {
            Logger.getLogger(Place.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private String getDistanceByJSON(String jsonString) {
        String status = null;
        try {
            final JSONObject jsonObject = new JSONObject(jsonString);
            status = jsonObject.getString("status");
            Log.e(TAG, status);
            JSONArray routeArray = jsonObject.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONArray newTempARr = routes.getJSONArray("legs");
            JSONObject newDisTimeOb = newTempARr.getJSONObject(0);
            JSONObject distOb = newDisTimeOb.getJSONObject("distance");
            JSONObject timeOb = newDisTimeOb.getJSONObject("duration");
            Log.i("Diatance :", distOb.getString("text"));
            Log.i("Time :", timeOb.getString("text"));
            return distOb.getString("text");
        } catch (JSONException e) {
            return status;
        }
    }
}
