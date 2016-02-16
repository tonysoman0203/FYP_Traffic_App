package com.example.tonyso.TrafficApp.location;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.tonyso.TrafficApp.MyApplication;
import com.example.tonyso.TrafficApp.fragment.NavTrafficSuggestFragment;
import com.example.tonyso.TrafficApp.listener.WeatherRefreshListener;
import com.example.tonyso.TrafficApp.model.Place;
import com.example.tonyso.TrafficApp.utility.ShareStorage;
import com.example.tonyso.TrafficApp.utility.StoreObject;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by TonySo on 20/1/16.
 */
public class GetLocationAsyncTask extends AsyncTask<String, Place, Place> {
    public Context context;
    private WeatherRefreshListener weatherListener;
    private LatLng latlng;
    private GoogleMap googleMap;

    public GetLocationAsyncTask() {
    }

    @Override
    protected Place doInBackground(String... params) {
        String url = constructURL(params[0]);
        Log.e(getClass().getCanonicalName(), url);
        String result = LocationPlacesJsonParser.getUrlContents(url);
        String district = "";
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
                    JSONArray address_components = json.getJSONArray("address_components");
                    for (int ac = 0; i < address_components.length() - 1; ac++) {
                        String types = address_components.getJSONObject(ac).getString("types");
                        Log.i(getClass().getCanonicalName(), "Type:" + types);
                        if (types.equalsIgnoreCase("[\"neighborhood\",\"political\"]")) {
                            district = address_components.getJSONObject(ac).getString("long_name");
                            Log.i(getClass().getCanonicalName(), "district=" + district);
                            location.setDistrict(district);
                        } else if (types.equalsIgnoreCase("[\"premise\")")) {
                            Log.e(getClass().getCanonicalName(), address_components.getJSONObject(ac).getString("long_name"));
                            location.setName(address_components.getJSONObject(ac).getString("long_name"));
                        } else {
                            location.setAddress(json.getString("formatted_address"));
                            Log.i(getClass().getCanonicalName(), location.getAddress().toString());
                        }
                        JSONObject geo = json.getJSONObject("geometry");
                        Log.e(getClass().getCanonicalName(), geo.toString());
                        JSONObject l = geo.getJSONObject("location");
                        Log.e(getClass().getCanonicalName(), l.toString());
                        location.setLatlngs(getLatlngs(l));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return location;
    }

    private double[] getLatlngs(JSONObject l) throws JSONException {
        double lat = l.getDouble("lat");
        double lng = l.getDouble("lng");
        //LatLng latLng = new LatLng(lat, lng);
        return new double[]{lat, lng};
    }

    private String constructURL(String locale) {
        StringBuffer urlbuffer = new StringBuffer("http://maps.googleapis.com/maps/api/geocode/json?latlng=");
        if (locale.equals(MyApplication.Language.ZH_HANT)) {
            urlbuffer.append(latlng.latitude);
            urlbuffer.append(",");
            urlbuffer.append(latlng.longitude);
            urlbuffer.append("&sensor=true");
            urlbuffer.append("&language=zh-tw");
        } else {
            urlbuffer.append(latlng.latitude);
            urlbuffer.append(",");
            urlbuffer.append(latlng.longitude);
            urlbuffer.append("&sensor=true");
        }
        return urlbuffer.toString();
    }

    @Override
    protected void onPostExecute(Place address) {
        super.onPostExecute(address);
        //Log.d(getClass().getCanonicalName(), address.getName().toString());
        if (NavTrafficSuggestFragment.placeMap != null) {
            address.setPlaceId(String.valueOf(NavTrafficSuggestFragment.place_ids++));
            NavTrafficSuggestFragment.placeMap.put(address.getAddress().toString(), address);
            Log.d(getClass().getCanonicalName(), "Put a address into Map success..." + address.toString());
        }

        if (weatherListener != null) {
            ShareStorage.saveData(ShareStorage.StorageType.SHARED_PREFERENCE,
                    new StoreObject<Object>(false, "name", address.getName()), ShareStorage.SP.ProtectedData, context);
            ShareStorage.saveData(ShareStorage.StorageType.SHARED_PREFERENCE,
                    new StoreObject<Object>(false, "address", address.getAddress().toString()), ShareStorage.SP.ProtectedData, context);
            ShareStorage.saveData(ShareStorage.StorageType.SHARED_PREFERENCE,
                    new StoreObject<Object>(false, "district", address.getDistrict()), ShareStorage.SP.ProtectedData, context);
            ShareStorage.saveData(ShareStorage.StorageType.SHARED_PREFERENCE,
                    new StoreObject<Object>(false, "lat", address.getLatlngs()[0]), ShareStorage.SP.ProtectedData, context);
            ShareStorage.saveData(ShareStorage.StorageType.SHARED_PREFERENCE,
                    new StoreObject<Object>(false, "lng", address.getLatlngs()[1]), ShareStorage.SP.ProtectedData, context);
            weatherListener.onRefreshLocation(address.getName().toString());
        } else {
            MarkerOptions markerOptions = new MarkerOptions()
                    .title(address.getAddress().toString())
                    .position(latlng);
            googleMap.addMarker(markerOptions);
        }
    }

    public void setWeatherListener(WeatherRefreshListener weatherListener) {
        this.weatherListener = weatherListener;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }

    public GoogleMap getGoogleMap() {
        return googleMap;
    }

    public void setGoogleMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

}
