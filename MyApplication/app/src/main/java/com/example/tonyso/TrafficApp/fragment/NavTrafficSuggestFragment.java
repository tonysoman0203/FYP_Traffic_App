package com.example.tonyso.TrafficApp.fragment;

import android.animation.Animator;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.cocosw.bottomsheet.BottomSheet;
import com.example.tonyso.TrafficApp.MainActivity;
import com.example.tonyso.TrafficApp.MyApplication;
import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.location.GetLocationAsyncTask;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;


public class NavTrafficSuggestFragment extends Fragment implements
        GoogleMap.OnMapLongClickListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String TAG = NavTrafficSuggestFragment.class.getCanonicalName();
    GetLocationAsyncTask task;
    MyApplication myapp;
    Toolbar toolbar;
    SupportMapFragment mapFragment;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private GoogleMap mGoogleMap;
    /**
     * 28/1/2016 Route Suggestion Layout Implementation
     */
    private FrameLayout frameLayout;
    private View inputDialogView;
    private EditText origin, destination;
    private Button btnSubmit, btnReset;
    private AppBarLayout appBarLayout;
    private Snackbar snackbar;

    public NavTrafficSuggestFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NavTrafficSuggestFragment newInstance(String param1) {
        NavTrafficSuggestFragment fragment = new NavTrafficSuggestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nav_traffic_suggest, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myapp = (MyApplication) MainActivity.activity.getApplication();
        setFragmentToolbar();
        initGoogleMap();
        initPopupDialog(view);
    }

    private void setFragmentToolbar() {
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(mParam1);
        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appbar);
    }

    private void initPopupDialog(View v) {
        frameLayout = (FrameLayout) v.findViewById(R.id.dialog_main);
        inputDialogView = frameLayout.findViewById(R.id.suggest_dialog);
        //Initialize EditText
        origin = (EditText) inputDialogView.findViewById(R.id.originEditText);
        destination = (EditText) inputDialogView.findViewById(R.id.destinationEditText);
        //Initialize Button
        btnSubmit = (Button) inputDialogView.findViewById(R.id.btnSuggestSubmit);
        btnReset = (Button) inputDialogView.findViewById(R.id.btnSuggestReset);
        //Set OnclickListener to Reset Button to reset EditTexts
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                origin.setText("");
                destination.setText("");
                snackbar = Snackbar.make(getActivity().findViewById(R.id.coordinateLayoutMain),
                        getString(R.string.snackbar_route_suggest_orgin), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
                mapFragment.getView().animate()
                        .translationY(0)
                        .setDuration(3000)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                frameLayout.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                                //inputDialogView.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });

            }
        });
    }


    private void initGoogleMap() {
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        //Sync Map
        mapFragment.getMapAsync(this);
        snackbar = Snackbar.make(getActivity().findViewById(R.id.coordinateLayoutMain), getString(R.string.snackbar_route_suggest_orgin), Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        task = new GetLocationAsyncTask();
        task.setLatlng(latLng);
        task.setApplication((MyApplication) getActivity().getApplication());
        task.setGoogleMap(mGoogleMap);
        task.execute();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.setOnMapLongClickListener(this);
        mGoogleMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        final Marker m = marker;
        new BottomSheet.Builder(getActivity())
                .title(marker.getTitle())
                .sheet(R.menu.routesuggest)
                .listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case R.id.nav_route_suggest_origin:
                                origin.setText(m.getTitle());
                                animateDialogView();
                                snackbar.dismiss();
                                snackbar = Snackbar.make(getActivity().findViewById(R.id.coordinateLayoutMain)
                                        , getString(R.string.snackbar_route_suggest_destination), Snackbar.LENGTH_INDEFINITE);
                                snackbar.show();
                                break;
                            case R.id.nav_traffic_destination:
                                destination.setText(m.getTitle());
                                break;
                            default:
                                break;
                        }
                    }
                }).show();
        return true;
    }

    private void animateDialogView() {
        //move map Fragment under input dialog view
        mapFragment.getView().animate()
                .translationY(500)
                .setDuration(3000)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        frameLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        //inputDialogView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

    }
}
