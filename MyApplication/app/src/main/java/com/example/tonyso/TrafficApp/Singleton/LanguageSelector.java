package com.example.tonyso.TrafficApp.Singleton;

import android.content.Context;
import android.util.Log;

import com.example.tonyso.TrafficApp.MyApplication;
import com.example.tonyso.TrafficApp.utility.ShareStorage;

/**
 * Created by SMK338 on 03/08/2015.
 */
public class LanguageSelector {
    Context context;
//    SharedPreferences langPref;
    public static LanguageSelector languageSelector = null;

    private LanguageSelector(Context context) {
        this.context = context;
    }

    public static LanguageSelector getInstance(Context context){
        if (languageSelector==null){
            synchronized (LanguageSelector.class){
                if (languageSelector==null){
                    languageSelector = new LanguageSelector(context);
                    return languageSelector;
                }
            }
        }
        Log.d(LanguageSelector.class.getName(), "Using Same Instance of" + languageSelector);
        return languageSelector;
    }

    @Override
    public String toString() {
        return "LanguageSelector{" +
                "context=" + context +
                '}';
    }

    public String getUserLanguage() {
          return (String) ShareStorage.retrieveData(MyApplication.Language_UserPref, ShareStorage.DataType.STRING, ShareStorage.SP.PrivateData, context).getValue();
    }

    public String getSystemLanguage() {
        return (String) ShareStorage.retrieveData(MyApplication.Language_Locale, ShareStorage.DataType.STRING, ShareStorage.SP.PrivateData, context).getValue();
    }

    public boolean isSystemLanguage() {
        return ((String) ShareStorage.retrieveData(MyApplication.Language_UserPref, ShareStorage.DataType.STRING, ShareStorage.SP.PrivateData, context).getValue()).isEmpty();
    }

    public String getLanguage() {
        if (isSystemLanguage()) {
            return getSystemLanguage();
        } else {
            return getUserLanguage();
        }
    }
}
