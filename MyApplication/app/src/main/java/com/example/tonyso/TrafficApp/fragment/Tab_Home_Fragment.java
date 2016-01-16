package com.example.tonyso.TrafficApp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tonyso.TrafficApp.MainActivity;
import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.baseclass.TabBaseFragment;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by TonySo on 11/1/16.
 */
public class Tab_Home_Fragment extends TabBaseFragment {

    private static final String TAG = Tab_Home_Fragment.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;

    public Tab_Home_Fragment() {

    }

    public static Tab_Home_Fragment newInstance(String title, int indicatorColor, int dividerColor, int icon) {
        Tab_Home_Fragment f = new Tab_Home_Fragment();
        f.setTitle(title);
        f.setIndicatorColor(indicatorColor);
        f.setDividerColor(dividerColor);
        f.setIcon(icon);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mGoogleApiClient = MainActivity.getmGoogleApiClient();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public Context getContext() {
        return super.getContext();
    }


}
