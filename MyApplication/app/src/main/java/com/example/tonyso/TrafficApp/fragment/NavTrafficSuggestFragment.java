package com.example.tonyso.TrafficApp.fragment;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.example.tonyso.TrafficApp.location.LocationPlacesJsonParser;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


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
                } else {
                    //fetch path
                    String[] l = new String[]{origin.getText().toString(), destination.getText().toString()};
                    if (mGoogleMap != null) {
                        DrawPathAsyncTask drawPathAsyncTask = new DrawPathAsyncTask(getActivity(), mGoogleMap, l);
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
                mapFragment.getMap().clear();

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
                        //inputDialogView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }

    public class DrawPathAsyncTask extends AsyncTask<Void, Void, String> {
        private final String TAG = DrawPathAsyncTask.class.getCanonicalName();
        Context context;
        GoogleMap mMap;
        String[] location;
        private ProgressDialog progressDialog;

        public DrawPathAsyncTask(Context context, GoogleMap map, String[] location) {
            this.context = context;
            this.mMap = map;
            this.location = location;
        }

        @Override
        protected String doInBackground(Void... params) {
            LocationPlacesJsonParser jsonParser = new LocationPlacesJsonParser(context.getString(R.string.place_api_server_key));
            String url = jsonParser.makeDistanceURL(location[0], location[1], null, null);
            return jsonParser.getJSON(url);
        }

        private List<LatLng> decodePoly(String encoded) {

            List<LatLng> poly = new ArrayList<LatLng>();
            int index = 0, len = encoded.length();
            int lat = 0, lng = 0;

            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;

                shift = 0;
                result = 0;
                do {
                    b = encoded.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;

                LatLng p = new LatLng((((double) lat / 1E5)),
                        (((double) lng / 1E5)));
                poly.add(p);
            }

            return poly;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Fetching Routes.......");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e(TAG, s);
            progressDialog.hide();
            if (s != null) {
                drawPath(s);
            }
        }

        public void drawPath(String result) {

            try {
                //Tranform the string into a json object
                final JSONObject json = new JSONObject(result);
                JSONArray routeArray = json.getJSONArray("routes");
                JSONObject routes = routeArray.getJSONObject(0);
                JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
                String encodedString = overviewPolylines.getString("points");
                List<LatLng> list = decodePoly(encodedString);
                Polyline line = mMap.addPolyline(new PolylineOptions()
                                .addAll(list)
                                .width(12)
                                .color(Color.parseColor("#05b1fb"))//Google maps blue color
                                .geodesic(true)
                );

           /*
           for(int z = 0; z<list.size()-1;z++){
                LatLng src= list.get(z);
                LatLng dest= list.get(z+1);
                Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude,   dest.longitude))
                .width(2)
                .color(Color.BLUE).geodesic(true));
            }
           */
            } catch (JSONException e) {

            }
        }
    }
}
