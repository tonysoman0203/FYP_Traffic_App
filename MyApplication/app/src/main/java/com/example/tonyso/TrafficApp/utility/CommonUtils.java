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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by TonySoMan on 2/9/2015.
 */
public class CommonUtils {

    ConnectivityManager conmgr;
    Context context;
    static Boolean isConnectingNetwork = false;
    static Boolean isConnectingMobileNetwork = false;
    static Boolean isConnectingWifiNetwork = false;

    static NetworkInfo.State isWIFINetwork , isMobileNetwork;

    public CommonUtils(Context context){
        this.context = context;
    }

    public static String initDate(Locale locale) {
        Calendar c = Calendar.getInstance();
        return new SimpleDateFormat("EEE , dd MMM yyyy", locale).format(c.getTime());
    }

    public static String getTime (){
        Calendar c = Calendar.getInstance();
        return new SimpleDateFormat("HH:mm:ss",Locale.TRADITIONAL_CHINESE).format(c.getTime());
    }

    /**
     * Method to verify google play services on the device
     * */
    public static boolean checkPlayServices(Activity context,int PLAY_SERVICES_RESOLUTION_REQUEST) {
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

    public static boolean checkInternetStatus(Context context) {
        ConnectivityManager conmgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conmgr.getActiveNetworkInfo();
        String networkName= "";
        if (!networkInfo.getTypeName().equals("")){
            networkName = networkInfo.getTypeName();
        }
        else{
            Log.e("Network Eroor","Network Error");
        }

        if (networkName.equals(MyApplication.WIFI)){
            if (conmgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI)==null){
                isWIFINetwork = null;
                isConnectingWifiNetwork = null;
            }else{
                isConnectingWifiNetwork = true;
                isWIFINetwork = conmgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
            }
        }
        else{
            if (conmgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)==null){
                isMobileNetwork = null;
                isConnectingMobileNetwork = false;
            }else{
                isMobileNetwork = conmgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
                isConnectingMobileNetwork = true;
            }
        }

        if (isMobileNetwork!=null) {
            if (isWIFINetwork == NetworkInfo.State.CONNECTED || (isMobileNetwork == NetworkInfo.State.CONNECTED)) {
                isConnectingNetwork = true;
                return isConnectingNetwork;

            } else if (conmgr.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
                    || conmgr.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTING
                    || (isWIFINetwork == NetworkInfo.State.DISCONNECTED
                    || isWIFINetwork == NetworkInfo.State.DISCONNECTING
                    || (isMobileNetwork == NetworkInfo.State.DISCONNECTED
                    || isMobileNetwork == NetworkInfo.State.DISCONNECTING))) {

                isConnectingNetwork = false;
            }
            return isConnectingNetwork;
        }else {
            if (isWIFINetwork == NetworkInfo.State.CONNECTED){
                isConnectingNetwork = true;
                return isConnectingNetwork;
            }else{
                isConnectingNetwork = false;
                return isConnectingNetwork;
            }
        }
    }

}
