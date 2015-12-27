package com.example.tonyso.TrafficApp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tonyso.TrafficApp.Singleton.LanguageSelector;
import com.example.tonyso.TrafficApp.Singleton.RouteMapping;
import com.example.tonyso.TrafficApp.adapter.InfoDetailAdapter;
import com.example.tonyso.TrafficApp.model.RouteCCTV;

public class TrafficInfoDetailScrollingActivity extends AppCompatActivity {
    //Constant
    public static final String TAG = TrafficInfoDetailScrollingActivity.class.getName();
    public static final String KEY = "key";
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
    RouteMapping routeMapping;
    LanguageSelector languageSelector;

    //Action
    RecyclerView recyclerView;
    private static final int RECYCLER_VIEW_SIZE = 3;
    InfoDetailAdapter infoDetailAdapter;
    CoordinatorLayout coordinatorLayout;

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
        infoDetailAdapter = new InfoDetailAdapter(this,RECYCLER_VIEW_SIZE,coordinatorLayout,route);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
        routeMapping = RouteMapping.getInstance(this);
        languageSelector = LanguageSelector.getInstance(this);
    }
    private void setImageHeader() {
        imageRoute.setImageBitmap(routeImg);

        Palette.from(routeImg).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int mutedColor = palette.getMutedColor(getResources().getColor(R.color.colorPrimary));
                collapsingToolbarLayout.setContentScrimColor(mutedColor);
            }
        });
    }

    /**
     *  Getting Data From Previous Fragment {{@link Nav_TrafficActivity}}
     */
    private void getDataFromIntent(){
        intent = getIntent();
        imageKey = intent.getStringExtra(KEY);
        route = (RouteCCTV) intent.getSerializableExtra(imageKey);
        routeImg = routeMapping.getBitmapFromMemCache(imageKey);
    }

}
