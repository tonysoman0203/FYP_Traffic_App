package com.example.tonyso.TrafficApp.location;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.fragment.NavTrafficSuggestFragment;
import com.example.tonyso.TrafficApp.model.RouteCCTV;
import com.example.tonyso.TrafficApp.model.RouteSpeedMap;
import com.example.tonyso.TrafficApp.utility.ErrorDialog;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by soman on 2016/2/10.
 */
public class DrawPathAsyncTask extends AsyncTask<Void, Void, String> {
    private NavTrafficSuggestFragment navTrafficSuggestFragment;
    private final String TAG = DrawPathAsyncTask.class.getCanonicalName();
    Context context;
    GoogleMap mMap;
    String[] location;
    List<RouteCCTV> routeCCTVs;
    List<RouteSpeedMap> routeSpeedMaps;

    List<Map<String, Float>> distanceList = new ArrayList<>();

    private ProgressDialog progressDialog;

    public DrawPathAsyncTask(NavTrafficSuggestFragment navTrafficSuggestFragment,
                             Context context, GoogleMap map, String[] location,
                             List<RouteCCTV> routeList, List<RouteSpeedMap> routeSpeedMap) {
        this.navTrafficSuggestFragment = navTrafficSuggestFragment;
        this.context = context;
        this.mMap = map;
        this.location = location;
        this.routeCCTVs = routeList;
        this.routeSpeedMaps = routeSpeedMap;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Fetching Routes.......");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(Void... params) {
        LocationPlacesJsonParser jsonParser = new LocationPlacesJsonParser(context.getString(R.string.place_api_server_key));
        String url = jsonParser.makeDistanceURL(location[0], location[1], null, null);
        return jsonParser.getJSON(url);
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.e(TAG, s);
        progressDialog.hide();
        if (s != null) {
            drawPath(s);
        }
    }

    public void drawPath(String result) {
        float[] distances = new float[3];
        try {
            //Transform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);

            LatLng startLatLng = list.get(0);
            LatLng endLng = list.get(list.size() - 1);

            Location.distanceBetween(startLatLng.latitude, startLatLng.longitude, endLng.latitude, endLng.longitude, distances);
            Log.e("Debug", distances[0] + " " + distances[1] + " " + distances[2]);


            Polyline line = mMap.addPolyline(new PolylineOptions()
                            .addAll(list)
                            .width(12)
                            .color(Color.parseColor("#05b1fb"))//Google maps blue color
                            .geodesic(true)
            );


        } catch (JSONException e) {
            ErrorDialog errorDialog = ErrorDialog.getInstance(navTrafficSuggestFragment.getContext());
            errorDialog.displayAlertDialog(e.getMessage());
        }
    }
}
