package com.example.tonyso.TrafficApp.baseclass;

import android.graphics.Color;
import android.support.v4.app.Fragment;

/**
 * Created by TonySo on 1/10/2015.
 */
public class BaseFragment extends Fragment {

    private String title = "";
    private int indicatorColor = Color.BLUE;
    private int dividerColor = Color.GRAY;


    public String getTitle() {
        return title;
    }
    protected void setTitle(String title) {
        this.title = title;
    }
    public int getIndicatorColor() {
        return indicatorColor;
    }
    protected void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
    }
    public int getDividerColor() {
        return dividerColor;
    }
    protected void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
    }


}