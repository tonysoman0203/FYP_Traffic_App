package com.example.tonyso.TrafficApp.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.adapter.TabFragmentPagerAdapter;
import com.example.tonyso.TrafficApp.baseclass.BaseDialogFragment;
import com.example.tonyso.TrafficApp.baseclass.BaseFragment;
import com.example.tonyso.TrafficApp.model.Place;

import java.util.LinkedList;

/**
 * Created by TonySo on 11/2/16.
 */
public class NavTrafficSuggestDetailFragment extends BaseDialogFragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     * Constant
     */
    public static final String ARG_SECTION_NUMBER = "section_number";
    public static final Integer ARG_SECTION_NUMBER_SIZE = 2;
    public static final String ARG_ORIGIN_OBJECT = "origin";
    public static final String ARG_DESTINATION_OBJECT = "destination";
    public static final String ARG_DISTANCE_AND_DURATION = "distance_duration";
    public static final String TAG = NavTrafficSuggestDetailFragment.class.getCanonicalName();

    View rootView;
    private TabFragmentPagerAdapter mSectionsPagerAdapter;
    private LinkedList<BaseFragment> fragments;
    private ViewPager viewPager;
    private int TABS_SIZE = -1;

    private Place origin, destination;
    private String[] arrDisDuration;

    private TextInputLayout originWrapper, destinationWrapper;
    private EditText originEdt, destinationEdt;
    private Toolbar toolbar;
    private TabLayout tabLayout;

    public NavTrafficSuggestDetailFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static NavTrafficSuggestDetailFragment newInstance(int sectionNumber, Place origin, Place destination, String[] disDur) {
        NavTrafficSuggestDetailFragment fragment = new NavTrafficSuggestDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putSerializable(ARG_ORIGIN_OBJECT, origin);
        args.putSerializable(ARG_DESTINATION_OBJECT, destination);
        args.putStringArray(ARG_DISTANCE_AND_DURATION, disDur);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getInstance();
        getBundleData();
    }

    private void getBundleData() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            TABS_SIZE = bundle.getInt(ARG_SECTION_NUMBER);
            origin = (Place) bundle.getSerializable(ARG_ORIGIN_OBJECT);
            destination = (Place) bundle.getSerializable(ARG_DESTINATION_OBJECT);
            arrDisDuration = bundle.getStringArray(ARG_DISTANCE_AND_DURATION);
        } else {
            throw new NullPointerException("bundle is null");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.dialog_fragment_route_suggest_detail, container, false);
        fragments = getFragments();
        onInitializeView();
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
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        viewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        setupTabLayoutIcon(tabLayout);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

    }

    private void onInitializeView() {
        originWrapper = (TextInputLayout) rootView.findViewById(R.id.originWrapper);
        destinationWrapper = (TextInputLayout) rootView.findViewById(R.id.destinationWrapper);
        originEdt = (EditText) rootView.findViewById(R.id.originEditText);
        destinationEdt = (EditText) rootView.findViewById(R.id.destinationEditText);
        originEdt.setText(origin.getAddress());
        destinationEdt.setText(destination.getAddress());
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        mSectionsPagerAdapter = new TabFragmentPagerAdapter(getChildFragmentManager(), fragments);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tablayout);
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
            fragments.add(NavSuggestMapFragment.newInstance(arrDisDuration[1], indicatorColor, dividerColor, drawableIcons[0], origin, destination));
        } else {
            fragments.add(NavSuggestMapFragment.newInstance(TABS_TITLES[0], indicatorColor, dividerColor, drawableIcons[0], origin, destination));
        }
        fragments.add(NavTrafficSuggestCCTVFragment.newInstance(TABS_TITLES[1], indicatorColor, dividerColor, drawableIcons[1], origin, destination, arrDisDuration));
        return fragments;
    }

}
