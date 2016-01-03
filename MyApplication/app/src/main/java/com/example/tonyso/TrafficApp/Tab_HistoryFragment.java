package com.example.tonyso.TrafficApp;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tonyso.TrafficApp.Singleton.SQLiteHelper;
import com.example.tonyso.TrafficApp.adapter.HistoryAdapter;
import com.example.tonyso.TrafficApp.baseclass.BaseFragment;
import com.example.tonyso.TrafficApp.model.TimedBookMark;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import jp.wasabeef.recyclerview.animators.FadeInRightAnimator;

public class Tab_HistoryFragment extends BaseFragment implements Observer {

    private static final String TAG = Tab_HistoryFragment.class.getSimpleName();
    private RecyclerView mRecyclerHistoryView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<TimedBookMark> historyList = new ArrayList<>();
    private HistoryAdapter historyAdapter;
    private View view;
    private TextView emptyView;

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
        // TODO: Change Adapter to display your content
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
        if (historyList.size() <= 0) {
            setEmptyText("There is no History....");
        } else {
            emptyView.setVisibility(View.GONE);
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
        registerForContextMenu(mRecyclerHistoryView);
    }

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(getView(), "I am Refreshing...Wait for while...", Snackbar.LENGTH_LONG).show();
                }
            }, 1000);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    };

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
        SQLiteHelper sqLiteHelper = new SQLiteHelper(this.getContext());
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
        if (emptyView != null) {
            emptyView.setText(emptyText);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = -1;
        try {
            position = historyAdapter.getPosition();
        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }
        switch (item.getItemId()) {
            case R.id.bookmark_delete:
                // do your stuff
                Log.e(TAG, item.getTitle() + "Click in Context Menu");
                if (historyAdapter.removeSelectedItem(position) > 0) {
                    break;
                } else
                    setHistoryAdapter();
                break;
            case R.id.bookmark_share:
                // do your stuff
                Log.e(TAG, item.getTitle() + "Click in Context Menu");
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void update(Observable observable, Object data) {
        setHistoryAdapter();
    }


}
