package com.example.tonyso.TrafficApp.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.adapter.TabFragmentPagerAdapter;
import com.example.tonyso.TrafficApp.baseclass.BaseFragment;

import java.util.LinkedList;

public class Tab_MainFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    static TabLayout tabLayout;
    ViewPager viewPager;
    TabFragmentPagerAdapter adapter;
    LinkedList<BaseFragment> fragments;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Tab_MainFragment() {
        // Required empty public constructor
    }

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

    public static Tab_MainFragment newInstance(String title, int indicatorColor, int dividerColor, TabLayout tab) {
        Tab_MainFragment f = new Tab_MainFragment();
        f.setTitle(title);
        f.setIndicatorColor(indicatorColor);
        f.setDividerColor(dividerColor);
        tabLayout = tab;

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        super.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_tab__main, container, false);
        fragments = getFragments();
        adapter = new TabFragmentPagerAdapter(getChildFragmentManager(), fragments);
        viewPager = (ViewPager)view.findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(fragments.get(0).getIcon());
        tabLayout.getTabAt(1).setIcon(fragments.get(1).getIcon());
        tabLayout.getTabAt(2).setIcon(fragments.get(2).getIcon());
        return view;
    }

    private LinkedList<BaseFragment> getFragments() {
        int indicatorColor = this.getResources().getColor(R.color.colorAccent);
        int dividerColor = Color.WHITE;

        int ic_home = R.drawable.ic_home_white_48dp;
        String near = getString(R.string.tab_Home);

        int ic_bookmark = R.drawable.ic_bookmark_white_48dp;
        int ic_history = R.drawable.ic_history_white_48dp;

        LinkedList<BaseFragment> fragments = new LinkedList<>();
        String bookmark = getString(R.string.tab_BookMarks);
        String history = getString(R.string.tab_History);
        fragments.add(Tab_Home_Fragment.newInstance(near, indicatorColor, dividerColor, ic_home));
        fragments.add(Tab_BookMarkFragment.newInstance(bookmark, indicatorColor, dividerColor, ic_bookmark));
        fragments.add(Tab_HistoryFragment.newInstance(history, indicatorColor, dividerColor, ic_history));
        return fragments;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

}
