package com.example.tonyso.TrafficApp.listener;

import android.view.View;

/* Interface for handling clicks - both normal and long ones. */
public interface OnItemClickListener {
    /**
     * Called when the view is clicked.
     *
     * @param v        view that is clicked
     * @param position of the clicked item
     */
    void onClick(View v, int position);
}