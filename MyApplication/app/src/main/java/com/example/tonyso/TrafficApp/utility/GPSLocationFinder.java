package com.example.tonyso.TrafficApp.utility;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.example.tonyso.TrafficApp.MainActivity;
import com.example.tonyso.TrafficApp.MyApplication;
import com.example.tonyso.TrafficApp.listener.WeatherRefreshListener;
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
    MainActivity context;
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
            List<Address> addresses = geocoder.getFromLocation(
                    latLng.latitude,
                    latLng.longitude, 10);
            for (int i =0;i<addresses.size();i++){
                Log.d("debug",addresses.get(i).getFeatureName());
            }
            if (addresses.size() > 0) {
                String address  = addresses.get(1).getFeatureName();
                Log.e("Test", "" + addresses.get(0).getLatitude() + "," + addresses.get(0).getLongitude());
               Log.e("Address",addresses.get(0).getFeatureName());
                Log.e("Address",addresses.get(0).getCountryName());
                weatherRefreshListener.onRefreshLocation(address);
            } else {
                Log.e(TAG,"No Address Found");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            //
            e.printStackTrace();
        }

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
