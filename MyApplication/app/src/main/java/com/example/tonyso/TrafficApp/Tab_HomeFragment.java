package com.example.tonyso.TrafficApp;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tonyso.TrafficApp.baseclass.BaseFragment;
import com.example.tonyso.TrafficApp.utility.XMLReader;

public class Tab_HomeFragment extends BaseFragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView textView;
    XMLReader xmlReader;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab_Home_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Tab_HomeFragment newInstance(String param1, String param2) {
        Tab_HomeFragment fragment = new Tab_HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static Tab_HomeFragment newInstance(String title, int indicatorColor, int dividerColor){
        Tab_HomeFragment baseFragment = new Tab_HomeFragment();
        baseFragment.setTitle(title);
        baseFragment.setDividerColor(dividerColor);
        baseFragment.setIndicatorColor(indicatorColor);
        return baseFragment;
    }

    public Tab_HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_tab__home_,container,false);
        textView = (TextView)view.findViewById(R.id.testLabel);
        //xmlReader = new XMLReader(this.getContext(),this);
        //xmlReader.feedImageXml();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


//    @Override
//    public void onXMLFetch(List<RouteCCTV> r) {
//        StringBuffer temp = new StringBuffer();
//        for (int i = 0 ;i<r.size();i++){
//            temp.append(r.get(i).getLatLngs()[0]+" "+r.get(i).getLatLngs()[1]);
//        }
//        textView.setText(temp);
//    }
}
