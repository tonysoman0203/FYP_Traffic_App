package com.example.tonyso.TrafficApp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tonyso.TrafficApp.adapter.BookMarkAdapter;
import com.example.tonyso.TrafficApp.baseclass.BaseFragment;
import com.example.tonyso.TrafficApp.model.TimedBookMark;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Tab_BookMarkFragment extends BaseFragment {

    RecyclerView recyclerView;
    BookMarkAdapter bookMarkAdapter;

    public Tab_BookMarkFragment() {
        // Required empty public constructor
    }

    public static Tab_BookMarkFragment newInstance(String title,int indicatorColor,int dividerColor){
        Tab_BookMarkFragment f = new Tab_BookMarkFragment();
        f.setTitle(title);
        f.setIndicatorColor(indicatorColor);
        f.setDividerColor(dividerColor);
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tab_bookmark,container,false);
        recyclerView = (RecyclerView)v.findViewById(R.id.tab_bookmark_recyclerlist);
        bookMarkAdapter = new BookMarkAdapter(getDataSets(),this);
        return v;
    }

    private List<TimedBookMark> getDataSets() {
        List<TimedBookMark> bookMarkSortedList = new ArrayList<>();
        bookMarkSortedList.add(new TimedBookMark(0,"香港仔隧道灣仔入口",
                new Timestamp(Calendar.getInstance().getTimeInMillis()),"H210F","香港島"));

        bookMarkSortedList.add(new TimedBookMark(1,"香港仔隧道香港仔入口",
                new Timestamp(Calendar.getInstance().getTimeInMillis()),"H421F","香港島"));

        bookMarkSortedList.add(new TimedBookMark(2,"漆咸道近公主道",
                new Timestamp(Calendar.getInstance().getTimeInMillis()),"K109F","九龍"));

        bookMarkSortedList.add(new TimedBookMark(3,"海底隧道九龍入口",
                new Timestamp(Calendar.getInstance().getTimeInMillis()),"K107F","九龍"));

        bookMarkSortedList.add(new TimedBookMark(4,"獅子山隧道公路近新田圍邨",
                new Timestamp(Calendar.getInstance().getTimeInMillis()),"ST725F","沙田及馬鞍山"));
        return bookMarkSortedList;
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
}
