package com.example.tonyso.TrafficApp.baseclass;

import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.tonyso.TrafficApp.MyApplication;
import com.example.tonyso.TrafficApp.rss_xml_feed.XMLReader;
import com.example.tonyso.TrafficApp.utility.SQLiteHelper;
import com.example.tonyso.TrafficApp.utility.ShareStorage;
import com.example.tonyso.TrafficApp.utility.StoreObject;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.util.List;
import java.util.Locale;

/**
 * Created by TonySo on 17/9/2015.
 */
public class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getCanonicalName();
    //String currLang;
    public static SQLiteDatabase sqLiteDatabase;
    public static SQLiteHelper sqLiteHelper;
    MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myApplication = (MyApplication) getApplication();
        storeUserLanuguage();
        myApplication.list = loadCache("ImageList");
        myApplication.speedMaps = loadCache("SpeedMap");
        initImageLoader();
        initNearLocationInKm();
        //initialize SQLLite
        //sqLiteDatabase = SQLiteHelper.getDatabase(this);
        sqLiteHelper = new SQLiteHelper(this);
        sqLiteDatabase = SQLiteHelper.getDatabase(this);
    }

    public List loadCache(String tag) {
        XMLReader xmlReader = XMLReader.getInstance(this);
        if (tag.equals("ImageList"))
            return xmlReader.getImageXML();
        else
            return xmlReader.getRouteImageSpeedMap();
    }

    //SetDefaultUserLanguage in FirstLaunch
    private void storeUserLanuguage() {
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
        MyApplication.CURR_LANG = lang;
    }


    private void initNearLocationInKm(){
        int KM_IN_DEVICE = (Integer) ShareStorage.retrieveData(MyApplication.KEY_NEAR_IN_KM,
                ShareStorage.DataType.INTEGER, ShareStorage.SP.PrivateData, getBaseContext()).getValue();
        Log.d(TAG, "THE KM-IN_DEVICE = " + KM_IN_DEVICE);
        int km = -1;
        if (KM_IN_DEVICE != -1) {
            km = KM_IN_DEVICE;
        } else {
            km = 2;
            ShareStorage.saveData(ShareStorage.StorageType.SHARED_PREFERENCE,
                    new StoreObject<>(false, MyApplication.KEY_NEAR_IN_KM,
                            km), ShareStorage.SP.PrivateData, getBaseContext());
        }
        MyApplication.KM_IN_NEAR = km;
    }


    private void initImageLoader(){

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
                .threadPoolSize(4) // default
                .threadPriority(Thread.MAX_PRIORITY)
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getResources().updateConfiguration(newConfig, getResources().getDisplayMetrics());
    }

}
