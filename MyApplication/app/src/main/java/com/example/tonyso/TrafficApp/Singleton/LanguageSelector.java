package com.example.tonyso.TrafficApp.Singleton;

import android.content.Context;

import com.example.tonyso.TrafficApp.MyApplication;
import com.example.tonyso.TrafficApp.utility.encryption.ShareStorage;

/**
 * Created by SMK338 on 03/08/2015.
 */
public class LanguageSelector {
    Context context;
//    SharedPreferences langPref;
    public static LanguageSelector languageSelector = null;

    private LanguageSelector(Context context) {
        this.context = context;
//        langPref = context.getSharedPreferences(MyApplication.Language_Tag,0);
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
        System.out.println(LanguageSelector.class.getName()+" Using Same Instance of"+languageSelector);
        return languageSelector;
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
