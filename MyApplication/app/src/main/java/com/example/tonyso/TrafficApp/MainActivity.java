package com.example.tonyso.TrafficApp;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.tonyso.TrafficApp.Singleton.LanguageSelector;
import com.example.tonyso.TrafficApp.Singleton.RssReader;
import com.example.tonyso.TrafficApp.baseclass.BaseActivity;
import com.example.tonyso.TrafficApp.listener.WeatherRefreshListener;
import com.example.tonyso.TrafficApp.location.GPSLocationFinder;
import com.example.tonyso.TrafficApp.utility.CommonUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, WeatherRefreshListener {

    LanguageSelector languageSelector;

    private Toolbar toolbar;
//    private RecyclerView.LayoutManager layoutManager;

    private RssReader rssReader;
    public Handler rss_Handler;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    public static boolean mResolvingError = false;
    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;
    private static int REQUEST_RESOLVE_ERROR = 1001;
    private static final String DIALOG_ERROR = "dialog_error";
    private static final int UPDATE_INTERVAL = 10000; // 10 sec
    private static final int FATEST_INTERVAL = 60000; // 5 sec
    private static final int DISPLACEMENT = 5; // 10 meters
    private String TAG = getClass().getSimpleName();

    private ActionBarDrawerToggle toggle;
    private GPSLocationFinder gpsLocationFinder;

    Locale ZH_HANT = Locale.TRADITIONAL_CHINESE;
    Locale ENG = Locale.ENGLISH;

    private DrawerLayout drawer;
    private FloatingActionButton fab;
    private NavigationView navigationView;

    BroadcastReceiver broadcastReceiver;

    Intent broadCastTimerIntent;
    CoordinatorLayout coordinatorLayout;
    //TabLayout tabLayout;

    TextView txtCurrDate, txtCurrTime, lbllocation, lblWeather, lblDate, lblTime;

    TabLayout tabLayout;
    View headerLayout;
    ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            init();
            initToolBar();
            initNavigationDrawer();
            LabelFindViewById();
            initRSSReader();

        FragmentManager fragmentManager = getSupportFragmentManager();
        int indicatorColor = this.getResources().getColor(R.color.colorAccent);
        int dividerColor = Color.WHITE;

        fragmentManager.beginTransaction().replace(R.id.flcontent,
                Tab_MainFragment.newInstance(getString(R.string.app_name),
                        indicatorColor, dividerColor, tabLayout)).commit();

    }

    private void init() {
        imageLoader = ImageLoader.getInstance();
        tabLayout = (TabLayout)findViewById(R.id.tablayout);
        broadcastReceiver = myBroadCastReceiver;
        languageSelector = LanguageSelector.getInstance(this);
        rss_Handler = new Handler();
        gpsLocationFinder = new GPSLocationFinder(this,this);
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinateLayoutMain);

        if (CommonUtils.checkPlayServices(this, PLAY_SERVICES_RESOLUTION_REQUEST)) {
            buildGoogleApiClient();
            createLocationRequest();
        }
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //initTabLayoutViewPager();
    }


    private void initToolBar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.app_name));

    }

    private void initNavigationDrawer(){
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) drawer.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);

    }


    private BroadcastReceiver myBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String currtime = intent.getStringExtra(CountDownService.MESSAGE);
            txtCurrTime.setText(currtime);
        }
    };

    private void setUpCountDownService(){
        broadCastTimerIntent = new Intent(this,CountDownService.class);
        startService(broadCastTimerIntent);
    }

    private void LabelFindViewById(){
        lbllocation = (TextView)headerLayout.findViewById(R.id.lblCurrLocation);
        lblWeather = (TextView)headerLayout.findViewById(R.id.lblTemp);
        lblDate = (TextView)headerLayout.findViewById(R.id.lblDate);
        lblTime = (TextView)headerLayout.findViewById(R.id.lblCurrTime);
        txtCurrDate = (TextView)headerLayout.findViewById(R.id.txtDate);
        txtCurrTime = (TextView)headerLayout.findViewById(R.id.txtCurrTime);
        lblTime.setText(getString(R.string.rss_CurrTime));
    }

    private void initRSSReader(){
        rssReader = new RssReader(this, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(MainActivity.class.getName(), "OnResume------@");
        CommonUtils.checkPlayServices(this, PLAY_SERVICES_RESOLUTION_REQUEST);

        if (languageSelector.getLanguage().equals(MyApplication.Language.ZH_HANT)) {
            txtCurrDate.setText(CommonUtils.initDate(ZH_HANT));
        }else{
            txtCurrDate.setText(CommonUtils.initDate(ENG));
        }

        rssReader.FeedRss();

        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }

        setUpCountDownService();
        //setImageDownloadService();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((broadcastReceiver),
                new IntentFilter(CountDownService.CURR_TIME_RESULT)
        );
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(broadCastTimerIntent);
        //stopService(ImageDownloadService);
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onStop();
    }

    private void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, gpsLocationFinder);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, gpsLocationFinder);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (toggle.isDrawerIndicatorEnabled() &&
                toggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();
        switch (id){
            case R.id.action_search:
                Snackbar.make(coordinatorLayout,"Search button click"
                        ,Snackbar.LENGTH_LONG).show();
                break;
            case R.id.action_settings:
                Snackbar.make(coordinatorLayout,"Setting button click"
                        ,Snackbar.LENGTH_LONG).show();
                break;
            // Handle home button in non-drawer mode
            case android.R.id.home:
                getSupportFragmentManager().popBackStackImmediate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass;
        Intent intent;
        if (id == R.id.nav_home) {
            tabLayout.setVisibility(View.VISIBLE);
            fab.setVisibility(View.VISIBLE);
            fragmentClass = Tab_MainFragment.class;
            try{
                fragment = (Fragment) fragmentClass.newInstance();

            }catch (Exception e){
                e.printStackTrace();
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flcontent, fragment).commit();
            item.setChecked(true);
            setTitle(getString(R.string.app_name));
            //fragmentManager.beginTransaction().replace(R.id.flcontent,tab_mainFragment ).commit();
        } else if (id == R.id.nav_traffic) {
            intent = new Intent(this,Nav_TrafficActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_suggestion) {

        } else if (id == R.id.nav_setting) {
            intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_feedback) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            Log.d(TAG, String.valueOf(mLastLocation.getLatitude()));
            Log.d(TAG, String.valueOf(mLastLocation.getLongitude()));
            Log.d(TAG, "Speed: " + mLastLocation.getSpeed());
            Log.d(TAG + "getAccuracy: ", String.valueOf(mLastLocation.getAccuracy()));
            if (languageSelector.getLanguage().equals(MyApplication.Language.ENGLISH)) {
                gpsLocationFinder.convertLatLongToAddress(mLastLocation,ENG);
            }else{
                gpsLocationFinder.convertLatLongToAddress(mLastLocation,ZH_HANT);
            }
        }

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
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
    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "errordialog");

    }

    public static void onDialogDismissed() {
        mResolvingError = false;
    }

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
    public void onRefreshWeather(String s) {
        TextView textView = (TextView) headerLayout.findViewById(R.id.txtTemperature);
        textView.setText(s);
    }

    @Override
    public void onRefreshIcon(String URL) {
        CircleImageView imageView = (CircleImageView) headerLayout.findViewById(R.id.bgHeader);
        imageLoader.displayImage(URL, imageView);
    }

    @Override
    public void onRefreshLocation(String address) {
        TextView textView = (TextView) navigationView.findViewById(R.id.txtLocation);
        textView.setText(address);
    }




}
