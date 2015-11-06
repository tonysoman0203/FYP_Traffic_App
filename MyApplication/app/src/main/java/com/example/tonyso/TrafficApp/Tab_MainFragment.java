package com.example.tonyso.TrafficApp;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tonyso.TrafficApp.adapter.TabFragmentPagerAdapter;
import com.example.tonyso.TrafficApp.baseclass.BaseFragment;

import java.util.LinkedList;

public class Tab_MainFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static TabLayout tabLayout;
    ViewPager viewPager;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab_MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Tab_MainFragment newInstance(String param1, String param2) {
        Tab_MainFragment fragment = new Tab_MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static Tab_MainFragment newInstance(String title,int indicatorColor,int dividerColor,TabLayout tabLayout){
        Tab_MainFragment f = new Tab_MainFragment();
        f.setTitle(title);
        f.setIndicatorColor(indicatorColor);
        f.setDividerColor(dividerColor);
        Tab_MainFragment.tabLayout = tabLayout;

        return f;
    }

    public Tab_MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab__main, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTabLayoutViewPager(view);

    }

    //    Tempartory Coding:
    private void initTabLayoutViewPager(View v){
        final LinkedList<BaseFragment> fragments = getFragments();
        TabFragmentPagerAdapter adapter = new TabFragmentPagerAdapter(getActivity().getSupportFragmentManager(), fragments);
        viewPager = (ViewPager)v.findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
        tabLayout.setTabsFromPagerAdapter(adapter);
        for (int i = 0;i<fragments.size();i++)
        tabLayout.getTabAt(0).setIcon(fragments.get(0).getIcon());

    }

    private LinkedList<BaseFragment> getFragments(){
        int indicatorColor = this.getResources().getColor(R.color.colorAccent);
        int dividerColor = Color.WHITE;

        int ic_home = R.drawable.ic_home_black_48dp;
        int ic_bookmark = (R.drawable.ic_bookmark);
        int ic_history = R.drawable.ic_history_black_48dp;

        LinkedList<BaseFragment> fragments = new LinkedList<>();
        String home = getString(R.string.tab_Home);
        String bookmark = getString(R.string.tab_BookMarks);
        String history = getString(R.string.tab_History);
        fragments.add(Tab_HomeFragment.newInstance(home, indicatorColor, dividerColor,ic_home));
        fragments.add(Tab_BookMarkFragment.newInstance(bookmark, indicatorColor, dividerColor,ic_bookmark));
        fragments.add(Tab_HistoryFragment.newInstance(history, indicatorColor, dividerColor,ic_history));
        return fragments;
    }

}
