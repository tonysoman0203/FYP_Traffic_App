package com.example.tonyso.TrafficApp.listener;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by soman on 2015/12/25.
 */
public class OnButtonClickListener implements View.OnClickListener {
    RecyclerViewListener recyclerViewListener;
    int pos;
    RecyclerView.ViewHolder viewHolder;

    public OnButtonClickListener(RecyclerViewListener recyclerViewListener, int adapterPosition, RecyclerView.ViewHolder viewHolder) {
        this.recyclerViewListener = recyclerViewListener;
        this.pos = adapterPosition;
        this.viewHolder = viewHolder;
        Log.e(OnButtonClickListener.class.getName(),""+pos);
    }


    @Override
    public void onClick(View v) {
        int tag = (Integer) v.getTag();
        switch (tag){
            case 1:case 2:case 3:
                recyclerViewListener.onRecyclerViewIndex(tag, pos);
                break;
            case 4:
                recyclerViewListener.onAddBookmarkClick(viewHolder);
                break;
            default:break;
        }
    }
}
