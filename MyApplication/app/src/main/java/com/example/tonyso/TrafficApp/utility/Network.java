package com.example.tonyso.TrafficApp.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.tonyso.TrafficApp.MyApplication;

/**
 * Created by TonySoMan on 2/9/2015.
 */
public class Network {

    static Boolean isConnectingNetwork = false;
    static Boolean isConnectingMobileNetwork = false;
    static Boolean isConnectingWifiNetwork = false;

    static NetworkInfo.State isWIFINetwork , isMobileNetwork;

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
        }else {
            // return isConnectingNetwork;
//return isConnectingNetwork;
            isConnectingNetwork = isWIFINetwork == NetworkInfo.State.CONNECTED;
            return isConnectingNetwork;
        }
    }
}
