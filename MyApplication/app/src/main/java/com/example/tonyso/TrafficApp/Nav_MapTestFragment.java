package com.example.tonyso.TrafficApp;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.tonyso.TrafficApp.baseclass.BaseFragment;
import com.example.tonyso.TrafficApp.utility.Convertor;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * A simple {@link Fragment} subclass.
 */
public class Nav_MapTestFragment extends BaseFragment implements OnMapReadyCallback {


    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    TextInputLayout usernameWrapper,passwordWrapper;
    Button convert;
    TextView tv;

    public Nav_MapTestFragment() {
        // Required empty public constructor
    }

    public static Nav_MapTestFragment newInstance() {
        Nav_MapTestFragment f = new Nav_MapTestFragment();
        Bundle args = new Bundle();
        args.putInt("index", 2);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_traffic_suggestions, container, false);
        usernameWrapper = (TextInputLayout) view.findViewById(R.id.northWrapper);
        passwordWrapper = (TextInputLayout) view.findViewById(R.id.EastWrapper);
        //final Button displayMap = (Button)view.findViewById(R.id.button);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        convert = (Button)view.findViewById(R.id.button2);
        tv = (TextView)view.findViewById(R.id.textView2);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapFragment.getMapAsync(this);

        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Convertor convertor = new Convertor(usernameWrapper.getEditText().getText().toString(),
                        passwordWrapper.getEditText().getText().toString());
                tv.setText("The Converted to ==> N: " + convertor.output()[0] + ", E:" + convertor.output()[1]);
                showInMap(convertor.output()[0],convertor.output()[1]);
            }
        });

    }

    private void showInMap(double v, double v1) {
        LatLng sydney = new LatLng(v,v1);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
    }
}
