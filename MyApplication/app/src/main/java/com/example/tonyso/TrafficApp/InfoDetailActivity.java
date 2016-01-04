package com.example.tonyso.TrafficApp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tonyso.TrafficApp.Singleton.LanguageSelector;
import com.example.tonyso.TrafficApp.Singleton.SQLiteHelper;
import com.example.tonyso.TrafficApp.adapter.HistoryAdapter;
import com.example.tonyso.TrafficApp.adapter.InfoDetailAdapter;
import com.example.tonyso.TrafficApp.model.RouteCCTV;
import com.example.tonyso.TrafficApp.model.TimedBookMark;
import com.nostra13.universalimageloader.core.ImageLoader;

import jp.wasabeef.recyclerview.animators.FadeInAnimator;

//import android.support.v7.graphics.Palette;

public class InfoDetailActivity extends AppCompatActivity {
    //Constant
    public static final String TAG = InfoDetailActivity.class.getName();
    public static final String KEY = "key";
    public static final String ADD_ROUTE_TYPE = "Add_ROUTE";
    public static final String VIEW_HISTORY_RECORD = "View History";

    //UI Components.....
    Toolbar toolbar;
    FloatingActionButton fab;
    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView imageRoute;
    TextView txtSubtitle,title;
    //Variable

    String imageKey;
    Intent intent;
    RouteCCTV route;
    Bitmap routeImg =null;

    //Instance
    LanguageSelector languageSelector;

    //Action
    RecyclerView recyclerView;
    private static final int RECYCLER_VIEW_SIZE = 3;
    InfoDetailAdapter infoDetailAdapter;
    public CoordinatorLayout coordinatorLayout;
    SQLiteHelper sqLiteHelper;
    TimedBookMark bookMark;
    String type = "Add_ROUTE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_info_detail_scrolling);
        getInstance();
        getDataFromIntent();
        initLayoutComponents();
        setImageHeader();
    }

    private void initLayoutComponents(){
        setToolbar();
        //init ImageView
        imageRoute = (ImageView)findViewById(R.id.header);
        recyclerView = (RecyclerView)findViewById(R.id.content_traffic).findViewById(R.id.recyclerview);
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinate_layout);
        infoDetailAdapter = new InfoDetailAdapter()
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
        finish();
    }

    private void setImageHeader() {
            ImageLoader.getInstance().displayImage("http://tdcctv.data.one.gov.hk/" + route.getRef_key() + ".JPG", imageRoute);
    }

    /**
     *  Getting Data From Previous Fragment {{@link Nav_TrafficActivity}}
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
                    bookMark = sqLiteHelper.getBookmark(intent.getIntExtra(HistoryAdapter.INTENT_TAG_HISTORY_ITEM, -1));
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

}
