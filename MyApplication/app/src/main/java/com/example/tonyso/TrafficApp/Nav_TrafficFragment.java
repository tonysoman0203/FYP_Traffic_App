package com.example.tonyso.TrafficApp;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.tonyso.TrafficApp.Singleton.LanguageSelector;
import com.example.tonyso.TrafficApp.Singleton.SQLiteHelper;
import com.example.tonyso.TrafficApp.model.RouteCCTV;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */

public class Nav_TrafficFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {

    public static final String TAG = Nav_TrafficFragment.class.getName();
    private static final String TRAFFIC_URL = "http://tdcctv.data.one.gov.hk/";
    private static final String JPG = ".JPG";

    private static final String ARG_Title = "title";
    private String Title = "";

    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private List<RouteCCTV> routeList;
    private LanguageSelector languageSelector;
    Map<String, RouteCCTV> roadCCTVMap = new HashMap<>();
    Map<String, LatLng> regionMap = new HashMap<>();
    SQLiteHelper sqLiteHelper;
    Snackbar snackbar;

    //Spinner Test
    private Spinner spinner;

    CoordinatorLayout coordinatorLayout;
    String[] arr, latlng;

    boolean isTrafficOn;
    Toolbar toolbar;
    ImageLoader imageLoader;
    View view;

    Bundle bundle;

    public Nav_TrafficFragment() {

    }

    public static Nav_TrafficFragment newInstance(String title) {
        Nav_TrafficFragment baseFragment = new Nav_TrafficFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_Title, title);
        baseFragment.setArguments(bundle);
        return baseFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Title = getArguments().getString(ARG_Title);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.content_traffic_monitoring, container, false);
            //InitUIComponents
            initUIComponents(view);
            //Getting Instance
            getInstance();
            //Sync Map
            mapFragment.getMapAsync(this);
            onSpinnerItemSelected();
        } catch (InflateException e) {
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initUIComponents(View view) {
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        setFragmentToolbar();
        setHasOptionsMenu(true);
        this.coordinatorLayout = (CoordinatorLayout) getActivity().findViewById(R.id.coordinateLayoutMain);
        snackbar = Snackbar.make(coordinatorLayout, "Click on one Marker....", Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
        // Spinner to let user select different district to see RouteCCTV
        spinner = (Spinner) view.findViewById(R.id.spinner);
        setSpinnerProperty();
    }

    private void setFragmentToolbar() {
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(Title);
        MainActivity.activity.setSupportActionBar(toolbar);
    }

    private void getInstance() {
        imageLoader = ImageLoader.getInstance();
        sqLiteHelper = new SQLiteHelper(this.getContext());
        languageSelector = LanguageSelector.getInstance(this.getContext());
        MyApplication myApplication = (MyApplication) getActivity().getApplication();
        routeList = myApplication.list;
    }

    private void onSpinnerItemSelected() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mMap.clear();
                setMarker(mMap, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setRegionHashMap(String[] arr, String[] regionHashMap) {
        double[] tmp;
        for (int i = 0; i < arr.length; i++) {
            tmp = new double[2];
            String t = regionHashMap[i];
            String[] sp = t.split(" ");
            tmp[0] = Double.parseDouble(sp[0]);
            tmp[1] = Double.parseDouble(sp[1]);
            LatLng latLng = new LatLng(tmp[0], tmp[1]);
            regionMap.put(arr[i], latLng);
        }
    }

    private void setSpinnerProperty() {
        arr = getResources().getStringArray(R.array.regions);
        latlng = getResources().getStringArray(R.array.region_LatLng);
        setRegionHashMap(arr, latlng);
        ArrayAdapter<String> dataAdpater = new ArrayAdapter(this.getContext(), android.R.layout.simple_spinner_item, arr);
        dataAdpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdpater);
        spinner.setSelection(0);
    }

    /**
     *  This mehtod setMarker is to setup the marker based on user selected
     *  @param googleMap is a Google Map Instance
     *  @param posDistrict is the user selection id
     */
    private void setMarker(GoogleMap googleMap, int posDistrict) {
        mMap = googleMap;
        int p = posDistrict;
        if (p == 0) return;
        String name = arr[p];
        List<RouteCCTV> selectedRoute = new ArrayList<>();
        for (int i = 0; i < routeList.size(); i++) {
            String d;
            if (languageSelector.getLanguage().equals(MyApplication.Language.ENGLISH)) {
                d = routeList.get(i).getRegion()[0];
            } else {
                d = routeList.get(i).getRegion()[1];
            }
            if (d.matches(name)) {
                selectedRoute.add(routeList.get(i));
            }
        }
        //Get the Latlng first and set the map;
        LatLng latLng = regionMap.get(name);
        CameraUpdate center = CameraUpdateFactory.newLatLng(latLng);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo((float) 11);
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);

        //Setup the Marker
        for (int count = 0; count < selectedRoute.size(); count++) {
            RouteCCTV routeCCTV = selectedRoute.get(count);
            String desc;
            LatLng latlng = new LatLng(selectedRoute.get(count).getLatLngs()[0], selectedRoute.get(count).getLatLngs()[1]);
            //LatLng location = new LatLng(latlng[0],latlng[1]);
            if (languageSelector.getLanguage().equals(MyApplication.Language.ENGLISH)) {
                desc = routeCCTV.getDescription()[0];
                roadCCTVMap.put(desc, routeCCTV);
            } else {
                desc = routeCCTV.getDescription()[1];
                roadCCTVMap.put(desc, routeCCTV);
            }
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latlng);
            markerOptions.title(desc);
            mMap.addMarker(markerOptions);
        }
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_nav_nav_trafficactivity, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_traffic_terrain:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            case R.id.nav_traffic_satelite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.nav_traffic_traffic:
                if (isTrafficOn) {
                    mMap.setTrafficEnabled(false);
                    isTrafficOn = false;
                } else {
                    mMap.setTrafficEnabled(true);
                    isTrafficOn = true;
                }
                return isTrafficOn;
            case R.id.nav_traffic_default:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onMapClick(LatLng latLng) {
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

    @Override
    public void onPause() {
        if (snackbar != null)
            snackbar.dismiss();
        super.onPause();
        Log.e(TAG, "On Pause Frag");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, "On Stop Frag");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "On Destory Frag");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, "On Destory View");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(TAG, "On Detach");
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        snackbar = Snackbar.make(coordinatorLayout,marker.getTitle(),Snackbar.LENGTH_INDEFINITE);
        ViewGroup view = (ViewGroup) snackbar.getView();
        //Get Route Object
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(getActivity(), InfoDetailActivity.class);
                final String title = marker.getTitle();
                Log.e(TAG, title);
                intent.putExtra("key", title);
                intent.putExtra(title, roadCCTVMap.get(title));
                intent.putExtra("type", InfoDetailActivity.ADD_ROUTE_TYPE);
                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.addToBackStack(TAG).commit();
                startActivity(intent);
                }
        });
        snackbar.show();
        return true;
    }

}
