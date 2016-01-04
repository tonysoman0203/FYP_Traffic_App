package com.example.tonyso.TrafficApp;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.tonyso.TrafficApp.Singleton.LanguageSelector;
import com.example.tonyso.TrafficApp.Singleton.SQLiteHelper;
import com.example.tonyso.TrafficApp.baseclass.BaseActivity;
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

public class Nav_TrafficActivity extends BaseActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener,GoogleMap.OnMarkerClickListener{

    private static final String TAG = Nav_TrafficActivity.class.getName();
    private static final String TRAFFIC_URL = "http://tdcctv.data.one.gov.hk/";
    private static final String JPG = ".JPG";

    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private List<RouteCCTV> routeList;
    private LanguageSelector languageSelector;
    Map<String,RouteCCTV> roadCCTVMap = new HashMap<>();
    Map<String,LatLng> regionMap = new HashMap<>();
    SQLiteHelper sqLiteHelper;
    Snackbar snackbar;

    //Spinner Test
    private Spinner spinner;

    CoordinatorLayout coordinatorLayout;
    String []arr,latlng;

    boolean isTrafficOn;
    Toolbar toolbar;
    ImageLoader imageLoader;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_monitoring_main);
        //InitUIComponents
        initUIComponents();
        //Getting Instance
        getInstance();
        //Sync Map
        mapFragment.getMapAsync(this);
        onSpinnerItemSelected();
    }

    private void initUIComponents(){
        mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        view = findViewById(R.id.content);
        toolbar = (Toolbar) findViewById(R.id.traffic_mon_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.coordinatorLayout = (CoordinatorLayout)findViewById(R.id.main_coordinate_layout_MON);
        Snackbar.make(coordinatorLayout,"Click on one Marker....",Snackbar.LENGTH_INDEFINITE).show();
        // Spinner to let user select different district to see RouteCCTV
        spinner = (Spinner) findViewById(R.id.spinner);
        setSpinnerProperty();
    }

    private void getInstance(){
        imageLoader = ImageLoader.getInstance();
        sqLiteHelper = new SQLiteHelper(this);
        languageSelector = LanguageSelector.getInstance(this);
        MyApplication myApplication = (MyApplication)getApplication();
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
    //private void removePreviousMarker() {mMap.clear();}

    private void setRegionHashMap(String[] arr, String[] regionHashMap) {
        double[] tmp;
        for (int i = 0;i < arr.length;i++) {
            tmp = new double[2];
            String t = regionHashMap[i];
            String[]sp = t.split(" ");
            tmp[0] = Double.parseDouble(sp[0]);
            tmp[1] = Double.parseDouble(sp[1]);
            LatLng latLng = new LatLng(tmp[0],tmp[1]);
            regionMap.put(arr[i],latLng);
        }
    }

    private void setSpinnerProperty(){
        arr = getResources().getStringArray(R.array.regions);
        latlng = getResources().getStringArray(R.array.region_LatLng);
        setRegionHashMap(arr, latlng);
        ArrayAdapter<String> dataadpater = new ArrayAdapter(this,android.R.layout.simple_spinner_item,arr);
        dataadpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataadpater);
        spinner.setSelection(0);
    }

    /**
     *  This mehtod setMarker is to setup the marker based on user selected
     *  @param googleMap is a Google Map Instance
     *  @param posDistrict is the user selection id
     */
    private void setMarker(GoogleMap googleMap,int posDistrict){
        mMap = googleMap;
        int p = posDistrict;
        if (p==0) return;
        String name = arr[p];
        List<RouteCCTV>selectedRoute = new ArrayList<>();
        for (int i = 0 ; i< routeList.size() ; i++){
            String d;
            if (languageSelector.getLanguage().equals(MyApplication.Language.ENGLISH)){
                d = routeList.get(i).getRegion()[0];
            }else{
                d = routeList.get(i).getRegion()[1];
            }
            if (d.matches(name)){
                selectedRoute.add(routeList.get(i));
            }
        }
        //Get the Latlng first and set the map;
        LatLng latLng = regionMap.get(name);
        CameraUpdate center = CameraUpdateFactory.newLatLng(latLng);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo((float)11);
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);

        //Setup the Marker
        for (int count = 0;count<selectedRoute.size();count++){
            RouteCCTV routeCCTV = selectedRoute.get(count);
            String desc;
            LatLng latlng = new LatLng(selectedRoute.get(count).getLatLngs()[0],selectedRoute.get(count).getLatLngs()[1]);
            //LatLng location = new LatLng(latlng[0],latlng[1]);
            if (languageSelector.getLanguage().equals(MyApplication.Language.ENGLISH)){
                desc = routeCCTV.getDescription()[0];
                roadCCTVMap.put(desc,routeCCTV);
            }else{
                desc = routeCCTV.getDescription()[1];
                roadCCTVMap.put(desc,routeCCTV);
            }
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latlng);
            markerOptions.title(desc);
            mMap.addMarker(markerOptions);
        }
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nav_nav_trafficactivity,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //called when the up affordance/carat in actionbar is pressed
                onBackPressed();
                return true;
            case R.id.nav_traffic_terrain:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            case R.id.nav_traffic_satelite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.nav_traffic_traffic:
                if(isTrafficOn){
                    mMap.setTrafficEnabled(false);
                    isTrafficOn = false;
                }else{
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
        setGoogleMapProperty(mMap);
    }

    /** The Method setGoogleMapProperty is to set the property of map
        * @param googleMap
        */
    private void setGoogleMapProperty(GoogleMap googleMap){
        googleMap.setMyLocationEnabled(true);
    }

    @Override
    public void onMapClick(LatLng latLng) {}

    @Override
    public boolean onMarkerClick(final Marker marker) {
        snackbar = Snackbar.make(coordinatorLayout,marker.getTitle(),Snackbar.LENGTH_INDEFINITE);
        ViewGroup viewGroup = (ViewGroup) snackbar.getView();
        //Get Route Object
        viewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(getApplicationContext(), InfoDetailActivity.class);
                final String title = marker.getTitle();
                Log.e(TAG, title);
                intent.putExtra("key", title);
                intent.putExtra(title, roadCCTVMap.get(title));
                intent.putExtra("type", InfoDetailActivity.ADD_ROUTE_TYPE);
                startActivity(intent);

                }
        });
        snackbar.show();
        return true;
    }

}
