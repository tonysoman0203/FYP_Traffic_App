package com.example.tonyso.TrafficApp.fragment;

import android.animation.Animator;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.InflateException;
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
import com.example.tonyso.TrafficApp.model.Place;
import com.example.tonyso.TrafficApp.utility.Current;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;


public class NavTrafficSuggestFragment extends BaseFragment implements
        GoogleMap.OnMapClickListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String TAG = NavTrafficSuggestFragment.class.getCanonicalName();
    private static final Integer TYPE_GPS = 1;
    private static final Integer TYPE_ASYNCTASK = 2;

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
    private View view;

    public static Map<String, Place> placeMap;
    public static int place_ids = -1;

    private Place[] places;

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
        placeMap = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        // Inflate the layout for this fragment
        try {
            view = inflater.inflate(R.layout.fragment_nav_traffic_suggest, container, false);
        } catch (InflateException i) {
            i.printStackTrace();
        }
        return view;
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
        final TextInputLayout orginInput = (TextInputLayout) inputDialogView.findViewById(R.id.originWrapper);
        final TextInputLayout destInput = (TextInputLayout) inputDialogView.findViewById(R.id.destinationWrapper);

        //Initialize Button
        Button btnSubmit = (Button) inputDialogView.findViewById(R.id.btnSuggestSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(origin.getText())) {
                    orginInput.setError("Input Error...Please select again...");
                    origin.setText("");
                    destination.setText("");
                } else if (TextUtils.isEmpty(destination.getText())) {
                    destInput.setError("Input Error...Please select again...");
                    origin.setText("");
                    destination.setText("");
                } else {
                    //fetch path
                    String[] l = new String[]{origin.getText().toString(), destination.getText().toString()};
                    Place origin = buildPlace(l[0]);
                    Place destination = buildPlace(l[1]);
                    DrawPathAsyncTask drawPathAsyncTask = new DrawPathAsyncTask(
                            NavTrafficSuggestFragment.this, getActivity(), origin, destination);
                    drawPathAsyncTask.execute();
                }
            }
        });
        Button btnReset = (Button) inputDialogView.findViewById(R.id.btnSuggestReset);
        //Set OnclickListener to Reset Button to reset EditTexts
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetView();
            }
        });

        ImageButton imgCurrentLocation = (ImageButton) inputDialogView.findViewById(R.id.imageButton);
        imgCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                origin.setText(Current.getCurrentAddress(getContext()));
                MarkerOptions markerOptions = new MarkerOptions()
                        .title(Current.getCurrentAddress(getContext()).toString())
                        .position(Current.getCurrentLocationByLatLng(getContext()));
                mGoogleMap.addMarker(markerOptions);
            }
        });
    }

    private void resetView() {
        origin.setText("");
        destination.setText("");
        snackbar = Snackbar.make(getActivity().findViewById(R.id.coordinateLayoutMain),
                getString(R.string.snackbar_route_suggest_orgin), Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(getString(R.string.route_suggestion_current), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                origin.setText(Current.getCurrentAddress(getContext()));
                MarkerOptions markerOptions = new MarkerOptions()
                        .title(Current.getCurrentAddress(getContext()).toString())
                        .position(Current.getCurrentLocationByLatLng(getContext()));
                mGoogleMap.addMarker(markerOptions);
                animateDialogView(true);
            }
        });
        snackbar.show();
        animateDialogView(false);
        mGoogleMap.clear();
    }

    private Place buildPlace(String key) {
        Place place;
        if (placeMap.get(key) != null) {
            place = placeMap.get(key);
            System.out.print(place.toString());
        } else {
            place = new Place();
            place.setPlaceId("" + (place_ids++));
            place.setName(Current.getCurrentLocationName(getContext()));
            place.setAddress(Current.getCurrentAddress(getContext()));
            place.setLatlngs(Current.getLatLngInDouble(getContext()));
            place.setDistrict(Current.getCurrentDistrict(getContext()));
        }
        return place;
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
                origin.setText(Current.getCurrentAddress(getContext()));
                MarkerOptions markerOptions = new MarkerOptions()
                        .title(Current.getCurrentAddress(getContext()).toString())
                        .position(Current.getCurrentLocationByLatLng(getContext()));
                mGoogleMap.addMarker(markerOptions);
                animateDialogView(true);
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
        mapFragment.onDetach();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        task = new GetLocationAsyncTask();
        task.setLatlng(latLng);
        task.setGoogleMap(mGoogleMap);
        task.execute(MyApplication.CURR_LANG);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.setOnMapClickListener(this);
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
                                animateDialogView(true);
                                snackbar.dismiss();
                                break;
                            case R.id.nav_traffic_destination:
                                destination.setText(m.getTitle());
                                animateDialogView(true);
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

    private void animateDialogView(final boolean status) {
        //move map Fragment under input dialog view
        mapFragment.getView().animate()
                .translationY((status) ? 500 : 0)
                .setDuration(1000)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (status) {
                            frameLayout.setVisibility(View.VISIBLE);
                            snackbar = Snackbar.make(getActivity().findViewById(R.id.coordinateLayoutMain)
                                    , getString(R.string.snackbar_route_suggest_destination), Snackbar.LENGTH_INDEFINITE);
                            snackbar.show();
                        } else
                            frameLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
    }
}
