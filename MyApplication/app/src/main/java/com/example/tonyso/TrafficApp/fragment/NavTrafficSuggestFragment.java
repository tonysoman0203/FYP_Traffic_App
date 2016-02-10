package com.example.tonyso.TrafficApp.fragment;

import android.animation.Animator;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import com.cocosw.bottomsheet.BottomSheet;
import com.example.tonyso.TrafficApp.MyApplication;
import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.baseclass.BaseFragment;
import com.example.tonyso.TrafficApp.location.DrawPathAsyncTask;
import com.example.tonyso.TrafficApp.location.GetLocationAsyncTask;
import com.example.tonyso.TrafficApp.utility.ErrorDialog;
import com.example.tonyso.TrafficApp.utility.ShareStorage;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class NavTrafficSuggestFragment extends BaseFragment implements
        GoogleMap.OnMapLongClickListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String TAG = NavTrafficSuggestFragment.class.getCanonicalName();
    GetLocationAsyncTask task;
    Toolbar toolbar;
    SupportMapFragment mapFragment;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private GoogleMap mGoogleMap;
    /**
     * 28/1/2016 Route Suggestion Layout Implementation
     */
    private FrameLayout frameLayout;
    private EditText origin, destination;
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
        } else {
            mParam1 = getContext().getString(R.string.title_routeSuggest);
        }
        super.getInstance();
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
        setFragmentToolbar();
        initGoogleMap();
        initPopupDialog(view);
    }

    private void setFragmentToolbar() {
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(mParam1);
    }

    private void initPopupDialog(View v) {
        frameLayout = (FrameLayout) v.findViewById(R.id.dialog_main);
        View inputDialogView = frameLayout.findViewById(R.id.suggest_dialog);
        //Initialize EditText
        origin = (EditText) inputDialogView.findViewById(R.id.originEditText);
        destination = (EditText) inputDialogView.findViewById(R.id.destinationEditText);
        //Initialize Button
        Button btnSubmit = (Button) inputDialogView.findViewById(R.id.btnSuggestSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (origin.getText() == null || destination.getText() == null) {
                    //show errorDialog
                    ErrorDialog errorDialog = ErrorDialog.getInstance(getContext());
                    errorDialog.displayAlertDialog("Input Error! Please select again...");
                    origin.setText("");
                    destination.setText("");
                } else {
                    //fetch path
                    String[] l = new String[]{origin.getText().toString(), destination.getText().toString()};
                    if (mGoogleMap != null) {
                        DrawPathAsyncTask drawPathAsyncTask = new DrawPathAsyncTask(
                                NavTrafficSuggestFragment.this, getActivity(), mGoogleMap, l, routeList, routeSpeedMap);
                        drawPathAsyncTask.execute();
                    }

                }
            }
        });
        Button btnReset = (Button) inputDialogView.findViewById(R.id.btnSuggestReset);
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
                        .setDuration(1000)
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
                            }
                            @Override
                            public void onAnimationRepeat(Animator animation) {
                            }
                        });
                mGoogleMap.clear();
            }
        });

        //ImageButton
        /*
      Navigate and get current location
     */
        ImageButton imgCurrentLocation = (ImageButton) inputDialogView.findViewById(R.id.imageButton);
        imgCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                origin.setText(getCurrentLocation());
                MarkerOptions markerOptions = new MarkerOptions()
                        .title(getCurrentLocation().toString())
                        .position(getCurrentLocationByLatLng());
                mGoogleMap.addMarker(markerOptions);
                animateDialogView();
            }
        });
    }


    private void initGoogleMap() {
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        //Sync Map
        mapFragment.getMapAsync(this);
        snackbar = Snackbar.make(getActivity().findViewById(R.id.coordinateLayoutMain),
                getString(R.string.snackbar_route_suggest_orgin), Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(getString(R.string.route_suggestion_current), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                origin.setText(getCurrentLocation());
                MarkerOptions markerOptions = new MarkerOptions()
                        .title(getCurrentLocation().toString())
                        .position(getCurrentLocationByLatLng());
                mGoogleMap.addMarker(markerOptions);
                animateDialogView();
                snackbar = Snackbar.make(getActivity().findViewById(R.id.coordinateLayoutMain)
                        , getString(R.string.snackbar_route_suggest_destination), Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
            }
        });
        snackbar.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        snackbar.dismiss();
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
        BottomSheet.Builder builder = new BottomSheet.Builder(getActivity())
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
                                animateDialogView();
                                snackbar.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                });

        final BottomSheet b = builder.build();
        b.getMenu().getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                b.dismiss();
                return true;
            }
        });
        b.show();
        return true;
    }


    private void animateDialogView() {
        //move map Fragment under input dialog view
        mapFragment.getView().animate()
                .translationY(500)
                .setDuration(1000)
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
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
    }

    public StringBuffer getCurrentLocation() {
        StringBuffer sb = new StringBuffer();
        sb.append(ShareStorage.retrieveData("address", ShareStorage.SP.ProtectedData, getContext()));
        return sb;
    }

    public LatLng getCurrentLocationByLatLng() {
        double[] lls = new double[]{
                Double.parseDouble(ShareStorage.retrieveData("lat", ShareStorage.SP.ProtectedData, getContext())),
                Double.parseDouble(ShareStorage.retrieveData("lng", ShareStorage.SP.ProtectedData, getContext()))
        };
        return new LatLng(lls[0], lls[1]);
    }
}
