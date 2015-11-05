package com.example.tonyso.TrafficApp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tonyso.TrafficApp.Interface.XMLFetchInterface;
import com.example.tonyso.TrafficApp.baseclass.BaseFragment;
import com.example.tonyso.TrafficApp.model.RouteCCTV;
import com.example.tonyso.TrafficApp.utility.LanguageSelector;
import com.example.tonyso.TrafficApp.utility.XMLReader;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.sephiroth.android.library.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class Nav_TrafficFragment extends BaseFragment implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener,XMLFetchInterface,GoogleMap.OnMarkerClickListener{

    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private List<RouteCCTV> routeList;
    private LanguageSelector languageSelector;
    Map<String,String> roadCCTVMap ;

    private static String TRAFFIC_URL = "http://tdcctv.data.one.gov.hk/";
    private static String JPG = ".JPG";

    private Bundle savedInstanceState;

    public Nav_TrafficFragment() {
        // Required empty public constructor
    }

    public static Nav_TrafficFragment newInstance(String title,int indicatorColor,int dividerColor){
        Nav_TrafficFragment f = new Nav_TrafficFragment();
        f.setTitle(title);
        f.setIndicatorColor(indicatorColor);
        f.setDividerColor(dividerColor);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_traffic_monitoring,container,false);
        this.savedInstanceState = savedInstanceState;
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        languageSelector = new LanguageSelector(getContext());
        XMLReader xmlReader = new XMLReader(this.getContext(),this);
        xmlReader.feedImageXml();
        mapFragment.getMapAsync(this);

        return v;
    }

    @Override
    public void onXMLFetch(List<RouteCCTV> r) {
        routeList = r;
        roadCCTVMap = new HashMap<>();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        String key,desc = "";
        if (routeList.size()!=0){
            for (int count = 0;count<routeList.size();count++){
                key = routeList.get(count).getKey();
                double[] latlng = routeList.get(count).getLatLngs();
                LatLng sydney = new LatLng(latlng[0],latlng[1]);
                if (languageSelector.getLanguage().equals(MyApplication.Language.ENGLISH)){
                    desc = routeList.get(count).getDescription()[0];
                    roadCCTVMap.put(desc,key);
                }else{
                    desc = routeList.get(count).getDescription()[1];
                    roadCCTVMap.put(desc,key);
                }

                mMap.addMarker(new MarkerOptions().
                        position(sydney).
                        title(desc));
            }
            mMap.setOnMarkerClickListener(this);
            mMap.setInfoWindowAdapter(new MapInfoAdapter());
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return false;
    }

    public class MapInfoAdapter implements GoogleMap.InfoWindowAdapter{

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {

            View view = getLayoutInflater(savedInstanceState).inflate(R.layout.popup_map_snapshot, null, false);
            String key = marker.getTitle();
            ImageView imageView = (ImageView)view.findViewById(R.id.map_snapshot);
            TextView tv = (TextView)view.findViewById(R.id.map_snapshot_title);
            String roadkey = roadCCTVMap.get(key);
            String URL = TRAFFIC_URL.concat(roadkey).concat(JPG);
            Log.e(getTag(), URL);
            Picasso.with(getContext()).load(URL).into(imageView);
            tv.setText(key);
            return view;
        }
    }
}
