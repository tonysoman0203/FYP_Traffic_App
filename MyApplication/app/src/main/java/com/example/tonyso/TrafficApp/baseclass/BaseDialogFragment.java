package com.example.tonyso.TrafficApp.baseclass;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.support.v4.app.DialogFragment;

import com.example.tonyso.TrafficApp.MyApplication;
import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.model.RouteCCTV;
import com.example.tonyso.TrafficApp.model.RouteSpeedMap;
import com.example.tonyso.TrafficApp.utility.LanguageSelector;
import com.example.tonyso.TrafficApp.utility.SQLiteHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

/**
 * Created by soman on 2016/2/13.
 */
public class BaseDialogFragment extends DialogFragment {
    protected String[] region_arr;
    protected String[] region_latlng;

    protected MyApplication myApplication;
    protected ImageLoader imageLoader;
    protected DisplayImageOptions displayImageOptions;
    protected SQLiteHelper sqLiteHelper;
    protected LanguageSelector languageSelector;
    protected List<RouteCCTV> routeList;
    protected List<RouteSpeedMap> routeSpeedMap;

    protected static final int[] drawableIcons = new int[]{
            R.drawable.ic_directions_car_white_36dp,
            R.drawable.ic_local_see_white_36dp
    };

    protected static String[] TABS_TITLES;

    public void onAttach(Context context) {
        super.onAttach(context);
        region_arr = context.getResources().getStringArray(R.array.regions);
        region_latlng = context.getResources().getStringArray(R.array.region_LatLng);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AppTheme);
        TABS_TITLES = new String[]{
                getString(R.string.route_suggest_car),
                getString(R.string.route_suggest_cctv)
        };
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getResources().updateConfiguration(newConfig, getResources().getDisplayMetrics());
    }

    protected void getInstance() {

        imageLoader = ImageLoader.getInstance();
        displayImageOptions = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_launcher)        //    Display Stub Image
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
    }
}
