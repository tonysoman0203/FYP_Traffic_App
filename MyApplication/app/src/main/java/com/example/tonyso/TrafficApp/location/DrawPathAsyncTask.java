package com.example.tonyso.TrafficApp.location;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.fragment.NavTrafficSuggestDetailFragment;
import com.example.tonyso.TrafficApp.fragment.NavTrafficSuggestFragment;
import com.example.tonyso.TrafficApp.model.Place;
import com.example.tonyso.TrafficApp.model.RouteCCTV;
import com.example.tonyso.TrafficApp.utility.ErrorDialog;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by soman on 2016/2/10.
 *
 */
public class DrawPathAsyncTask extends AsyncTask<Void, Void, List<Map<String, Object>>> {
    private static final String KET_DISTANCE = "DISTANCE_STRING";
    private final String TAG = DrawPathAsyncTask.class.getCanonicalName();
    //private OnPathReadyListener onPathReadyListener;
    //private SwipeRefreshLayout swipe;
    Context context;
    private NavTrafficSuggestFragment navTrafficSuggestFragment;
    private ProgressDialog progressDialog;
    Place origin, destination;
    private List<RouteCCTV>cctvList;

    public void setCctvList(List<RouteCCTV> cctvList) {
        this.cctvList = cctvList;
    }

//    public void setOnPathReadyListener(OnPathReadyListener onPathReadyListener) {
//        this.onPathReadyListener = onPathReadyListener;
//    }

//    public void setSwipe(SwipeRefreshLayout swipe) {
//        this.swipe = swipe;
//    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setNavTrafficSuggestFragment(NavTrafficSuggestFragment navTrafficSuggestFragment) {
        this.navTrafficSuggestFragment = navTrafficSuggestFragment;
    }

    public void setOrigin(Place origin) {
        this.origin = origin;
    }

    public void setDestination(Place destination) {
        this.destination = destination;
    }

    public void setProgressDialog(ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
    }

    public DrawPathAsyncTask() {
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//        if (swipe == null) {
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Fetching Routes......");
            progressDialog.show();
//        } else {
//            swipe.setRefreshing(true);
//        }
    }

    @Override
    protected List<Map<String, Object>> doInBackground(Void... params) {
        LocationPlacesJsonParser jsonParser = new LocationPlacesJsonParser(context.getString(R.string.place_api_server_key));
        String url = jsonParser.makeDistanceURL(origin.getAddress().toString(), destination.getAddress().toString());
        Log.d(TAG, url);
        String json = jsonParser.getJSON(url);
        return getMapList(json);
    }

    private List<Map<String, Object>> getMapList(String json) {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> pathsMap = new HashMap<>();
        Map<String, Object> routeCCTVMap = new HashMap<>();
        Map<String, Object> DDMap = new HashMap<>();
        String[] disDur = new String[]{getDistance(json), getDuration(json)};
        DDMap.put(KET_DISTANCE, disDur);
        List<Double[]> path = drawPath(json);
        List<Object> cctvsInPaths = findCCTVInPaths(drawPathInLatLng(json));
        for (int index = 0; index < path.size(); index++) {
            pathsMap.put(String.valueOf(index), path.get(index));
        }
        for (int id = 0; id < cctvsInPaths.size(); id++) {
            RouteCCTV routeCCTV = (RouteCCTV) cctvsInPaths.get(id);
            LatLng ori = new LatLng(origin.getLatlngs()[0], origin.getLatlngs()[1]);
            LatLng cctv = new LatLng(routeCCTV.getLatLngs()[0], routeCCTV.getLatLngs()[1]);
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            String distance = decimalFormat.format(getDistanceFromLatLngInKm(ori, cctv));
            routeCCTV.setDistance(distance);
            routeCCTVMap.put(String.valueOf(id), routeCCTV);
        }
        list.add(pathsMap);
        list.add(routeCCTVMap);
        list.add(DDMap);
        return list;
    }
    @Override
    protected void onPostExecute(List<Map<String, Object>> s) {
        super.onPostExecute(s);
        progressDialog.hide();
        NavTrafficSuggestDetailFragment fragment = NavTrafficSuggestDetailFragment.newInstance(origin, destination, s);
        FragmentManager fm = navTrafficSuggestFragment.getChildFragmentManager();
        fragment.show(fm, NavTrafficSuggestDetailFragment.TAG);
    }

    private List<Object> findCCTVInPaths(List<LatLng> paths) {
        List<Object> route = new ArrayList<>();
        RouteCCTV routeCCTV;
        for (LatLng latLng : paths) {
            for(int i = 0 ;i<cctvList.size();i++){
                LatLng lat = new LatLng(cctvList.get(i).getLatLngs()[0],cctvList.get(i).getLatLngs()[1]);
                double diff = getDistanceFromLatLngInKm(latLng, lat);
                Log.e(TAG, String.format("%.2f", diff));
                if (diff <= 1) {
                    routeCCTV = cctvList.get(i);
                    route.add(routeCCTV);
                    cctvList.remove(routeCCTV);
                    i--;
                }
            }
        }
        return route;
    }

    public String getDistance(String result) {
        String distance = "";
        try {
            //Transform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONArray legs = routes.getJSONArray("legs");
            distance = legs.getJSONObject(0).getJSONObject("distance").getString("text");
        } catch (JSONException e) {
            ErrorDialog errorDialog = ErrorDialog.getInstance(navTrafficSuggestFragment.getContext());
            errorDialog.displayAlertDialog(e.getMessage());
        }
        return distance;
    }

    public String getDuration(String result) {
        String duration = "";
        try {
            //Transform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONArray legs = routes.getJSONArray("legs");
            duration = legs.getJSONObject(0).getJSONObject("duration").getString("text");
            if (duration.contains("分")) {
                duration = duration.replace("分", context.getResources().getString(R.string.Minute));
            } else {
                duration = duration.replace("min", context.getResources().getString(R.string.Minute));
            }
        } catch (JSONException e) {
            ErrorDialog errorDialog = ErrorDialog.getInstance(navTrafficSuggestFragment.getContext());
            errorDialog.displayAlertDialog(e.getMessage());
        }
        return duration;
    }

    private ArrayList<LatLng> decodePoly(String encoded) {

        ArrayList<LatLng> poly = new ArrayList<LatLng>();
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


    public ArrayList<Double[]> drawPath(String result) {
        ArrayList<Double[]> pathsList = new ArrayList<>();
        try {
            //Transform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);

            JSONArray legs = routes.getJSONArray("legs");
            Log.e(TAG, legs.toString());
            String distance = legs.getJSONObject(0).getJSONObject("distance").getString("text");
            Log.e(TAG, distance);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);
            for (LatLng latLng : list){
                Double[] paths = new Double[]{latLng.latitude,latLng.longitude};
                pathsList.add(paths);
            }
            return pathsList;
            //onPathReadyListener.onPathReady(list, getDuration(result));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pathsList;
    }

    public ArrayList<LatLng> drawPathInLatLng(String result) {
        ArrayList<LatLng> pathsList = new ArrayList<>();
        try {
            //Transform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);

            JSONArray legs = routes.getJSONArray("legs");
            Log.e(TAG, legs.toString());
            String distance = legs.getJSONObject(0).getJSONObject("distance").getString("text");
            Log.e(TAG, distance);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            pathsList = decodePoly(encodedString);
            return pathsList;
            //onPathReadyListener.onPathReady(list, getDuration(result));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pathsList;
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


}
