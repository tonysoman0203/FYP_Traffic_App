package com.example.tonyso.TrafficApp.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.adapter.HistoryAdapter;
import com.example.tonyso.TrafficApp.baseclass.BaseFragment;
import com.example.tonyso.TrafficApp.listener.OnItemClickListener;
import com.example.tonyso.TrafficApp.listener.StatusObserver;
import com.example.tonyso.TrafficApp.model.TimedBookMark;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import jp.wasabeef.recyclerview.animators.FadeInRightAnimator;

public class Tab_HistoryFragment extends BaseFragment implements Observer, OnItemClickListener {

    public static final String INTENT_TAG_HISTORY_ITEM = "History Object";
    private static final String TAG = Tab_HistoryFragment.class.getSimpleName();
    private static final int HISTORY_VIEW_RECORD_REQUEST_CODE = 00001000;
    StatusObserver statusObserver;
    private RecyclerView mRecyclerHistoryView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<TimedBookMark> historyList = new ArrayList<>();
    private HistoryAdapter historyAdapter;
    private TextView emptyView;
    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.e("What the Fuck", "What the fuck");
                    setHistoryAdapter();
                }
            }, 1000);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public Tab_HistoryFragment() {

    }

    public static Tab_HistoryFragment newInstance(String title, int indicatorColor, int dividerColor,int icon){
        Tab_HistoryFragment baseFragment = new Tab_HistoryFragment();
        baseFragment.setTitle(title);
        baseFragment.setDividerColor(dividerColor);
        baseFragment.setIcon(icon);
        baseFragment.setIndicatorColor(indicatorColor);
        return baseFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.getInstance();
        // TODO: Change Adapter to display your content
        myApplication.getTimeStatusObserver().addObserver(this);
        statusObserver = myApplication.getTimeStatusObserver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_history_list, container, false);
        initializeUIView(view);
        setHistoryAdapter();
        return view;
    }

    private void setHistoryAdapter() {
        historyList = getBookMarkHistoryList();
        Log.d(TAG, "history LIst Size = " + historyList.size());
        if (historyList.size() <= 0) {
            setEmptyText("There is no History....");
            mRecyclerHistoryView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            mRecyclerHistoryView.setVisibility(View.VISIBLE);
            historyAdapter = new HistoryAdapter(this, historyList);
            mRecyclerHistoryView.setAdapter(historyAdapter);
        }
    }

    private void initializeUIView(View view) {
        emptyView = (TextView) view.findViewById(R.id.emptyText);
        mRecyclerHistoryView = (RecyclerView) view.findViewById(R.id.historylist);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.SwipeRefresh_History_Layout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.BLACK, Color.BLUE, Color.GREEN, Color.CYAN);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        mRecyclerHistoryView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerHistoryView.setItemAnimator(new FadeInRightAnimator());
        //registerForContextMenu(mRecyclerHistoryView);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * Get History List From SQLiteDatabase
     * if History LIST 's size > 0
     *
     * @return bookkmark History List with no item and size == 0
     */
    private List<TimedBookMark> getBookMarkHistoryList() {

        if (sqLiteHelper.getBookmarkHistory().size() > 0) {
            return sqLiteHelper.getBookmarkHistory();
        } else
            return new ArrayList<>();
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
            emptyView.setText(emptyText);
    }


    @Override
    public void update(Observable observable, Object data) {
        if (statusObserver.isTimeOverChanged()) {
            Log.e(TAG, "Update AR........ ");
            setHistoryAdapter();
            //historyAdapter.notifyItemInserted(historyAdapter.getItemCount()-1);
        }
    }


    @Override
    public void onClick(int position, boolean isLongClick) {
        final Snackbar snackbar = Snackbar.make(getView(), "One Item is Deleted....", Snackbar.LENGTH_SHORT);
        if (isLongClick) {
            if (historyAdapter.removeSelectedItem(position) > 0) {
                snackbar.setAction("Dismiss...", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            } else {
                snackbar.setAction("Dismiss...", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
                setHistoryAdapter();
            }
        } else {
            Intent intent = new Intent(getContext(), InfoDetailFragment.class);
            intent.putExtra(INTENT_TAG_HISTORY_ITEM, historyAdapter.getmSortedList().get(position).get_id());
            intent.putExtra("type", InfoDetailFragment.VIEW_HISTORY_RECORD);
            startActivityForResult(intent, HISTORY_VIEW_RECORD_REQUEST_CODE);
        }
    }
}
