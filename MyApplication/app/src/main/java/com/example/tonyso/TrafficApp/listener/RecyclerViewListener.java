package com.example.tonyso.TrafficApp.listener;

import android.support.v7.widget.RecyclerView;

/**
 * Created by soman on 2015/12/25.
 */
public interface RecyclerViewListener {
    void onRecyclerViewIndex(int tag,int pos);
    void onAddBookmarkClick(RecyclerView.ViewHolder viewHolder);

    void onResetTimerClick(RecyclerView.ViewHolder viewHolder);
}
