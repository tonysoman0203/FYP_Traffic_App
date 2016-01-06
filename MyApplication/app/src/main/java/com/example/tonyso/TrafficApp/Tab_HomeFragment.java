package com.example.tonyso.TrafficApp;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tonyso.TrafficApp.adapter.HomeAdapter;
import com.example.tonyso.TrafficApp.baseclass.TabBaseFragment;
import com.example.tonyso.TrafficApp.model.Place;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FadeInRightAnimator;

public class Tab_HomeFragment extends TabBaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = Tab_HomeFragment.class.getName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private GoogleApiClient mGoogleApiClient;

    private List<Place> places;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab_Home_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Tab_HomeFragment newInstance(String param1, String param2) {
        Tab_HomeFragment fragment = new Tab_HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static Tab_HomeFragment newInstance(String title, int indicatorColor, int dividerColor, int icon) {
        Tab_HomeFragment baseFragment = new Tab_HomeFragment();
        baseFragment.setTitle(title);
        baseFragment.setDividerColor(dividerColor);
        baseFragment.setIcon(icon);
        baseFragment.setIndicatorColor(indicatorColor);
        return baseFragment;
    }

    public Tab_HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleApiClient = MainActivity.getmGoogleApiClient();
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    private void setPlaces() {
        places = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                .getCurrentPlace(mGoogleApiClient, null);
        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                    Place place = new Place().
                            setPlaceId(placeLikelihood.getPlace().getId())
                            .setAddress(placeLikelihood.getPlace().getAddress())
                            .setLatlngs(placeLikelihood.getPlace().getLatLng())
                            .setName(placeLikelihood.getPlace().getName())
                            .setPhoneno(placeLikelihood.getPlace().getPhoneNumber())
                            .build();
                    places.add(place);
                    Log.i(TAG, String.format("Place '%s' has likelihood: %g",
                            placeLikelihood.getPlace().getName(),
                            placeLikelihood.getLikelihood()));
                }
                likelyPlaces.release();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_tab__home_, container, false);
        setPlaces();
        initializeUIView(view);
        return view;
    }

    private void initializeUIView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.home_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshHomeLayout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLUE, Color.GREEN, Color.CYAN);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.setItemAnimator(new FadeInRightAnimator());
        mRecyclerView.setAdapter(new HomeAdapter(this.getContext(), places));
    }

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                }

            }, 5000);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
