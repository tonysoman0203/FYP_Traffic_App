package com.example.tonyso.TrafficApp.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.util.SortedList;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.tonyso.TrafficApp.MainActivity;
import com.example.tonyso.TrafficApp.MyApplication;
import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.baseclass.BaseFragment;
import com.example.tonyso.TrafficApp.location.LatLngConverter;
import com.example.tonyso.TrafficApp.model.RouteCCTV;
import com.example.tonyso.TrafficApp.utility.ShareStorage;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TonySo on 11/1/16.
 */
public class Tab_Home_Fragment extends BaseFragment {

    private static final String TAG = Tab_Home_Fragment.class.getSimpleName();
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    HomeAdapter homeAdapter;
    private static final String TRAFFIC_URL = "http://tdcctv.data.one.gov.hk/";

    public Tab_Home_Fragment() {

    }

    public static Tab_Home_Fragment newInstance(String title, int indicatorColor, int dividerColor, int icon) {
        Tab_Home_Fragment f = new Tab_Home_Fragment();
        f.setTitle(title);
        f.setIndicatorColor(indicatorColor);
        f.setDividerColor(dividerColor);
        f.setIcon(icon);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_home, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.list_near);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.view);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
    }

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            List<RouteCCTV> cctvs = getNearCCTVLocation();
            SortedList<RouteCCTV> sortedList = getSortedList(cctvs);
            homeAdapter = new HomeAdapter(sortedList);
            recyclerView.setAdapter(homeAdapter);
            swipeRefreshLayout.setRefreshing(false);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mOnRefreshListener.onRefresh();
            }
        });
    }

    @Override
    public Context getContext() {
        return super.getContext();
    }

    public List<RouteCCTV> getNearCCTVLocation() {
        List<RouteCCTV> nearCCTVLocation = new ArrayList<>();
        List<RouteCCTV> list = routeList;
        int kmFromSp = ShareStorage.getInteger(MyApplication.KEY_NEAR_IN_KM, ShareStorage.SP.PrivateData, getContext());

        double lat = 0,lng = 0;

        if (MainActivity.getmGoogleApiClient()!=null){
            lat = Double.parseDouble(ShareStorage.retrieveData("lat", ShareStorage.SP.ProtectedData, getContext()));
            lng = Double.parseDouble(ShareStorage.retrieveData("lng", ShareStorage.SP.ProtectedData, getContext()));
        }
        LatLng currentLocationInLatLng = new LatLng(lat,lng) ;

        for(int index = 0;index<list.size();index++){
            LatLng target = new LatLng(list.get(index).getLatLngs()[0],list.get(index).getLatLngs()[1]);
            double distance = LatLngConverter.getDistanceFromLatLngInKm(currentLocationInLatLng,target);
            if (distance <= kmFromSp) {
                RouteCCTV cctv = list.get(index);
                cctv.setDistance(String.valueOf(new DecimalFormat("#.##").format(distance)));
                nearCCTVLocation.add(list.get(index));
                //list.remove(index);
            }
        }
        return nearCCTVLocation;
    }

    private SortedList<RouteCCTV> getSortedList(List<RouteCCTV> list){
        SortedList<RouteCCTV> sortedList = new SortedList<RouteCCTV>(RouteCCTV.class, new SortedList.Callback<RouteCCTV>() {
            @Override
            public int compare(RouteCCTV o1, RouteCCTV o2) {
                double o1_distance = Double.parseDouble(o1.getDistance());
                double o2_distance = Double.parseDouble(o2.getDistance());
                return (o1_distance<o2_distance)?1:0;
            }

            @Override
            public void onInserted(int position, int count) {

            }

            @Override
            public void onRemoved(int position, int count) {

            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {

            }

            @Override
            public void onChanged(int position, int count) {

            }

            @Override
            public boolean areContentsTheSame(RouteCCTV oldItem, RouteCCTV newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areItemsTheSame(RouteCCTV item1, RouteCCTV item2) {
                return item1.equals(item2);
            }
        });
        for (RouteCCTV cctv:list){
            sortedList.add(cctv);
        }
        return sortedList;
    }


    private class HomeAdapter extends RecyclerView.Adapter<HomeViewHolder>{

        SortedList<RouteCCTV>cctvList;

        public HomeAdapter(SortedList<RouteCCTV> nearCCTVLocation) {
            this.cctvList = nearCCTVLocation;

        }

        @Override
        public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sugggest_cctv_list,parent,false);
            HomeViewHolder homeViewHolder = new HomeViewHolder(view);
            return homeViewHolder;
        }

        @Override
        public void onBindViewHolder(HomeViewHolder holder, int position) {
            String url = TRAFFIC_URL + cctvList.get(position).getRef_key() + ".JPG";
            imageLoader.displayImage(url, holder.cctvImage, displayImageOptions);
            holder.title.setText(cctvList.get(position).getDescription()[1]);
            holder.distance.setText(String.format("%s:%s %s",
                    getResources().getString(R.string.distance),
                    cctvList.get(position).getDistance(),
                    getResources().getString(R.string.km)));
        }

        @Override
        public int getItemCount() {
            return cctvList.size();
        }
    }

    private class HomeViewHolder extends RecyclerView.ViewHolder {
        ImageView cctvImage;
        TextView title, distance;

        public HomeViewHolder(View itemView) {
            super(itemView);
            cctvImage = (ImageView) itemView.findViewById(R.id.cctv);
            title = (TextView) itemView.findViewById(R.id.txtCCTVName);
            distance = (TextView) itemView.findViewById(R.id.txtCCTVDistance);
        }
    }
}
