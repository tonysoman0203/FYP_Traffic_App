package com.example.tonyso.TrafficApp.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.adapter.TabFragmentPagerAdapter;
import com.example.tonyso.TrafficApp.baseclass.BaseDialogFragment;
import com.example.tonyso.TrafficApp.baseclass.BaseFragment;
import com.example.tonyso.TrafficApp.model.Place;
import com.example.tonyso.TrafficApp.model.RouteCCTV;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NavTrafficSuggestDetailFragment extends BaseDialogFragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     * Constant
     */
    public static final String ARG_ORIGIN_OBJECT = "origin";
    public static final String ARG_DESTINATION_OBJECT = "destination";
    private static final String KET_DISTANCE = "DISTANCE_STRING";
    public static final String TAG = NavTrafficSuggestDetailFragment.class.getCanonicalName();
    private static final String ARG_LIST = "LIST";

    View rootView;
    private LinkedList<BaseFragment> fragments;
    private ViewPager viewPager;

    private Place origin, destination;
    private String[] arrDisDuration;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    //private SwipeRefreshLayout mSwipeRefreshLayout;

    List<Map<String, Object>> list;
    ArrayList<Double[]> paths;
    ArrayList<RouteCCTV> cctvs;

    public NavTrafficSuggestDetailFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static NavTrafficSuggestDetailFragment newInstance(Place origin, Place destination, List<Map<String, Object>> result) {
        NavTrafficSuggestDetailFragment fragment = new NavTrafficSuggestDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ORIGIN_OBJECT, origin);
        args.putSerializable(ARG_DESTINATION_OBJECT, destination);
        args.putSerializable(ARG_LIST, (Serializable) result);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getInstance();
        getBundleData();
        getDataFromResultList();
    }

    private void getBundleData() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            origin = (Place) bundle.getSerializable(ARG_ORIGIN_OBJECT);
            destination = (Place) bundle.getSerializable(ARG_DESTINATION_OBJECT);
            list = (List<Map<String, Object>>) bundle.getSerializable(ARG_LIST);
        } else {
            throw new NullPointerException("bundle is null");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.dialog_fragment_route_suggest_detail, container, false);
        onInitializeView();
        InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        setTabFragmentPagerAdapter();
    }

    private void getDataFromResultList() {
        //Get Distance and Duration
        paths = new ArrayList<>();
        cctvs = new ArrayList<>();
        Log.d(TAG, "Received List and Size = " + list.size());
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).get(KET_DISTANCE) != null) {
                arrDisDuration = (String[]) list.get(i).get(KET_DISTANCE);
            } else if (list.get(i).get(String.valueOf(i)) instanceof Double[]) {
                int index = 0;
                Double[] d;
                while (list.get(i).get(String.valueOf(index)) != null) {
                    d = (Double[]) list.get(i).get(String.valueOf(index));
                    paths.add(d);
                    index++;
                    if (list.get(i).get(String.valueOf(index)) == null &&
                            !(list.get(i).get(String.valueOf(index)) instanceof Double[])) {
                        break;
                    }
                }
            } else {
                int index = 0;
                RouteCCTV r;
                while (list.get(i).get(String.valueOf(index)) != null) {
                    r = (RouteCCTV) list.get(i).get(String.valueOf(index));
                    cctvs.add(r);
                    index++;
                    if (list.get(i).get(String.valueOf(index)) == null &&
                            !(list.get(i).get(String.valueOf(index)) instanceof RouteCCTV)) {
                        break;
                    }
                }
            }
        }
        Log.d(TAG, "" + arrDisDuration[0] + " " + arrDisDuration[1]);
        Log.d(TAG, "" + paths.size());
        Log.d(TAG, "" + cctvs.size());

    }

    private void onInitializeView() {
        TextInputLayout originWrapper = (TextInputLayout) rootView.findViewById(R.id.originWrapper);
        TextInputLayout destinationWrapper = (TextInputLayout) rootView.findViewById(R.id.destinationWrapper);
        EditText originEdt = (EditText) rootView.findViewById(R.id.originEditText);
        EditText destinationEdt = (EditText) rootView.findViewById(R.id.destinationEditText);
        originEdt.setText(origin.getAddress());
        destinationEdt.setText(destination.getAddress());
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tablayout);
        //mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipemap);
        //mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);
    }

    private void setTabFragmentPagerAdapter() {
        fragments = getFragments();
        TabFragmentPagerAdapter mSectionsPagerAdapter = new TabFragmentPagerAdapter(getChildFragmentManager(), fragments);
        viewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        setupTabLayoutIcon(tabLayout);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
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
        final int indicatorColor = this.getResources().getColor(R.color.colorAccent);
        final int dividerColor = Color.WHITE;
        LinkedList<BaseFragment> fragments = new LinkedList<>();
        if (arrDisDuration != null) {
            fragments.add(NavSuggestMapFragment.newInstance(arrDisDuration[1],
                    indicatorColor, dividerColor, drawableIcons[0], origin, destination, arrDisDuration, paths, cctvs));
        } else {
            fragments.add(NavSuggestMapFragment.newInstance(TABS_TITLES[0],
                    indicatorColor, dividerColor, drawableIcons[0], origin, destination, arrDisDuration, paths, cctvs));
        }
        fragments.add(NavTrafficSuggestCCTVFragment.newInstance(TABS_TITLES[1],
                indicatorColor, dividerColor, drawableIcons[1], origin, destination, arrDisDuration, cctvs));
        return fragments;
    }
}
