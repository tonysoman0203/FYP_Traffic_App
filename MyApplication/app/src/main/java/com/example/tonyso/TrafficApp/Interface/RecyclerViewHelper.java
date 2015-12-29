package com.example.tonyso.TrafficApp.Interface;

import android.support.v7.widget.RecyclerView;

/**
 * Created by soman on 2015/12/25.
 */
public interface RecyclerViewHelper {
    void onRecyclerViewIndex(int tag,int pos);

    void onAddBookmarkClick(RecyclerView.ViewHolder viewHolder);
}
