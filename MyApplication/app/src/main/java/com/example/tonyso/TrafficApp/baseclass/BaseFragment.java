package com.example.tonyso.TrafficApp.baseclass;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.tonyso.TrafficApp.MyApplication;
import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.listener.StatusObserver;
import com.example.tonyso.TrafficApp.model.RouteCCTV;
import com.example.tonyso.TrafficApp.model.RouteSpeedMap;
import com.example.tonyso.TrafficApp.utility.LanguageSelector;
import com.example.tonyso.TrafficApp.utility.SQLiteHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

/**
 * Created by TonySo on 1/10/2015.
 */
public class BaseFragment extends Fragment {
    protected static final String TAG = BaseFragment.class.getCanonicalName();
    private String title = "";
    private int indicatorColor = Color.BLUE;
    private int dividerColor = Color.GRAY;
    private int icon = -1;

    protected String[] region_arr;
    protected String[] region_latlng;

    protected MyApplication myApplication;
    protected ImageLoader imageLoader;
    protected DisplayImageOptions displayImageOptions;
    protected SQLiteHelper sqLiteHelper;
    protected LanguageSelector languageSelector;
    protected List<RouteCCTV> routeList;
    protected List<RouteSpeedMap> routeSpeedMap;

    protected static String[] TABS_TITLES;

    protected static final int[] drawableIcons = new int[]{
            R.drawable.ic_directions_car_white_36dp,
            R.drawable.ic_local_see_white_36dp
    };

    protected StatusObserver observer;

    public void onAttach(Context context) {
        super.onAttach(context);
        region_arr = context.getResources().getStringArray(R.array.regions);
        region_latlng = context.getResources().getStringArray(R.array.region_LatLng);
        TABS_TITLES = new String[]{
                getString(R.string.route_suggest_car),
                getString(R.string.route_suggest_cctv)
        };
    }

    public String getTitle() {
        return title;
    }
    protected void setTitle(String title) {
        this.title = title;
    }
    public int getIndicatorColor() {
        return indicatorColor;
    }
    protected void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
    }
    public int getDividerColor() {
        return dividerColor;
    }
    protected void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
    }
    public int getIcon() {
        return icon;
    }
    public void setIcon(int icon) {
        this.icon = icon;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getResources().updateConfiguration(newConfig, getResources().getDisplayMetrics());
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "On Stop Frag");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy Frag");
    }

    protected void getInstance() {

        imageLoader = ImageLoader.getInstance();
        displayImageOptions = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.placeholder)        //    Display Stub Image
                .showImageForEmptyUri(R.drawable.ic_error_black_24dp)    //    If Empty image found
                .cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565)
                .cacheInMemory(true)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .build();
        sqLiteHelper = new SQLiteHelper(this.getContext());
        languageSelector = LanguageSelector.getInstance(this.getContext());
        myApplication = (MyApplication) getActivity().getApplication();
        routeList = myApplication.list;
        routeSpeedMap = myApplication.speedMaps;
        observer = myApplication.getTimeStatusObserver();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "On Destroy View");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(TAG, "On Detach");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "On Start Frag");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "On Resume Frag");
    }

    public DisplayImageOptions getImageOptions() {
        return displayImageOptions;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public LanguageSelector getLanguageSelector() {
        return languageSelector;
    }
}
