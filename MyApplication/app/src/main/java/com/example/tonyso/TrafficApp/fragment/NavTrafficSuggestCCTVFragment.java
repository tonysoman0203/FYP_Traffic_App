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
import com.example.tonyso.TrafficApp.adapter.CCTVListAdapter;
import com.example.tonyso.TrafficApp.baseclass.BaseFragment;

import jp.wasabeef.recyclerview.animators.SlideInDownAnimator;

/**
 * Created by soman on 2016/2/13.
 */
public class NavTrafficSuggestCCTVFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private CCTVListAdapter cctvListAdapter;
    private View view;

    public NavTrafficSuggestCCTVFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NavTrafficSuggestCCTVFragment newInstance(String near, int indicatorColor, int dividerColor, int ic_home) {
        NavTrafficSuggestCCTVFragment f = new NavTrafficSuggestCCTVFragment();
        f.setTitle(near);
        f.setIndicatorColor(indicatorColor);
        f.setDividerColor(dividerColor);
        f.setIcon(ic_home);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getInstance();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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
    }

    private void onViewInitialize(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.cctvlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new SlideInDownAnimator());
        recyclerView.setAdapter(new CCTVListAdapter());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


}
