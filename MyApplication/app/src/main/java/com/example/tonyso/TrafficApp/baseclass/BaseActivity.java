package com.example.tonyso.TrafficApp.baseclass;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import com.example.tonyso.TrafficApp.MyApplication;
import com.example.tonyso.TrafficApp.utility.encryption.ShareStorage;
import com.example.tonyso.TrafficApp.utility.encryption.StoreObject;

import java.util.Locale;

/**
 * Created by TonySo on 17/9/2015.
 */
public class BaseActivity extends AppCompatActivity {

    String currLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storeUserLanuguage();
        //Configuration configuration = new Configuration();
        //configuration.locale = new Locale(currLang);
        //onConfigurationChanged(configuration);
    }

    //SetDefaultUserLanguage in FirstLaunch
    private void storeUserLanuguage() {
//        langPref = getSharedPreferences(MyApplication.STATIC_DATA_TAG,0).edit();
//        langPref.putString(MyApplication.Language_Locale, MyApplication.getAppLocale(this));
//        langPref.apply();
        String lang2 = (String) ShareStorage.retrieveData(MyApplication.Language_UserPref,
                ShareStorage.DataType.STRING, ShareStorage.SP.PrivateData, getBaseContext()).getValue();
        String lang = "";
        if (!lang2.equals("")) {
            lang = lang2;
        } else {
            lang = Locale.getDefault().getLanguage();
            ShareStorage.saveData(ShareStorage.StorageType.SHARED_PREFERENCE,
                    new StoreObject<>(false, MyApplication.Language_UserPref,
                            lang), ShareStorage.SP.PrivateData, getBaseContext());
        }
        currLang = lang;
    }

    private void initStaticData(){

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

}
