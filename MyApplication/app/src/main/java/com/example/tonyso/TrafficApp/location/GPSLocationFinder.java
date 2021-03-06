package com.example.tonyso.TrafficApp.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.example.tonyso.TrafficApp.MainActivity;
import com.example.tonyso.TrafficApp.MyApplication;
import com.example.tonyso.TrafficApp.listener.WeatherRefreshListener;
import com.example.tonyso.TrafficApp.utility.ErrorDialog;
import com.example.tonyso.TrafficApp.utility.LanguageSelector;
import com.example.tonyso.TrafficApp.utility.ShareStorage;
import com.example.tonyso.TrafficApp.utility.StoreObject;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


/**
 * Created by TonySoMan on 31/5/2015.
 */
public class GPSLocationFinder implements LocationListener {
    private static final String TAG = GPSLocationFinder.class.getSimpleName();
    Context context;
    Location mLocation;
    ErrorDialog errorDialog;
    LanguageSelector languageSelector ;
    WeatherRefreshListener weatherRefreshListener;

    public GPSLocationFinder() {
    }

    private GPSLocationFinder(GPSLocationFinder gpsLocationFinder) {
        this.context = gpsLocationFinder.context;
        this.mLocation = gpsLocationFinder.mLocation;
        this.errorDialog = ErrorDialog.getInstance(this.context);
        this.languageSelector = LanguageSelector.getInstance(this.context);
        this.weatherRefreshListener = gpsLocationFinder.weatherRefreshListener;
    }

    //public static GPSLocationFinder getInstance()

    public GPSLocationFinder build() {
        return new GPSLocationFinder(this);
    }

    public GPSLocationFinder setContext(MainActivity context) {
        this.context = context;
        return this;
    }

    public GPSLocationFinder setWeatherRefreshListener(WeatherRefreshListener weatherRefreshListener) {
        this.weatherRefreshListener = weatherRefreshListener;
        return this;
    }

    public void convertLatLongToAddress(Location location, Locale locale) {
       //location = mLocationManager.getLastKnownLocation(locationprovider);
        try {
            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
            Geocoder geocoder = new Geocoder(context,locale);
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 10);
            if (addresses.size() > 0) {
                Log.d(TAG, addresses.toString());
                Log.e("Test", "" + addresses.get(0).getLatitude() + "," + addresses.get(0).getLongitude());
                Log.e("Address: ", addresses.get(0).getFeatureName());
                Log.e("Address", addresses.get(0).getAddressLine(1));
                String district = addresses.get(1).getFeatureName();
                String address = addresses.get(0).getAddressLine(0).concat(addresses.get(0).getAddressLine(1));

                ShareStorage.saveData(ShareStorage.StorageType.SHARED_PREFERENCE,
                        new StoreObject<Object>(false, "name", district), ShareStorage.SP.ProtectedData, context);
                ShareStorage.saveData(ShareStorage.StorageType.SHARED_PREFERENCE,
                        new StoreObject<Object>(false, "address", address), ShareStorage.SP.ProtectedData, context);
                ShareStorage.saveData(ShareStorage.StorageType.SHARED_PREFERENCE,
                        new StoreObject<Object>(false, "district", district), ShareStorage.SP.ProtectedData, context);
                ShareStorage.saveData(ShareStorage.StorageType.SHARED_PREFERENCE,
                        new StoreObject<Object>(false, "lat", location.getLatitude()), ShareStorage.SP.ProtectedData, context);
                ShareStorage.saveData(ShareStorage.StorageType.SHARED_PREFERENCE,
                        new StoreObject<Object>(false, "lng", location.getLongitude()), ShareStorage.SP.ProtectedData, context);
                weatherRefreshListener.onRefreshLocation(district);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            // Get JSON BY WebService
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            GetLocationAsyncTask asyncTask = new GetLocationAsyncTask();
            asyncTask.setWeatherListener(weatherRefreshListener);
            asyncTask.setLatlng(latLng);
            asyncTask.execute(MyApplication.CURR_LANG);
        }

    }

    public Location getmLocation() {
        return mLocation;
    }

    public GPSLocationFinder setmLocation(Location mLocation) {
        this.mLocation = mLocation;
        return this;
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        if (languageSelector.getLanguage().equals(MyApplication.Language.ENGLISH)) {
            convertLatLongToAddress(mLocation, Locale.ENGLISH);
        }else{
            convertLatLongToAddress(mLocation, Locale.TRADITIONAL_CHINESE);
        }
    }


}
