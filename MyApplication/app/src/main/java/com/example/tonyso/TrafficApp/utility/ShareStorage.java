package com.example.tonyso.TrafficApp.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.example.tonyso.TrafficApp.MyApplication;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by NCH575 on 11/08/2015.
 */
public class ShareStorage {

    public ShareStorage() {

    }

    public static void saveData(int storageType, StoreObject<?> storeObject, String sp, Context context) {
        if (sp != null) {
            if (sp.equals(SP.ProtectedData)) {
                sp = SP.ProtectedData;
            } else if (sp.equals(SP.PrivateData)) {
                sp = SP.PrivateData;
            } else if (sp.equals(SP.SysParam)) {
                sp = SP.SysParam;
            }
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(sp, 0);
        switch (storageType) {
            case StorageType.SHARED_PREFERENCE:
                String key = storeObject.getKey();
                if (storeObject.getValue() instanceof Integer) {
                    String encrypt_value = encryption(String.valueOf(storeObject.getValue()));
                    sharedPreferences.edit().putString(key, encrypt_value).apply();
                } else if (storeObject.getValue() instanceof String) {
                    String encrypt_value = encryption(String.valueOf(storeObject.getValue()));
                    sharedPreferences.edit().putString(key, encrypt_value).apply();
                } else if (storeObject.getValue() instanceof Long) {
                    String encrypt_value = encryption(String.valueOf(storeObject.getValue()));
                    sharedPreferences.edit().putString(key, encrypt_value).apply();
                } else if (storeObject.getValue() instanceof Boolean) {
                    String encrypt_value = encryption(String.valueOf(storeObject.getValue()));
                    sharedPreferences.edit().putString(key, encrypt_value).apply();
                } else {
                    return;
                }
                break;
            case StorageType.PRIVATE_FILE:
                break;
            case StorageType.SQLLITE:
                break;
            default:
                break;
        }
    }

    public static Boolean getBoolean(String key, String sp, Context context) {
        if (retrieveData(key, sp, context).equals("")) {
            return false;
        }
        return Boolean.valueOf(retrieveData(key, sp, context));
    }

    public static Long getLong(String key, String sp, Context context) {
        if (retrieveData(key, sp, context).equals("")) {
            return Long.valueOf(-1);
        }
        return Long.valueOf(retrieveData(key, sp, context));
    }

    public static String getString(String key, String sp, Context context) {
        return retrieveData(key, sp, context);
    }

    public static int getInteger(String key, String sp, Context context) {
        if (retrieveData(key, sp, context).equals("")) {
            return -1;
        }
        return Integer.valueOf(retrieveData(key, sp, context));
    }

    public static String retrieveData(String key, String sp, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sp, 0);
        String string_value = decryption(sharedPreferences.getString(key, ""));
        return string_value;
    }

    public static ArrayList<StoreObject<?>> retrieveDataArray(String Regexpattern, int datatype, String sp, Context context) {
        if (sp != null) {
            if (sp.equals(SP.Locale)) {
                LanguageSelector languageSelector = LanguageSelector.getInstance(context);
                String lang_pref = languageSelector.getUserLanguage();
                if (lang_pref != null) {
                    if (lang_pref.equals("en")) {
                        sp = PrivateSP.Locale_EN;
                    } else {
                        sp = PrivateSP.Locale_ZH;
                    }
                }
            }
        }
        ArrayList<StoreObject<?>> storeObjectArrayList = new ArrayList<StoreObject<?>>();
        SharedPreferences sharedPreferences = context.getSharedPreferences(sp, 0);
        Map<String, ?> map = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            String key = entry.getKey();
            Pattern p = Pattern.compile(Regexpattern);
            Matcher m = p.matcher(key);
            if (m.find()) {
                storeObjectArrayList.add(fetchData(sharedPreferences, datatype, key));
            }
        }
        return storeObjectArrayList;
    }

    private static StoreObject<?> fetchData(SharedPreferences sharedPreferences, int datatype, String key) {
        String tmp = Base64.encodeToString(key.getBytes(), Base64.DEFAULT);
        byte[] decodeTmp = Base64.decode(tmp, Base64.DEFAULT);
        key = new String(decodeTmp);
        switch (datatype) {
            case DataType.BOOLEAN:
                boolean boolean_value = Boolean.valueOf(decryption(sharedPreferences.getString(key, "false")));
                return new StoreObject<>(false, key, boolean_value);
            case DataType.INTEGER:
                Integer integer_value = Integer.valueOf(decryption(sharedPreferences.getString(key, "-1")));
                return new StoreObject<>(false, key, integer_value);
            case DataType.LONG:
                Long long_value = Long.valueOf(decryption(sharedPreferences.getString(key, "-1")));
                return new StoreObject<>(false, key, long_value);
            case DataType.STRING:
                String string_value = decryption(sharedPreferences.getString(key, ""));
                return new StoreObject<>(false, key, string_value);
            default:
                break;
        }
        return null;
    }

    public static StoreObject<?> retrieveData(String key, int datatype, String sp, Context context) {
        sp = Normalizer.normalize(sp, Normalizer.Form.NFKC);
        if (sp != null) {
            if (sp.equals(SP.Locale)) {
                LanguageSelector languageSelector = LanguageSelector.getInstance(context);
                String lang_pref = languageSelector.getUserLanguage();
                if (lang_pref != null) {
                    if (lang_pref.equals(MyApplication.Language.ENGLISH)) {
                        sp = PrivateSP.Locale_EN;
                    } else {
                        sp = PrivateSP.Locale_ZH;
                    }
                }
            }
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(sp, 0);
        return fetchData(sharedPreferences, datatype, key);
    }

    //encryption and decyption
    private static String encryption(String data) {
        return data;
    }

    private static String decryption(String data) {
        return data;
    }

    public static void clearShareStorage(String sp, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(sp, 0);
        sharedPreferences.edit().clear().apply();
    }

    public class StorageType {
        public static final int SHARED_PREFERENCE = 1;
        public static final int PRIVATE_FILE = 2;
        public static final int SQLLITE = 3;
    }

    public class DataType {
        public static final int INTEGER = 1;
        public static final int STRING = 2;
        public static final int LONG = 3;
        public static final int BOOLEAN = 4;
    }

    public class SP {
        public static final String ProtectedData = "ProtectedData";
        public static final String PrivateData = "PrivateData2";
        public static final String SysParam = "SysParam";
        public static final String Locale = "LocalizationData";
    }

    public class PrivateSP {
        public static final String Locale_EN = "Locale_EN";
        public static final String Locale_ZH = "Locale_ZH";
    }
}
