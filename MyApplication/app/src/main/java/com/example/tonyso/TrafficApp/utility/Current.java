package com.example.tonyso.TrafficApp.utility;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by soman on 2016/2/13.
 */
public class Current {

    public static StringBuffer getCurrentLocationName(Context context) {
        StringBuffer sb = new StringBuffer();
        sb.append(ShareStorage.retrieveData("name", ShareStorage.SP.ProtectedData, context));
        return sb;
    }

    public static StringBuffer getCurrentDistrict(Context context) {
        StringBuffer sb = new StringBuffer();
        sb.append(ShareStorage.retrieveData("district", ShareStorage.SP.ProtectedData, context));
        return sb;
    }

    public static StringBuffer getCurrentAddress(Context context) {
        StringBuffer sb = new StringBuffer();
        sb.append(ShareStorage.retrieveData("address", ShareStorage.SP.ProtectedData, context));
        return sb;
    }

    public static LatLng getCurrentLocationByLatLng(Context context) {
        double[] lls = new double[]{
                Double.parseDouble(ShareStorage.retrieveData("lat", ShareStorage.SP.ProtectedData, context)),
                Double.parseDouble(ShareStorage.retrieveData("lng", ShareStorage.SP.ProtectedData, context))
        };
        return new LatLng(lls[0], lls[1]);
    }

    public static double[] getLatLngInDouble(Context context) {
        double[] lls = new double[]{
                Double.parseDouble(ShareStorage.retrieveData("lat", ShareStorage.SP.ProtectedData, context)),
                Double.parseDouble(ShareStorage.retrieveData("lng", ShareStorage.SP.ProtectedData, context))
        };
        return lls;
    }
}
