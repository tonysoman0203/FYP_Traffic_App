package com.example.tonyso.TrafficApp.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.tonyso.TrafficApp.baseclass.TabBaseFragment;

import java.util.LinkedList;

/**
 * Created by TonySo on 1/10/2015.
 */
public class TabFragmentPagerAdapter extends FragmentPagerAdapter {

    LinkedList<TabBaseFragment> fragments = null;

    public TabFragmentPagerAdapter(FragmentManager fm, LinkedList<TabBaseFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public TabBaseFragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).getTitle();
    }


}
