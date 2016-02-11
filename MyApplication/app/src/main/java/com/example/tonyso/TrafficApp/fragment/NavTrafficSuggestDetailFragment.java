package com.example.tonyso.TrafficApp.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.adapter.TabFragmentPagerAdapter;
import com.example.tonyso.TrafficApp.baseclass.BaseFragment;

import java.util.LinkedList;

/**
 * Created by TonySo on 11/2/16.
 */
public class NavTrafficSuggestDetailFragment extends DialogFragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     * Constant
     */
    public static final String ARG_SECTION_NUMBER = "section_number";
    public static final Integer ARG_SECTION_NUMBER_SIZE = 2;
    public static final String ARG_ORIGIN_OBJECT = "origin";
    public static final String ARG_DESTINATION_OBJECT = "destination";
    public static final String TAG = NavTrafficSuggestDetailFragment.class.getCanonicalName();
    View rootView;
    private TabFragmentPagerAdapter mSectionsPagerAdapter;
    private LinkedList<BaseFragment> fragments;
    private ViewPager viewPager;


    public NavTrafficSuggestDetailFragment() {

    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static NavTrafficSuggestDetailFragment newInstance(int sectionNumber) {
        NavTrafficSuggestDetailFragment fragment = new NavTrafficSuggestDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AppTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.dialog_fragment_route_suggest_detail, container, false);
        fragments = getFragments();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new TabFragmentPagerAdapter(getChildFragmentManager(), fragments);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        viewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        setupTabLayoutIcon(tabLayout);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        //TabLayout.Tab tab = tabLayout.getTabAt(0);
        //tab.select();

        return rootView;
    }

    private void setupTabLayoutIcon(TabLayout tabLayout) {
        for (int i = 0; i < fragments.size(); i++) {
            View item = getActivity().getLayoutInflater().inflate(R.layout.item_tab_icon, null);
            ImageView imageView = (ImageView) item.findViewById(R.id.imgIcon);
            TextView txt = (TextView) item.findViewById(R.id.txtTitle);
            imageView.setImageResource(fragments.get(i).getIcon());
            txt.setText(fragments.get(i).getTitle());
            tabLayout.getTabAt(i).setCustomView(item);
        }
        tabLayout.getTabAt(0).getCustomView().setSelected(true);
    }

    private LinkedList<BaseFragment> getFragments() {
        int indicatorColor = this.getResources().getColor(R.color.colorAccent);
        int dividerColor = Color.WHITE;

        int[] drawableIcons = new int[]{
                R.drawable.ic_directions_car_white_36dp,
                R.drawable.ic_local_see_white_36dp
        };
        String[] tabs_Title = new String[]{
                getString(R.string.route_suggest_car),
                getString(R.string.route_suggest_cctv)
        };
        LinkedList<BaseFragment> fragments = new LinkedList<>();
        fragments.add(NavSuggestMapFragment.newInstance(tabs_Title[0], indicatorColor, dividerColor, drawableIcons[0]));
        fragments.add(NavSuggestMapFragment.newInstance(tabs_Title[1], indicatorColor, dividerColor, drawableIcons[1]));
        return fragments;
    }

}
