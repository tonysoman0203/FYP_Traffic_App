package com.example.tonyso.TrafficApp.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.baseclass.BaseFragment;
import com.example.tonyso.TrafficApp.listener.OnPathReadyListener;
import com.example.tonyso.TrafficApp.location.DrawPathAsyncTask;
import com.example.tonyso.TrafficApp.model.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;


public class NavSuggestMapFragment extends BaseFragment
        implements OnMapReadyCallback
        , GoogleMap.OnMapClickListener, OnPathReadyListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    SupportMapFragment mapFragment;
    private View view;
    private GoogleMap mMap;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private Place origin, destination;

    public NavSuggestMapFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NavSuggestMapFragment newInstance(String near, int indicatorColor, int dividerColor, int ic_home, Place origin, Place destination) {
        NavSuggestMapFragment f = new NavSuggestMapFragment();
        f.setTitle(near);
        f.setIndicatorColor(indicatorColor);
        f.setDividerColor(dividerColor);
        f.setIcon(ic_home);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_PARAM1, origin);
        bundle.putSerializable(ARG_PARAM2, destination);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            origin = (Place) getArguments().getSerializable(ARG_PARAM1);
            destination = (Place) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_nav_suggest_map, container, false);
        } catch (InflateException e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        //Sync Map
        mapFragment.getMapAsync(this);
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipemap);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mapFragment.onDetach();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.e(TAG, "Map Ready...");
        mMap = googleMap;
        DrawPathAsyncTask drawPathAsyncTask = new DrawPathAsyncTask(this, mMap, origin, destination, mSwipeRefreshLayout, this);
        drawPathAsyncTask.execute();
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onPathReady(List<LatLng> paths, final String duration) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(paths.get(0), 15));
        Polyline line = mMap.addPolyline(new PolylineOptions()
                        .addAll(paths)
                        .width(14)
                        .color(Color.parseColor("#05b1fb"))//Google maps blue color
                        .geodesic(true)
        );

        Marker des = mMap.addMarker(new MarkerOptions()
                        .position(paths.get(paths.size() - 1))
        );

        Marker originM = mMap.addMarker(new MarkerOptions()
                        .position(paths.get(0))
        );

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.item_suggest_marker_detail_map, null, false);
                TextView mT = (TextView) view.findViewById(R.id.txtMarkerTitle);
                mT.setTextColor(Color.parseColor("#FFC107"));
                mT.setText(duration);
                return view;
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });
    }

}
