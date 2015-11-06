package com.example.tonyso.TrafficApp;


import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.tonyso.TrafficApp.Interface.Rss_Listener;
import com.example.tonyso.TrafficApp.baseclass.BaseFragment;
import com.example.tonyso.TrafficApp.model.Route;
import com.example.tonyso.TrafficApp.model.RouteCCTV;
import com.example.tonyso.TrafficApp.utility.LanguageSelector;
import com.example.tonyso.TrafficApp.utility.XMLReader;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Nav_TrafficFragment extends BaseFragment implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener,Rss_Listener,GoogleMap.OnMarkerClickListener{

    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private List<RouteCCTV> routeList;
    private LanguageSelector languageSelector;
    Map<String,String> roadCCTVMap ;

    private static String TRAFFIC_URL = "http://tdcctv.data.one.gov.hk/";
    private static String JPG = ".JPG";

    private Bundle savedInstanceState;

    //Spinner Test
    private Spinner spinner;

    CoordinatorLayout coordinatorLayout;

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
        spinner = (Spinner)v.findViewById(R.id.spinner);
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        languageSelector = new LanguageSelector(getContext());
        XMLReader xmlReader = new XMLReader(this.getContext(),this);
        xmlReader.feedImageXml();
        mapFragment.getMapAsync(this);

        return v;
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

    @Override
    public void ParsedInfo(List list) {
        routeList = list;
        roadCCTVMap = new HashMap<>();
        final String []arr = getResources().getStringArray(R.array.regions);
        ArrayAdapter<String> dataadpater = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,arr);
        dataadpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataadpater);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Snackbar.make(getActivity().findViewById(R.id.coordinateLayoutMain),arr[position] , Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

//    private List<String> getDropdownlist() {
//        List<String> sortedRegions = new ArrayList<>();
//        String pref = languageSelector.getLanguage();
//        String curr,region="";
//        int j = 0;
//        for (int i = 0; i < routeList.size(); i++) {
//            j = i + 1;
//            if (j>=routeList.size()) break;
//            String[] currRegions = routeList.get(i).getRegion(); //Get regions first from each RouteCCTV Object //current
//            String[] r = routeList.get(j).getRegion();
//
//            if (!Arrays.equals(currRegions,r)){
//                Log.e(getTag(), "i 's value:" + currRegions[1] + " j's value:" + r[1]);
//                if (pref.equals(MyApplication.Language.ENGLISH)){
//                    curr = currRegions[0];
//                    region = r[0];
//                }else{
//                    curr = currRegions[1];
//                    region = r[1];
//                }
//                sortedRegions.add(region);
//            }else{
//                continue;
//            }
//
//        }
//
//
//        return sortedRegions;
//    }


    public class MapInfoAdapter implements GoogleMap.InfoWindowAdapter{

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {

            View view = null;
            ViewHolder viewHolder = null;
            if (view == null){
                view = getLayoutInflater(savedInstanceState).
                        inflate(R.layout.popup_map_snapshot, null, false);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            }else
                viewHolder = (ViewHolder)view.getTag();

            //Operations
            String key = marker.getTitle();
            String roadkey = roadCCTVMap.get(key);

            String URL = TRAFFIC_URL.concat(roadkey).concat(JPG);
            Log.e(getTag(), URL);
            Picasso.with(getContext()).load(URL).into(viewHolder.imgRoutecctv);
            viewHolder.cctvtitle.setText(key);
            viewHolder.imgAddBK.setImageResource(R.drawable.ic_bookmark);
            viewHolder.btnMore.setText("More");
            //Picasso.with(getContext()).load(R.drawable.ic_bookmark).into(viewHolder.imgAddBK);

            return view;
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            ImageView imgRoutecctv;
            ImageButton imgAddBK;
            Button btnMore;
            TextView cctvtitle;

            public ViewHolder(View itemView) {
                super(itemView);
                cctvtitle = (TextView)itemView.findViewById(R.id.map_snapshot_title);
                imgRoutecctv = (ImageView)itemView.findViewById(R.id.map_snapshot);
                imgAddBK = (ImageButton)itemView.findViewById(R.id.imgAddBookMark);
                btnMore = (Button)itemView.findViewById(R.id.btnBKMore);
            }
        }
    }
}
