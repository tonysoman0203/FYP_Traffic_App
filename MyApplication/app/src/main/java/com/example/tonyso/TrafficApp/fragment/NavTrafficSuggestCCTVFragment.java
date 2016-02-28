package com.example.tonyso.TrafficApp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.baseclass.BaseFragment;
import com.example.tonyso.TrafficApp.location.FetchDistanceCCTVTask;
import com.example.tonyso.TrafficApp.model.Place;

import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;

/**
 * Created by soman on 2016/2/13.
 */
public class NavTrafficSuggestCCTVFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "Origin";
    private static final String ARG_PARAM2 = "Destination";
    private static final String ARG_PARAM3 = "distance";

    private RecyclerView recyclerView;
    private View view;

    private Place origin;
    private Place destination;
    private String[] distanceAndDuration;

    public NavTrafficSuggestCCTVFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NavTrafficSuggestCCTVFragment newInstance(String near, int indicatorColor, int dividerColor, int ic_home, Place origin, Place destination, String[] arrDisDuration) {
        NavTrafficSuggestCCTVFragment f = new NavTrafficSuggestCCTVFragment();
        f.setTitle(near);
        f.setIndicatorColor(indicatorColor);
        f.setDividerColor(dividerColor);
        f.setIcon(ic_home);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_PARAM1, origin);
        bundle.putSerializable(ARG_PARAM2, destination);
        bundle.putStringArray(ARG_PARAM3, arrDisDuration);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getInstance();
        if (getArguments() != null) {
            origin = (Place) getArguments().getSerializable(ARG_PARAM1);
            destination = (Place) getArguments().getSerializable(ARG_PARAM2);
            distanceAndDuration = getArguments().getStringArray(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_nav_suggest_cctv, container, false);
        } catch (InflateException e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onViewInitialize(view);
        FetchDistanceCCTVTask cctvTask = new FetchDistanceCCTVTask();
        cctvTask.setFragment(this);
        cctvTask.setCctvList(routeList);
        cctvTask.setOrigin(origin);
        cctvTask.setDestination(destination);
        cctvTask.setTotat_distance(Double.parseDouble(distanceAndDuration[0].substring(0, 3)));
        cctvTask.setRecyclerView(recyclerView);
        cctvTask.execute();

    }

    private void onViewInitialize(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.cctvlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new SlideInDownAnimator());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
