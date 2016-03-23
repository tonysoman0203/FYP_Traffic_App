package com.example.tonyso.TrafficApp.utility;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.example.tonyso.TrafficApp.MyApplication;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by soman on 2016/3/24.
 */
public class CommonUtil {
    static Boolean isConnectingNetwork = false;
    static Boolean isConnectingMobileNetwork = false;
    static Boolean isConnectingWifiNetwork = false;

    static NetworkInfo.State isWIFINetwork, isMobileNetwork;

    public static boolean checkInternetStatus(Context context) {
        ConnectivityManager conmgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conmgr.getActiveNetworkInfo();
        String networkName = "";
        if (!networkInfo.getTypeName().equals("")) {
            networkName = networkInfo.getTypeName();
        } else {
            Log.e("Network Eroor", "Network Error");
        }

        if (networkName.equals(MyApplication.WIFI)) {
            if (conmgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI) == null) {
                isWIFINetwork = null;
                isConnectingWifiNetwork = null;
            } else {
                isConnectingWifiNetwork = true;
                isWIFINetwork = conmgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
            }
        } else {
            if (conmgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) == null) {
                isMobileNetwork = null;
                isConnectingMobileNetwork = false;
            } else {
                isMobileNetwork = conmgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
                isConnectingMobileNetwork = true;
            }
        }

        if (isMobileNetwork != null) {
            if (isWIFINetwork == NetworkInfo.State.CONNECTED || (isMobileNetwork == NetworkInfo.State.CONNECTED)) {
                isConnectingNetwork = true;
                // return isConnectingNetwork;

            } else if (conmgr.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
                    || conmgr.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTING
                    || (isWIFINetwork == NetworkInfo.State.DISCONNECTED
                    || isWIFINetwork == NetworkInfo.State.DISCONNECTING
                    || (isMobileNetwork == NetworkInfo.State.DISCONNECTED
                    || isMobileNetwork == NetworkInfo.State.DISCONNECTING))) {

                isConnectingNetwork = false;
            }
            return isConnectingNetwork;
        } else {
            // return isConnectingNetwork;
//return isConnectingNetwork;
            isConnectingNetwork = isWIFINetwork == NetworkInfo.State.CONNECTED;
            return isConnectingNetwork;
        }
    }

    /**
     * Method to verify google play services on the device
     */
    public static boolean checkPlayServices(Activity context, int PLAY_SERVICES_RESOLUTION_REQUEST) {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, context,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(context,
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                context.finish();
            }
            return false;
        }
        return true;
    }

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
