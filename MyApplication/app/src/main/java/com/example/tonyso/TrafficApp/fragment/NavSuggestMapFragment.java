package com.example.tonyso.TrafficApp.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.baseclass.BaseFragment;
import com.example.tonyso.TrafficApp.model.Place;
import com.example.tonyso.TrafficApp.model.RouteCCTV;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;


public class NavSuggestMapFragment extends BaseFragment
        implements OnMapReadyCallback
        , GoogleMap.OnMapClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    List<RouteCCTV> cctvList ;
    List<Double[]>pathsInDouble;

    private static String ARG_DIS = "distance";
    private static final String ARG_PATHS = "Paths";
    private static final String ARG_CCTV = "cctvInPaths";

    // TODO: Rename and change types of parameters
    SupportMapFragment mapFragment;
    private View view;
    GoogleMap mMap;

    private String[] distance;

    public NavSuggestMapFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NavSuggestMapFragment newInstance(String near, int indicatorColor, int dividerColor,
                                                    int ic_home, Place origin, Place destination, String[]
                                                            arrDisDuration, ArrayList<Double[]> paths, ArrayList<RouteCCTV> cctvsInPaths) {
        NavSuggestMapFragment f = new NavSuggestMapFragment();
        f.setTitle(near);
        f.setIndicatorColor(indicatorColor);
        f.setDividerColor(dividerColor);
        f.setIcon(ic_home);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_PARAM1, origin);
        bundle.putSerializable(ARG_PARAM2, destination);
        bundle.putStringArray(ARG_DIS, arrDisDuration);
        bundle.putSerializable(ARG_PATHS, paths);
        bundle.putSerializable(ARG_CCTV, cctvsInPaths);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getInstance();
        if (getArguments() != null) {
            Place origin = (Place) getArguments().getSerializable(ARG_PARAM1);
            Place destination = (Place) getArguments().getSerializable(ARG_PARAM2);
            distance = getArguments().getStringArray(ARG_DIS);
            pathsInDouble = (ArrayList<Double[]>) getArguments().getSerializable(ARG_PATHS);
            cctvList = (ArrayList<RouteCCTV>)getArguments().getSerializable(ARG_CCTV);
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        List<LatLng>paths = convertFromDoubleToLatLng();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(paths.get(0), 15));
        Polyline line = mMap.addPolyline(new PolylineOptions()
                        .addAll(paths)
                        .width(14)
                        .color(Color.parseColor("#05b1fb"))//Google maps blue color
                        .geodesic(true)
        );

        Marker des = mMap.addMarker(new MarkerOptions()
                        .title(distance[1])
                        .position(paths.get(paths.size() - 1))
        );

        Marker originM = mMap.addMarker(new MarkerOptions()
                        .position(paths.get(0))
                        .title(distance[1])
        );


//        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//            @Override
//            public View getInfoWindow(Marker marker) {
//                return null;
//            }
//
//            @Override
//            public View getInfoContents(Marker marker) {
//                View view = LayoutInflater.from(getContext()).inflate(R.layout.item_suggest_marker_detail_map, null, false);
//                TextView mT = (TextView) view.findViewById(R.id.txtMarkerTitle);
//                mT.setTextColor(Color.parseColor("#FFC107"));
//                mT.setText(distance[1]);
//                return view;
//            }
//        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });

        for(int i = 0;i<cctvList.size();i++){
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.title(cctvList.get(i).getDescription()[1]);
            markerOptions.position(new LatLng(cctvList.get(i).getLatLngs()[0],cctvList.get(i).getLatLngs()[1]));
            mMap.addMarker(markerOptions);
        }
    }

    private List<LatLng>convertFromDoubleToLatLng(){
        List<LatLng> path = new ArrayList<>();
        for (Double[] doubles : pathsInDouble){
            LatLng latLng = new LatLng(doubles[0],doubles[1]);
            path.add(latLng);
        }
        return path;
    }
}
