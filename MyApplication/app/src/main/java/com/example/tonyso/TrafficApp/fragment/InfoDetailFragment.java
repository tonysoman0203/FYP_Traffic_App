package com.example.tonyso.TrafficApp.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tonyso.TrafficApp.MyApplication;
import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.adapter.InfoDetailAdapter;
import com.example.tonyso.TrafficApp.baseclass.BaseDialogFragment;
import com.example.tonyso.TrafficApp.model.RouteCCTV;
import com.example.tonyso.TrafficApp.model.TimedBookMark;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.places.Places;

import jp.wasabeef.recyclerview.animators.FadeInAnimator;

//import android.support.v7.graphics.Palette;

public class InfoDetailFragment extends BaseDialogFragment
        implements GoogleApiClient.ConnectionCallbacks, OnConnectionFailedListener {
    //Constant
    public static final String TAG = InfoDetailFragment.class.getName();
    public static final String KEY = "key";
    public static final String ADD_ROUTE_TYPE = "Add_ROUTE";
    public static final String VIEW_HISTORY_RECORD = "View History";

    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    private static final int RECYCLER_VIEW_SIZE = 3;
    private static final String STATE_RESOLVING_ERROR = "resolving_error";
    private static final String TRAFFIC_URL = "http://tdcctv.data.one.gov.hk/";
    private static final String TRAFFIC_SPEED_MAP = "http://resource.data.one.gov.hk/td/";
    private static final String TC = "TC";
    private static final String EN = "EN";
    private static final String JPG = ".JPG";
    private static final String PNG = ".png";
    private static String imgKey = "";
    public CoordinatorLayout coordinatorLayout;
    //UI Components.....
    Toolbar toolbar;
    FloatingActionButton fab;
    CollapsingToolbarLayout collapsingToolbarLayout;
    //Variable
    ImageView imageRoute;
    TextView txtSubtitle, title;
    String imageKey;
    Bundle intent;
    RouteCCTV route;
    //Action
    RecyclerView recyclerView;
    InfoDetailAdapter infoDetailAdapter;
    TimedBookMark bookMark;
    String type = "Add_ROUTE";

    GoogleApiClient mGoogleApiClient;
    FragmentManager fm;


    // Bool to track whether the app is already resolving an error
    private static boolean mResolvingError = false;
    private View view;

    public InfoDetailFragment() {}

    public static InfoDetailFragment newInstance(int key, String type) {
        InfoDetailFragment fragment = new InfoDetailFragment();
        Bundle args = new Bundle();
        args.putInt(KEY, key);
        args.putString("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    public static InfoDetailFragment newInstance(String key, String type, RouteCCTV obj) {
        InfoDetailFragment fragment = new InfoDetailFragment();
        Bundle args = new Bundle();
        args.putString(KEY, key);
        args.putString("type",type);
        args.putSerializable("route", obj);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        //savedInstanceState.putBoolean(STATE_RESOLVING_ERROR, mResolvingError);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_nav_traffic_info_detail_main, container, false);
        } catch (InflateException e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = getChildFragmentManager();
        mResolvingError = savedInstanceState != null
                && savedInstanceState.getBoolean(STATE_RESOLVING_ERROR, false);
        buildGoogleApiClient();
        super.getInstance();
        getDataFromIntent();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLayoutComponents();
        setImageHeader();

    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient
                .Builder(this.getContext())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    private void initLayoutComponents() {
        setToolbar();
        //init ImageView
        imageRoute = (ImageView) view.findViewById(R.id.header);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinate_layout);
        infoDetailAdapter = new InfoDetailAdapter()
                .setGoogleApiClient(mGoogleApiClient)
                .setContext(this)
                .setSize(RECYCLER_VIEW_SIZE)
                .setCoordinatorLayout(coordinatorLayout)
                .setRoute(route)
                .setType(type)
                .setTimedBookMark(bookMark)
                .build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);
        FadeInAnimator fadeInAnimator = new FadeInAnimator();
        fadeInAnimator.setAddDuration(1000);
        fadeInAnimator.setChangeDuration(1000);
        fadeInAnimator.setMoveDuration(1000);
        recyclerView.setItemAnimator(fadeInAnimator);
        recyclerView.setAdapter(infoDetailAdapter);

    }

    private void setToolbar() {
        //setting Toolbar
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        //Combined Toolbar into CollapsingToolbar Layout
        collapsingToolbarLayout = (CollapsingToolbarLayout)view.findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        //title = (TextView) view.findViewById(R.id.traffic_info_title);
        //  txtSubtitle = (TextView) view.findViewById(R.id.txtsubTitle);
       /*
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(InfoDetailFragment.this.getContext(), "Show in Google Map ", Toast.LENGTH_SHORT).show();
                Uri gmmIntentUrl = Uri.parse("geo:" + route.getLatLngs()[0] + "," + route.getLatLngs()[1] + "?z=19");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUrl);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });*/

        if (languageSelector.getLanguage().equals(MyApplication.Language.ZH_HANT)) {
            collapsingToolbarLayout.setTitle(route.getDescription()[1]);
        //    title.setText(route.getDescription()[1]);
            //   txtSubtitle.setText(route.getRegion()[1]);
        } else {
            collapsingToolbarLayout.setTitle(route.getDescription()[0]);
        //    title.setText(route.getDescription()[0]);
        //    txtSubtitle.setText(route.getRegion()[0]);
        }
    }

    private void setImageHeader() {
        String url = "";
        //Compare Two String
        if (route.getType().equals(RouteCCTV.TYPE_CCTV)) {
            url = TRAFFIC_URL + route.getRef_key() + JPG;
            Log.d(TAG, url);
        } else {
            url = TRAFFIC_SPEED_MAP + route.getRef_key() +
                    (languageSelector.getLanguage().equals(MyApplication.Language.ENGLISH) ? EN : TC) + PNG;
            Log.d(TAG, "Else: " + url);
        }
        imageLoader.displayImage(url, imageRoute, displayImageOptions);

    }

    /**
     * Getting Data From Previous Fragment {{@link NavTrafficMonitorFragment}}
     */
    private void getDataFromIntent() {
        intent = this.getArguments();
        String intent_type = intent.getString("type");
        if (intent_type != null) {
            switch (intent_type) {
                case Tab_BookMarkFragment.TYPE_EDIT_BOOKMARK:
                    type = intent_type;
                    Log.d(TAG, "" + intent.getInt("key"));
                    bookMark = sqLiteHelper.getBookmark(intent.getInt("key"));
                    if (bookMark != null) {
                        route = new RouteCCTV.Builder()
                                .setId(bookMark.get_id())
                                .setDescription(bookMark.getBkRouteName())
                                .setRegion(bookMark.getRegions())
                                .setKey(bookMark.getRouteImageKey())
                                .setLatLngs(bookMark.getLatLngs())
                                .setType(bookMark.getType())
                                .build();
                    }
                    break;
                case VIEW_HISTORY_RECORD:
                    type = intent_type;
                    bookMark = sqLiteHelper.getBookmark(intent.getInt("key"));
                    if (bookMark != null) {
                        route = new RouteCCTV.Builder()
                                .setId(bookMark.get_id())
                                .setDescription(bookMark.getBkRouteName())
                                .setRegion(bookMark.getRegions())
                                .setKey(bookMark.getRouteImageKey())
                                .setLatLngs(bookMark.getLatLngs())
                                .setType(bookMark.getType())
                                .build();
                    }
                    break;
                default:
                    imageKey = intent.getString(KEY);
                    route = (RouteCCTV) intent.getSerializable("route");
                    break;
            }
        } else {
            Log.e(TAG, "Intent is null Exception....");
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this.getActivity(), REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GoogleApiAvailability.getErrorDialog()
            showErrorDialog(result.getErrorCode());
            mResolvingError = true;
        }
    }

    /* Creates a dialog for an error message */
    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getChildFragmentManager(), "errordialog");
    }

    /* Called from ErrorDialogFragment when the dialog is dismissed. */
    public static void onDialogDismissed() {
        mResolvingError = false;
    }


    @Override
    public void onStart() {
        super.onStart();
        if (!mResolvingError) {  // more about this later
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    /* A fragment to display an error dialog */
    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            onDialogDismissed();
        }
    }

}
