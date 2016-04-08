package com.example.tonyso.TrafficApp.fragment;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
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
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.tonyso.TrafficApp.MyApplication;
import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.baseclass.BaseFragment;
import com.example.tonyso.TrafficApp.model.RouteCCTV;
import com.example.tonyso.TrafficApp.model.RouteSpeedMap;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */

public class NavTrafficMonitorFragment extends BaseFragment implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {

    private static final String TAG = NavTrafficMonitorFragment.class.getName();
    private static final String TRAFFIC_URL = "http://tdcctv.data.one.gov.hk/";
    private static final String TRAFFIC_SPEED_MAP = "http://resource.data.one.gov.hk/td/";
    private static final String TC = "TC";
    private static final String EN = "EN";

    private static final String JPG = ".JPG";
    private static final String PNG = ".png";

    private static final String ARG_Title = "title";
    Map<String, RouteCCTV> roadCCTVMap = new HashMap<>();
    Map<String, LatLng> regionMap = new HashMap<>();
    Hashtable<String, Boolean> markerSet = new Hashtable<>();
    Hashtable<String, RouteSpeedMap> speedMapSet = new Hashtable<>();
    //Snackbar snackbar;
    CoordinatorLayout coordinatorLayout, cood;
    String[] arr, latlng;
    boolean isTrafficOn;
    Toolbar toolbar;
    View view;
    private String Title = "";
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    //Spinner Test
    private Spinner spinner;
    private Snackbar snackbar;

    public NavTrafficMonitorFragment() {

    }

    public static NavTrafficMonitorFragment newInstance(String title) {
        NavTrafficMonitorFragment baseFragment = new NavTrafficMonitorFragment();
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

        //Getting Instance
        super.getInstance();
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
        } catch (InflateException e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //snackbar.dismiss();
        mapFragment.onDetach();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setFragmentToolbar();
        //InitUIComponents
        initUIComponents(view);
        setSpinnerProperty();
        onSpinnerItemSelected();
    }

    private void initUIComponents(View view) {
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        //Sync Map
        mapFragment.getMapAsync(this);
        setHasOptionsMenu(true);
        this.coordinatorLayout = (CoordinatorLayout) getActivity().findViewById(R.id.coordinateLayoutMain);
        cood = (CoordinatorLayout) view.findViewById(R.id.traffic_main);
        snackbar = Snackbar.make(coordinatorLayout, "Click on one Marker....", Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
        //Spinner to let user select different district to see RouteCCTV
        spinner = (Spinner) view.findViewById(R.id.spinner);
    }

    private void setFragmentToolbar() {
        CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) view.findViewById(R.id.collapsingToolbar);
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorPrimary));
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(Title);
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
        arr = super.region_arr;
        latlng = super.region_latlng;
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
        List<RouteSpeedMap> speedMaps = super.routeSpeedMap;
        List<RouteSpeedMap> selectedMaps = new ArrayList<>();

        /**
         * Generate Selected Route List
         */
        for (RouteCCTV r : routeList) {
            String d;
            if (languageSelector.getLanguage().equals(MyApplication.Language.ENGLISH)) {
                d = r.getRegion()[0];
            } else {
                d = r.getRegion()[1];
            }
            if (d.matches(name)) {
                selectedRoute.add(r);
            }
        }

        for (RouteSpeedMap s : speedMaps) {
            String r;
            if (languageSelector.getLanguage().equals(MyApplication.Language.ENGLISH)) {
                r = s.getRegion()[0];
            } else {
                r = s.getRegion()[1];
            }
            if (r.matches(name)) {
                selectedMaps.add(s);
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
            Marker marker = mMap.addMarker(markerOptions);
            markerSet.put(marker.getId(), false);
        }

        for (RouteSpeedMap speedMap : selectedMaps) {
            String locationDesc;
            if (languageSelector.getLanguage().equals(MyApplication.Language.ENGLISH)) {
                locationDesc = speedMap.getDescription()[0];
                speedMapSet.put(locationDesc, speedMap);
            } else {
                locationDesc = speedMap.getDescription()[1];
                speedMapSet.put(locationDesc, speedMap);
            }
            Log.e(TAG, locationDesc);
            double[] temp = speedMap.getLatLngs();
            Log.e(TAG, "" + temp[0] + " " + temp[1]);
            LatLng latLng1 = new LatLng(speedMap.getLatLngs()[0], speedMap.getLatLngs()[1]);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng1);
            markerOptions.title(locationDesc);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            Marker marker = mMap.addMarker(markerOptions);
            markerSet.put(marker.getId(), false);
        }

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(markerSet, getContext(), speedMapSet));
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
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onPause() {
        if (snackbar != null)
            snackbar.dismiss();
        super.onPause();
        Log.e(TAG, "On Pause Frag");

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        final Marker m = marker;
        marker.showInfoWindow();
        snackbar = Snackbar.make(coordinatorLayout,marker.getTitle(),Snackbar.LENGTH_INDEFINITE);
        ViewGroup view = (ViewGroup) snackbar.getView();
        //Get Route Object
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String title = m.getTitle();
                RouteCCTV obj = null;
                if (roadCCTVMap.get(title) != null) {
                    obj=  roadCCTVMap.get(title);
                } else {
                    obj =speedMapSet.get(title);
                }
                Log.e(TAG, title);
                InfoDetailFragment fragment = InfoDetailFragment.newInstance(title, InfoDetailFragment.ADD_ROUTE_TYPE, obj);
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.add(fragment,InfoDetailFragment.TAG);
                fragmentTransaction.commit();
            }
        });
        snackbar.show();
        return true;
    }

    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        private final Hashtable<String, Boolean> markerSet;
        private Context context;
        private View view;
        private Hashtable<String, RouteSpeedMap> hashtable;

        public CustomInfoWindowAdapter(Hashtable<String, Boolean> markerSet,
                                       Context context, Hashtable<String, RouteSpeedMap> speedMapSet) {
            this.markerSet = markerSet;
            this.context = context;
            this.hashtable = speedMapSet;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            view = getActivity().getLayoutInflater().inflate(R.layout.item_map_image, null);
            ImageView img = (ImageView) view.findViewById(R.id.imgMapImage);
            RouteCCTV routeCCTV = (roadCCTVMap.get(marker.getTitle()) == null) ? null : roadCCTVMap.get(marker.getTitle());
            RouteSpeedMap speedMap;
            if (hashtable.get(marker.getTitle()) == null) {
                speedMap = null;
            } else {
                speedMap = hashtable.get(marker.getTitle());
            }

            String url = "";
            //Compare Two String

            if (routeCCTV != null) {
                url = TRAFFIC_URL + routeCCTV.getRef_key() + JPG;
                Log.d(TAG, url);
            } else if (speedMap != null) {
                url = TRAFFIC_SPEED_MAP + speedMap.getRef_key() +
                        (languageSelector.getLanguage().equals(MyApplication.Language.ENGLISH) ? EN : TC) + PNG;
                Log.d(TAG, "Else: " + url);
            }

            boolean isImageLoaded = markerSet.get(marker.getId());
            if (isImageLoaded) {
                imageLoader.displayImage(url, img, displayImageOptions);
            } else {
                isImageLoaded = true;
                markerSet.put(marker.getId(), true);
                imageLoader.displayImage(url, img, displayImageOptions, new CustomImageLoadingListener(marker));
            }

            return view;
        }
    }

    private class CustomImageLoadingListener implements ImageLoadingListener {

        private Marker marker;

        public CustomImageLoadingListener(Marker markerToRefresh) {
            this.marker = markerToRefresh;
        }

        @Override
        public void onLoadingStarted(String imageUri, View view) {

        }

        @Override
        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

        }

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (marker != null && marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
                marker.showInfoWindow();
            }
        }

        @Override
        public void onLoadingCancelled(String imageUri, View view) {

        }
    }
}
