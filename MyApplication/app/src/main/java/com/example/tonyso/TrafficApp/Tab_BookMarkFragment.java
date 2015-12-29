package com.example.tonyso.TrafficApp;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tonyso.TrafficApp.Interface.OnRemainingTimeListener;
import com.example.tonyso.TrafficApp.Singleton.SQLiteHelper;
import com.example.tonyso.TrafficApp.adapter.BookMarkAdapter;
import com.example.tonyso.TrafficApp.baseclass.BaseFragment;
import com.example.tonyso.TrafficApp.model.TimedBookMark;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Tab_BookMarkFragment extends BaseFragment {

    private static final String TAG = Tab_BookMarkFragment.class.getName();
    public static final String HELPER = "Helper";
    RecyclerView recyclerView;
    public static BookMarkAdapter bookMarkAdapter;
    static SQLiteHelper sqLiteHelper ;
    TextView msg;
    public static ArrayList<TimedBookMark> bookmarklist;
    SwipeRefreshLayout mSwipeRefreshLayout;
    BroadcastReceiver broadcastReceiver;
    Intent bookmarkService;
    OnRemainingTimeListener onRemainingTimeListener;

    // Intent for Counting Remaining Time: Tag
    public static final String LIST = "list";

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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
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
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e(TAG, "Receive Broadcast: " + intent.getIntExtra(BookMarkService.MESSAGE, -1));
                Log.e(TAG, "Receive Broadcast: " + intent.getIntExtra(BookMarkService.POS, -1));
                int remain = intent.getIntExtra(BookMarkService.MESSAGE, -1);
                int p = intent.getIntExtra(BookMarkService.POS, -1);
                TimedBookMark timedBookMark = bookmarklist.get(p);
                timedBookMark.setRemainTime(remain);
                onRemainingTimeListener.onRemainingTimeChanged(p);
            }
        };
        return v;
    }

    private void setDatasets(){
        if (getDataSets().size() <= 0) {
            recyclerView.setVisibility(View.GONE);
            msg.setText("There is no Bookmark...");
        }else{
            bookmarklist = getDataSets();
            recyclerView.setVisibility(View.VISIBLE);
            msg.setVisibility(View.GONE);
            bookMarkAdapter = new BookMarkAdapter(bookmarklist,this.getContext());
            onRemainingTimeListener = bookMarkAdapter;
            recyclerView.setAdapter(bookMarkAdapter);
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
        setBookMarkAdapter(bookmarklist);
        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void setBookMarkAdapter(ArrayList<TimedBookMark> timedBookMarks) {
        bookMarkAdapter = new BookMarkAdapter(timedBookMarks, this.getContext());
        recyclerView.setAdapter(bookMarkAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "OnResume@" + TAG);

        setDatasets();

        if (getDataSets().size() > 0) {
            bookmarkService = new Intent(getActivity(), BookMarkService.class);
            bookmarkService.putExtra(LIST, bookmarklist);
            getActivity().startService(bookmarkService);
        } else {
            Log.d(TAG, "Service Not Yet Started");
        }


    }

    @Override
    public void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this.getContext()).registerReceiver(broadcastReceiver,
                new IntentFilter(BookMarkService.CURR_TIME_RESULT));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (bookmarkService != null)
            getActivity().stopService(bookmarkService);
    }

    public static ArrayList<TimedBookMark> getDataSets() {
        int size = sqLiteHelper.getBookmarksList().size();
        Log.e(TAG, "SQLITE.LIST.SIZE=" + size);
        if (sqLiteHelper.getBookmarksList().size()>0)
            return sqLiteHelper.getBookmarksList();
        else {
            return new ArrayList<>();
        }
    }


}
