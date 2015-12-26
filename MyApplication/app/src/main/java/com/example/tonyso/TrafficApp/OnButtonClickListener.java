package com.example.tonyso.TrafficApp;

import android.util.Log;
import android.view.View;

import com.example.tonyso.TrafficApp.Interface.RecyclerViewHelper;

/**
 * Created by soman on 2015/12/25.
 */
public class OnButtonClickListener implements View.OnClickListener {
    RecyclerViewHelper recyclerViewHelper;
    int pos;

    public OnButtonClickListener(RecyclerViewHelper recyclerViewHelper, int adapterPosition){
        this.recyclerViewHelper  = recyclerViewHelper;
        this.pos = adapterPosition;
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
                recyclerViewHelper.onAddBookmarkClick();
                break;

            default:break;
        }
    }
}
