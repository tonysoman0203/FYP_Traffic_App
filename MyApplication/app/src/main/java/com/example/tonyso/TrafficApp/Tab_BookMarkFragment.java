package com.example.tonyso.TrafficApp;


import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tonyso.TrafficApp.Singleton.SQLiteHelper;
import com.example.tonyso.TrafficApp.adapter.BookMarkAdapter;
import com.example.tonyso.TrafficApp.baseclass.BaseFragment;
import com.example.tonyso.TrafficApp.model.TimedBookMark;

import org.w3c.dom.Text;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Tab_BookMarkFragment extends BaseFragment {

    private static final String TAG = Tab_BookMarkFragment.class.getName();
    RecyclerView recyclerView;
    public static BookMarkAdapter bookMarkAdapter;
    static SQLiteHelper sqLiteHelper ;
    TextView msg;
    public static List<TimedBookMark>bookmarklist;
    SwipeRefreshLayout mSwipeRefreshLayout;

    public Tab_BookMarkFragment() {
        // Required empty public constructor
    }

    public static Tab_BookMarkFragment newInstance(String title,int indicatorColor,int dividerColor,int icon){
        Tab_BookMarkFragment f = new Tab_BookMarkFragment();
        f.setTitle(title);
        f.setIndicatorColor(indicatorColor);
        f.setDividerColor(dividerColor);
        f.setIcon(icon);
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_bookmark,container,false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshBookMarklist);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });
        sqLiteHelper = new SQLiteHelper(this.getContext());
        recyclerView = (RecyclerView)v.findViewById(R.id.tab_bookmark_recyclerlist);
        msg= (TextView)v.findViewById(R.id.txtBookMarkMsg);
        setDatasets();
        return v;
    }

    private void setDatasets(){
        bookmarklist = getDataSets();
        if (getDataSets()==null){
            recyclerView.setVisibility(View.GONE);
            msg.setText("There is no Bookmark...");
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            msg.setVisibility(View.GONE);
            bookMarkAdapter = new BookMarkAdapter(bookmarklist,this.getContext());
        }
    }

    private void refreshItems() {
        // Load items
        bookmarklist = getDataSets();
        // Load complete
        onItemsLoadComplete();
    }

    private void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        bookmarklist = getDataSets();
        bookMarkAdapter = new BookMarkAdapter(bookmarklist,this.getContext());
        recyclerView.setAdapter(bookMarkAdapter);
        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupItemList();;
    }

    private void setupItemList() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(bookMarkAdapter);
        //setupDividerItemDecoration(mRecyclerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "OnResume:" + TAG);

        bookmarklist = getDataSets();
        bookMarkAdapter = new BookMarkAdapter(bookmarklist,this.getContext());
        recyclerView.setAdapter(bookMarkAdapter);
    }

    public static List<TimedBookMark> getDataSets() {

        if (sqLiteHelper.getBookmarksList().size()>0)
            return sqLiteHelper.getBookmarksList();
        else
            return null;
    }


}
