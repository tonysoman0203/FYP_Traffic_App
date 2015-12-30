package com.example.tonyso.TrafficApp;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tonyso.TrafficApp.Interface.OnRemainingTimeListener;
import com.example.tonyso.TrafficApp.Singleton.SQLiteHelper;
import com.example.tonyso.TrafficApp.adapter.BookMarkAdapter;
import com.example.tonyso.TrafficApp.baseclass.BaseFragment;
import com.example.tonyso.TrafficApp.model.TimedBookMark;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.FadeInRightAnimator;


/**
 * A simple {@link Fragment} subclass.
 */
public class Tab_BookMarkFragment extends BaseFragment
        implements BookMarkAdapter.OnItemClickListener {

    private static final String TAG = Tab_BookMarkFragment.class.getName();
    RecyclerView recyclerView;
    BookMarkAdapter bookMarkAdapter;
    static SQLiteHelper sqLiteHelper ;
    TextView msg;
    ArrayList<TimedBookMark> bookmarklist;
    SwipeRefreshLayout mSwipeRefreshLayout;
    BroadcastReceiver broadcastReceiver;
    Intent bookmarkService;
    OnRemainingTimeListener onRemainingTimeListener;

    // Intent for Counting Remaining Time: Tag
    public static final String LIST = "list";

    boolean isServiceRunning = false;

    //Request Code for Activity Result
    public static final int EDIT_BOOKMARK_REQUEST_CODE = 2000;
    public static final int EDIT_BOOKMARK_RESULT_CODE = 2001;
    public static final String TYPE_EDIT_BOOKMARK = "Edit_bookmark_action";

    private ActionModeCallback actionModeCallback = new ActionModeCallback();
    private ActionMode actionMode;

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
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);
        mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        sqLiteHelper = new SQLiteHelper(this.getContext());
        recyclerView = (RecyclerView)v.findViewById(R.id.tab_bookmark_recyclerlist);
        recyclerView.setItemAnimator(new FadeInRightAnimator());

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        msg= (TextView)v.findViewById(R.id.txtBookMarkMsg);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e(TAG, "Receive Broadcast:CheckSum= " + intent.getIntExtra(BookMarkService.POS, -1));
                if (intent.getSerializableExtra(BookMarkService.MESSAGE) != null) {
                    ArrayList<TimedBookMark> arrayList = (ArrayList<TimedBookMark>) intent.getSerializableExtra(BookMarkService.MESSAGE);
                    sqLiteHelper.onUpdateBookMarkRemainingTime(arrayList);
                    onRemainingTimeListener.onRemainingTimeChanged(arrayList);
                }
            }
        };
        return v;
    }

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshItems();
        }
    };

    private void setDatasets(){
        bookmarklist = getDataSets();
        int size = bookmarklist.size();
        if (size <= 0) {
            recyclerView.setVisibility(View.GONE);
            msg.setVisibility(View.VISIBLE);
            msg.setText("There is no Bookmark...");
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            msg.setVisibility(View.GONE);
            setBookMarkAdapter(bookmarklist);
            onRemainingTimeListener = bookMarkAdapter;
        }
    }

    private void refreshItems() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setDatasets();
                getActivity().stopService(bookmarkService);
                getActivity().startService(bookmarkService);
            }
        }, 1000);
        mSwipeRefreshLayout.setRefreshing(false);
    }


    private void setBookMarkAdapter(ArrayList<TimedBookMark> timedBookMarks) {
        bookMarkAdapter = new BookMarkAdapter(timedBookMarks, this);
        recyclerView.setAdapter(bookMarkAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "OnResume@" + TAG);

        setDatasets();
            bookmarkService = new Intent(getActivity(), BookMarkService.class);
            bookmarkService.putExtra(LIST, bookmarklist);
            getActivity().startService(bookmarkService);

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
//        if (bookmarkService != null)
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_BOOKMARK_REQUEST_CODE) {
            if (resultCode == EDIT_BOOKMARK_RESULT_CODE) {
                refreshItems();
            }
        }
    }

    @Override
    public void onClick(View v, int position, boolean isLongClick) {
        if (isLongClick) {
            if (actionMode == null) {
                actionMode = getActivity().startActionMode(actionModeCallback);
            }
            toggleSelection(position);
        } else {
            Intent editIntent = new Intent(getActivity(), InfoDetailActivity.class);
            editIntent.putExtra(SQLiteHelper.getKeyId(), bookmarklist.get(position).get_id());
            editIntent.putExtra("type", TYPE_EDIT_BOOKMARK);
            startActivityForResult(editIntent, Tab_BookMarkFragment.EDIT_BOOKMARK_REQUEST_CODE);
        }
    }

    private void toggleSelection(int position) {
        bookMarkAdapter.toggleSelection(position);
        int count = bookMarkAdapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }
    }

    private class ActionModeCallback implements ActionMode.Callback {
        @SuppressWarnings("unused")
        private final String TAG = ActionModeCallback.class.getSimpleName();

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.bookmark_menu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.bookmark_delete:
                    // TODO: actually remove items
                    Log.d(TAG, "menu_remove");
                    if (bookMarkAdapter.getSelectedItemCount() == 1) {
                        Log.e(TAG, "Bookmark Selected Count=" + bookMarkAdapter.getSelectedItemCount());
                        Log.e(TAG, "Selected Item index at " + bookMarkAdapter.getSelectedItems().get(0));
                        bookMarkAdapter.removeSelectedItem(bookMarkAdapter.getSelectedItems().get(0));
                        performRefresh();
                    }
                    mode.finish();
                    return true;

                default:
                    return false;
            }
        }

        private void performRefresh() {
            setDatasets();
            getActivity().stopService(bookmarkService);
            bookmarkService = new Intent(getActivity(), BookMarkService.class);
            bookmarkService.putExtra(LIST, bookmarklist);
            getActivity().startService(bookmarkService);
            Snackbar.make(getActivity().findViewById(R.id.coordinateLayoutMain), "Delete Bookmark(s) success", Snackbar.LENGTH_SHORT).show();

        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            bookMarkAdapter.clearSelection();
            actionMode = null;
        }
    }

}
