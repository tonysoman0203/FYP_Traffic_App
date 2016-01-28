package com.example.tonyso.TrafficApp.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.tonyso.TrafficApp.GetCurrentLocationAsyncTask;
import com.example.tonyso.TrafficApp.MainActivity;
import com.example.tonyso.TrafficApp.MyApplication;
import com.example.tonyso.TrafficApp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class NavTrafficSuggestFragment extends Fragment implements
        GoogleMap.OnMapClickListener, OnMapReadyCallback, GoogleMap.OnMapLongClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String TAG = NavTrafficSuggestFragment.class.getCanonicalName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GoogleMap mGoogleMap;

    private LocationInputFragment locationInputFragment;


    public NavTrafficSuggestFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NavTrafficSuggestFragment newInstance(String param1) {
        NavTrafficSuggestFragment fragment = new NavTrafficSuggestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nav_traffic_suggest, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setFragmentToolbar();
        initGoogleMap();
        locationInputFragment = LocationInputFragment.newInstance();
        //locationInputFragment.show(getChildFragmentManager(),null);
    }


    private void setFragmentToolbar() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(mParam1);
    }

    private void PopUpDialog() {

    }

    private void initGoogleMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        //Sync Map
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.setOnMapLongClickListener(this);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        GetCurrentLocationAsyncTask task = new GetCurrentLocationAsyncTask();
        if (mGoogleMap != null) {
            task.setLatlng(latLng);
            task.setApplication((MyApplication) getActivity().getApplication());
            task.execute();
        } else {
            Log.e(TAG, "Exception Catch...");
        }
        MyApplication myapp = (MyApplication) MainActivity.activity.getApplication();
        MarkerOptions marker = new MarkerOptions().position(latLng).title(myapp.locate);
        Marker m = mGoogleMap.addMarker(marker);
    }

    public static class LocationInputFragment extends DialogFragment {

        public LocationInputFragment() {

        }

        public static LocationInputFragment newInstance() {
            LocationInputFragment fragment = new LocationInputFragment();
            Bundle args = new Bundle();
            //args.putString(ARG_PARAM1, param1);
            //args.putString(ARG_PARAM2, param2);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Dialog dialog = super.onCreateDialog(savedInstanceState);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            return dialog;
        }


        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            return inflater.inflate(R.layout.popup_traffic_suggest_dialog, container, false);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

        }
    }
}
