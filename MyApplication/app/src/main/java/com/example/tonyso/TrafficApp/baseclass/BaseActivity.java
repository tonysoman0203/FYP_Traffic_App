package com.example.tonyso.TrafficApp.baseclass;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import com.example.tonyso.TrafficApp.MyApplication;
import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.rss.XMLReader;
import com.example.tonyso.TrafficApp.utility.encryption.ShareStorage;
import com.example.tonyso.TrafficApp.utility.encryption.StoreObject;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.util.Locale;

/**
 * Created by TonySo on 17/9/2015.
 */
public class BaseActivity extends AppCompatActivity {

    String currLang;
    XMLReader xmlReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        xmlReader = new XMLReader(this);
        MyApplication myApplication = (MyApplication) getApplication();
        myApplication.list = xmlReader.getImageXML();
        storeUserLanuguage();
        initImageLoader();
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

    private void initImageLoader(){

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
                .threadPoolSize(3) // default
                .threadPriority(Thread.NORM_PRIORITY)
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .imageDownloader(new BaseImageDownloader(this)) // default
                .imageDecoder(new BaseImageDecoder(true)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .build();
        ImageLoader.getInstance().init(configuration);
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
