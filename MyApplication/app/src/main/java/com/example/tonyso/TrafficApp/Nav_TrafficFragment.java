package com.example.tonyso.TrafficApp;


import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import android.widget.Toolbar;

import com.example.tonyso.TrafficApp.Interface.Rss_Listener;
import com.example.tonyso.TrafficApp.baseclass.BaseFragment;
import com.example.tonyso.TrafficApp.model.Route;
import com.example.tonyso.TrafficApp.model.RouteCCTV;
import com.example.tonyso.TrafficApp.Singleton.LanguageSelector;
import com.example.tonyso.TrafficApp.rss.XMLReader;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * A simple {@link Fragment} subclass.
 */

public class Nav_TrafficFragment extends BaseFragment implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener,GoogleMap.OnMarkerClickListener{

    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private List<RouteCCTV> routeList;
    private LanguageSelector languageSelector;
    Map<String,RouteCCTV> roadCCTVMap = new HashMap<>();
    Map<String,LatLng> regionMap = new HashMap<>();

    private static String TRAFFIC_URL = "http://tdcctv.data.one.gov.hk/";
    private static String JPG = ".JPG";

    private Bundle savedInstanceState;

    //Spinner Test
    private Spinner spinner;

    CoordinatorLayout coordinatorLayout;
    String []arr,latlng;

    Toolbar toolbar;

    boolean isTrafficOn;

    public Nav_TrafficFragment() {
        // Required empty public constructor
    }

    ImageLoader imageLoader;

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
        imageLoader = ImageLoader.getInstance();
        setHasOptionsMenu(true);
        this.savedInstanceState = savedInstanceState;
        this.coordinatorLayout = (CoordinatorLayout)getActivity().findViewById(R.id.coordinateLayoutMain);

        //Spinner to let user select different district to see RouteCCTV
        spinner = (Spinner) v.findViewById(R.id.spinner);
        setSpinnerProperty();
        languageSelector = LanguageSelector.getInstance(getContext());
        MyApplication myApplication = (MyApplication)getActivity().getApplication();
        routeList = myApplication.list;
        //XMLReader xmlReader = new XMLReader(this.getContext(),this);
        //xmlReader.feedImageXml();
        mapFragment.getMapAsync(this);
        onSpinnerItemSelected();
        return v;
    }

    private void onSpinnerItemSelected() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                removePreviousMarker();
                setMarker(mMap,position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void removePreviousMarker() {mMap.clear();}

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
        ArrayAdapter<String> dataadpater = new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,arr);
        dataadpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataadpater);
        spinner.setSelection(0);
    }

    /**
     *  This mehtod setMarker is to setup the marker based on user selected
     *  @param googleMap is a Google Map Instance
     *  @param posDistrict is the user selection id
     *
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
            double[] latlng = selectedRoute.get(count).getLatLngs();
            LatLng location = new LatLng(latlng[0],latlng[1]);
            if (languageSelector.getLanguage().equals(MyApplication.Language.ENGLISH)){
                desc = routeCCTV.getDescription()[0];
                roadCCTVMap.put(desc,routeCCTV);
            }else{
                desc = routeCCTV.getDescription()[1];
                roadCCTVMap.put(desc,routeCCTV);
            }
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(location);
            markerOptions.title(desc);
            mMap.addMarker(markerOptions);
        }
        mMap.setOnMarkerClickListener(this);
        mMap.setInfoWindowAdapter(new MapInfoAdapter());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_nav_traffic_monitoring,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_traffic_terrain:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.nav_traffic_satelite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.nav_traffic_traffic:
                if(isTrafficOn){
                    mMap.setTrafficEnabled(false);
                    isTrafficOn = false;
                }else{
                    mMap.setTrafficEnabled(true);
                    isTrafficOn = true;
                }
                break;
            case R.id.nav_traffic_default:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setGoogleMapProperty(mMap);
    }

    /** The Method setGoogleMapProperty is to set the property of map
     *
     * @param googleMap
     */
    private void setGoogleMapProperty(GoogleMap googleMap){
        googleMap.setMyLocationEnabled(true);
    }

    @Override
    public void onMapClick(LatLng latLng) {}

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return false;
    }

//    @Override
//    public void ParsedInfo(List list) {
//        routeList = list;
//
//    }




    public class MapInfoAdapter implements GoogleMap.InfoWindowAdapter{

        private DisplayImageOptions options;
        private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

        public MapInfoAdapter() {
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.ic_action_loading)
                    .showImageForEmptyUri(R.drawable.ic_error_black_24dp)
                    .cacheInMemory(true)
                    .considerExifParams(true)
                    .displayer(new SimpleBitmapDisplayer())
                    .build();
        }

        @Override
        public View getInfoWindow(Marker marker) {return null;}

        @Override
        public View getInfoContents(Marker marker) {

            View view = null;
            final ViewHolder viewHolder;
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
            imageLoader.displayImage(URL, viewHolder.imgRoutecctv, options,animateFirstListener);

//            Picasso.with(getContext()).load(URL).into(viewHolder.imgRoutecctv
//                    , new Callback() {
//                @Override
//                public void onSuccess() {
//
//                }
//
//                @Override
//                public void onError() {
//
//                }
//            });

            viewHolder.cctvtitle.setText(title);
            viewHolder.btnMore.setText(getString(R.string.bookMark_More));
            //Picasso.with(getContext()).load(R.drawable.ic_bookmark).into(viewHolder.imgAddBK);

            return view;
        }



        public class ViewHolder extends RecyclerView.ViewHolder{
            ImageView imgRoutecctv;
            Button btnMore,btnAddBookmark,btnFindNear;
            TextView cctvtitle;
            ProgressBar progressBar;

            public ViewHolder(View itemView) {
                super(itemView);
                cctvtitle = (TextView)itemView.findViewById(R.id.map_snapshot_title);
                imgRoutecctv = (ImageView)itemView.findViewById(R.id.map_snapshot);
                btnAddBookmark = (Button)itemView.findViewById(R.id.imgAddBookMark);
                btnMore = (Button)itemView.findViewById(R.id.btnRouteMore);
                btnFindNear = (Button)itemView.findViewById(R.id.btnPopupNear);
                progressBar = (ProgressBar)itemView.findViewById(R.id.progressbar);
            }
        }
    }
    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
}
