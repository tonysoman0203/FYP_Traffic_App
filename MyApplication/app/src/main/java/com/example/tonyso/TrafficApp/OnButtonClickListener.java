package com.example.tonyso.TrafficApp;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.tonyso.TrafficApp.Interface.RecyclerViewHelper;

/**
 * Created by soman on 2015/12/25.
 */
public class OnButtonClickListener implements View.OnClickListener {
    RecyclerViewHelper recyclerViewHelper;
    int pos;
    RecyclerView.ViewHolder viewHolder;

    public OnButtonClickListener(RecyclerViewHelper recyclerViewHelper, int adapterPosition, RecyclerView.ViewHolder viewHolder) {
        this.recyclerViewHelper  = recyclerViewHelper;
        this.pos = adapterPosition;
        this.viewHolder = viewHolder;

        Log.e(OnButtonClickListener.class.getName(),""+pos);
    }


    @Override
    public void onClick(View v) {
        int tag = (Integer) v.getTag();
        switch (tag){
            case 1:case 2:case 3:
                recyclerViewHelper.onRecyclerViewIndex(tag,pos);
                break;
            case 4:
                recyclerViewHelper.onAddBookmarkClick(viewHolder);
                break;

            default:break;
        }
    }
}
