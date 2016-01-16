package com.example.tonyso.TrafficApp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tonyso.TrafficApp.adapter.InfoDetailAdapter;
import com.example.tonyso.TrafficApp.fragment.Nav_TrafficFragment;
import com.example.tonyso.TrafficApp.fragment.Tab_BookMarkFragment;
import com.example.tonyso.TrafficApp.fragment.Tab_HistoryFragment;
import com.example.tonyso.TrafficApp.model.RouteCCTV;
import com.example.tonyso.TrafficApp.model.TimedBookMark;
import com.example.tonyso.TrafficApp.utility.LanguageSelector;
import com.example.tonyso.TrafficApp.utility.SQLiteHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.places.Places;
import com.nostra13.universalimageloader.core.ImageLoader;

import jp.wasabeef.recyclerview.animators.FadeInAnimator;

//import android.support.v7.graphics.Palette;

public class InfoDetailActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, OnConnectionFailedListener {
    //Constant
    public static final String TAG = InfoDetailActivity.class.getName();
    public static final String KEY = "key";
    public static final String ADD_ROUTE_TYPE = "Add_ROUTE";
    public static final String VIEW_HISTORY_RECORD = "View History";

    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    private static final int RECYCLER_VIEW_SIZE = 3;
    private static final String STATE_RESOLVING_ERROR = "resolving_error";
    public CoordinatorLayout coordinatorLayout;
    //UI Components.....
    Toolbar toolbar;
    FloatingActionButton fab;
    CollapsingToolbarLayout collapsingToolbarLayout;
    //Variable
    ImageView imageRoute;
    TextView txtSubtitle,title;
    String imageKey;
    Intent intent;
    RouteCCTV route;
    Bitmap routeImg =null;
    //Instance
    LanguageSelector languageSelector;
    //Action
    RecyclerView recyclerView;
    InfoDetailAdapter infoDetailAdapter;
    SQLiteHelper sqLiteHelper;
    TimedBookMark bookMark;
    String type = "Add_ROUTE";

    GoogleApiClient mGoogleApiClient;
    FragmentManager fm;
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_RESOLVING_ERROR, mResolvingError);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fm = getSupportFragmentManager();
        setContentView(R.layout.activity_traffic_info_detail_scrolling);
        mResolvingError = savedInstanceState != null
                && savedInstanceState.getBoolean(STATE_RESOLVING_ERROR, false);
        buildGoogleApiClient();
        getInstance();
        getDataFromIntent();
        initLayoutComponents();
        setImageHeader();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    private void initLayoutComponents(){
        setToolbar();
        //init ImageView
        imageRoute = (ImageView)findViewById(R.id.header);
        recyclerView = (RecyclerView)findViewById(R.id.content_traffic).findViewById(R.id.recyclerview);
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinate_layout);
        infoDetailAdapter = new InfoDetailAdapter()
                .setGoogleApiClient(mGoogleApiClient)
                .setContext(this)
                .setSize(RECYCLER_VIEW_SIZE)
                .setCoordinatorLayout(coordinatorLayout)
                .setRoute(route)
                .setType(type)
                .setTimedBookMark(bookMark)
                .build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        FadeInAnimator fadeInAnimator = new FadeInAnimator();
        fadeInAnimator.setAddDuration(1000);
        fadeInAnimator.setChangeDuration(1000);
        fadeInAnimator.setMoveDuration(1000);
        recyclerView.setItemAnimator(fadeInAnimator);
        recyclerView.setAdapter(infoDetailAdapter);
    }

    private void setToolbar(){
        //setting Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Combined Toolbar into CollapsingToolbar Layout
        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        title = (TextView)findViewById(R.id.traffic_info_title);
        txtSubtitle = (TextView)findViewById(R.id.traffic_info_subtitle);

        if (languageSelector.getLanguage().equals(MyApplication.Language.ZH_HANT)) {
            collapsingToolbarLayout.setTitle(route.getDescription()[1]);
            title.setText(route.getDescription()[1]);
            txtSubtitle.setText(route.getRegion()[1]);
        } else {
            collapsingToolbarLayout.setTitle(route.getDescription()[0]);
            title.setText(route.getDescription()[0]);
            txtSubtitle.setText(route.getRegion()[0]);
        }
    }

    private void getInstance(){
        //Getting Instance and Cache
        languageSelector = LanguageSelector.getInstance(this);
        sqLiteHelper = new SQLiteHelper(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        int count = fm.getBackStackEntryCount();
        Log.e(TAG, "On Back Stack Count:+ " + TAG + count);
        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            fm.popBackStack();
        }
    }

    private void setImageHeader() {
            ImageLoader.getInstance().displayImage("http://tdcctv.data.one.gov.hk/" + route.getRef_key() + ".JPG", imageRoute);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *  Getting Data From Previous Fragment {{@link Nav_TrafficFragment}}
     */
    private void getDataFromIntent(){
        intent = getIntent();
        String intent_type = intent.getStringExtra("type");
        if (intent_type != null) {
            switch (intent_type) {
                case Tab_BookMarkFragment.TYPE_EDIT_BOOKMARK:
                    type = intent_type;
                    bookMark = sqLiteHelper.getBookmark(intent.getIntExtra(SQLiteHelper.getKeyId(), -1));
                    if (bookMark != null) {
                        route = new RouteCCTV.Builder()
                                .setId(bookMark.get_id())
                                .setDescription(bookMark.getBkRouteName())
                                .setRegion(bookMark.getRegions())
                                .setKey(bookMark.getRouteImageKey())
                                .setLatLngs(bookMark.getLatLngs())
                                .build();
                    }
                    break;
                case VIEW_HISTORY_RECORD:
                    type = intent_type;
                    bookMark = sqLiteHelper.getBookmark(intent.getIntExtra(Tab_HistoryFragment.INTENT_TAG_HISTORY_ITEM, -1));
                    if (bookMark != null) {
                        route = new RouteCCTV.Builder()
                                .setId(bookMark.get_id())
                                .setDescription(bookMark.getBkRouteName())
                                .setRegion(bookMark.getRegions())
                                .setKey(bookMark.getRouteImageKey())
                                .setLatLngs(bookMark.getLatLngs())
                                .build();
                    }
                    break;
                default:
                    imageKey = intent.getStringExtra(KEY);
                    route = (RouteCCTV) intent.getSerializableExtra(imageKey);
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
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
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
        dialogFragment.show(getSupportFragmentManager(), "errordialog");
    }

    /* Called from ErrorDialogFragment when the dialog is dismissed. */
    public void onDialogDismissed() {
        mResolvingError = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RESOLVE_ERROR) {
            mResolvingError = false;
            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mResolvingError) {  // more about this later
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
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
            ((InfoDetailActivity) getActivity()).onDialogDismissed();
        }
    }

}
