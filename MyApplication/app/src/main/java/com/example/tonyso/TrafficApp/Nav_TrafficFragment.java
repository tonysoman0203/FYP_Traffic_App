package com.example.tonyso.TrafficApp;


import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.tonyso.TrafficApp.Interface.ImageProcessCallback;
import com.example.tonyso.TrafficApp.Interface.Rss_Listener;
import com.example.tonyso.TrafficApp.baseclass.BaseFragment;
import com.example.tonyso.TrafficApp.model.RouteCCTV;
import com.example.tonyso.TrafficApp.utility.LanguageSelector;
import com.example.tonyso.TrafficApp.rss.XMLReader;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

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
    Map<String,RouteCCTV> roadCCTVMap = new HashMap<>();

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
        mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        setHasOptionsMenu(true);
        this.savedInstanceState = savedInstanceState;
        this.coordinatorLayout = (CoordinatorLayout)getActivity().findViewById(R.id.coordinateLayoutMain);

        spinner = (Spinner) v.findViewById(R.id.spinner);
        languageSelector = new LanguageSelector(getContext());
        XMLReader xmlReader = new XMLReader(this.getContext(),this);
        xmlReader.feedImageXml();

        mapFragment.getMapAsync(this);

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        RouteCCTV routeCCTV;
        setGoogleMapProperty();
        String desc ;
        if (routeList.size()!=0){
            for (int count = 0;count<routeList.size();count++){
                routeCCTV = routeList.get(count);
//                key = routeList .get(count).getKey();
                double[] latlng = routeList.get(count).getLatLngs();
                LatLng sydney = new LatLng(latlng[0],latlng[1]);
                if (languageSelector.getLanguage().equals(MyApplication.Language.ENGLISH)){
                    desc = routeCCTV.getDescription()[0];
                    roadCCTVMap.put(desc,routeCCTV);
                }else{
                    desc = routeCCTV.getDescription()[1];
                    roadCCTVMap.put(desc,routeCCTV);
                }

                mMap.addMarker(new MarkerOptions().position(sydney).title(desc));
            }
            mMap.setOnMarkerClickListener(this);
            mMap.setInfoWindowAdapter(new MapInfoAdapter());
        }
    }


    /*Set Google Map property*/
    private void setGoogleMapProperty(){
        mMap.setMyLocationEnabled(true);
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
        final String []arr = getResources().getStringArray(R.array.regions);
        ArrayAdapter<String> dataadpater = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,arr);
        dataadpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataadpater);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Snackbar.make(coordinatorLayout,arr[position] , Snackbar.LENGTH_SHORT).show();
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
            ViewHolder viewHolder;
            if (view == null){
                view = getLayoutInflater(savedInstanceState).
                        inflate(R.layout.popup_map_snapshot, null, false);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            }else
                viewHolder = (ViewHolder)view.getTag();

            //Operations
            String title = marker.getTitle();
            RouteCCTV routeCCTV = roadCCTVMap.get(title);
            String imgKey = routeCCTV.getKey();
            String URL = TRAFFIC_URL.concat(imgKey).concat(JPG);
            //Log.e(getTag(), URL);
            final ViewHolder finalViewHolder = viewHolder;
            Picasso.with(getContext()).load(URL).into(viewHolder.imgRoutecctv
                    , new ImageProcessCallback(finalViewHolder.progressBar) {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                }
            });

            viewHolder.cctvtitle.setText(title);
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
            ProgressBar progressBar;

            public ViewHolder(View itemView) {
                super(itemView);
                cctvtitle = (TextView)itemView.findViewById(R.id.map_snapshot_title);
                imgRoutecctv = (ImageView)itemView.findViewById(R.id.map_snapshot);
                imgAddBK = (ImageButton)itemView.findViewById(R.id.imgAddBookMark);
                btnMore = (Button)itemView.findViewById(R.id.btnBKMore);
                progressBar = (ProgressBar)itemView.findViewById(R.id.progressBar);
            }
        }
    }
}
