package com.example.tonyso.TrafficApp.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.util.SortedList;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tonyso.TrafficApp.MyApplication;
import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.baseclass.BaseFragment;
import com.example.tonyso.TrafficApp.listener.OnItemClickListener;
import com.example.tonyso.TrafficApp.model.RouteCCTV;
import com.example.tonyso.TrafficApp.utility.LatLngConverter;
import com.example.tonyso.TrafficApp.utility.ShareStorage;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kyleduo.switchbutton.SwitchButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TonySo on 11/1/16.
 */
public class Tab_Home_Fragment extends BaseFragment implements OnItemClickListener {

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
        //recyclerView.setHasFixedSize(true);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.view);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeAdapter = new HomeAdapter();
        homeAdapter.setFragmentManager(getChildFragmentManager());
        SortedList<RouteCCTV> sortedList = getSortedList(getNearCCTVLocation());
        homeAdapter.setSortedList(sortedList);
        homeAdapter.setSavedInstanceState(savedInstanceState);
        homeAdapter.setOnItemClickListener(Tab_Home_Fragment.this);
        recyclerView.setAdapter(homeAdapter);
        swipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        swipeRefreshLayout.setOnRefreshListener(null);
    }

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            List<RouteCCTV> cctvs = getNearCCTVLocation();
            homeAdapter = new HomeAdapter();
            SortedList<RouteCCTV> sortedList = getSortedList(cctvs);
            homeAdapter.setSortedList(sortedList);
            homeAdapter.setOnItemClickListener(Tab_Home_Fragment.this);
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
                mOnRefreshListener.onRefresh();
            }
        });
    }

    public List<RouteCCTV> getNearCCTVLocation() {
        List<RouteCCTV> nearCCTVLocation = new ArrayList<>();
        List<RouteCCTV> list = routeList;
        int kmFromSp = MyApplication.KM_IN_NEAR;
        Log.d(TAG, "KM In Share Pref. = " + kmFromSp);
        double lat = 0,lng = 0;
        LatLng currentLocationInLatLng = null;

        if (ShareStorage.retrieveData("lat", ShareStorage.SP.ProtectedData, getContext())!=null
                && ShareStorage.retrieveData("lat", ShareStorage.SP.ProtectedData, getContext()).length()>0){
            lat = Double.parseDouble(ShareStorage.retrieveData("lat", ShareStorage.SP.ProtectedData, getContext()));
        }else{
            lat = 0.00;
        }

        if (ShareStorage.retrieveData("lng", ShareStorage.SP.ProtectedData, getContext())!=null&&
                ShareStorage.retrieveData("lng", ShareStorage.SP.ProtectedData, getContext()).length()>0){
            lng = Double.parseDouble(ShareStorage.retrieveData("lng", ShareStorage.SP.ProtectedData, getContext()));
        }else{
            lng = 0.00;
        }

        currentLocationInLatLng = new LatLng(lat,lng);

        for(int index = 0;index<list.size();index++){
            LatLng target = new LatLng(list.get(index).getLatLngs()[0],list.get(index).getLatLngs()[1]);
            double distance = LatLngConverter.getDistanceFromLatLngInKm(currentLocationInLatLng, target);
            if (distance <= kmFromSp) {
                RouteCCTV cctv = list.get(index);
                cctv.setDistance(String.valueOf(new DecimalFormat("#.##").format(distance)));
                nearCCTVLocation.add(list.get(index));
            }
        }
        return nearCCTVLocation;
    }

    private SortedList<RouteCCTV> getSortedList(List<RouteCCTV> list){
        SortedList<RouteCCTV> sortedList = new SortedList<>(RouteCCTV.class, new SortedList.Callback<RouteCCTV>() {
            @Override
            public int compare(RouteCCTV o1, RouteCCTV o2) {
                double o1_distance = Double.parseDouble(o1.getDistance());
                double o2_distance = Double.parseDouble(o2.getDistance());
                Log.e(TAG, "" + o1_distance + " " + o2_distance);
                return (o1_distance<o2_distance)?-1:1;
            }

            @Override
            public void onInserted(int position, int count) {
                homeAdapter.notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                homeAdapter.notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                homeAdapter.notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onChanged(int position, int count) {
                homeAdapter.notifyItemRangeChanged(position, count);
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

    @Override
    public void onClick(int position, boolean isLongClick) {
        if (!isLongClick) {
            // final Intent intent = new Intent(getActivity(), InfoDetailFragment.class);
            final String title = (MyApplication.CURR_LANG.equals(MyApplication.Language.ZH_HANT)) ?
                    homeAdapter.getSortedList().get(position).getDescription()[1] :
                    homeAdapter.getSortedList().get(position).getDescription()[0];
            Log.e(TAG, title);

            InfoDetailFragment fragment = InfoDetailFragment.newInstance(title, InfoDetailFragment.ADD_ROUTE_TYPE, homeAdapter.getSortedList().get(position));
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.add(fragment, InfoDetailFragment.TAG);
            fragmentTransaction.commit();

        }
    }

    private class HomeAdapter extends RecyclerView.Adapter<HomeViewHolder>
    {
        private SortedList<RouteCCTV> sortedList;
        private OnItemClickListener onItemClickListener;
        private FragmentManager fragmentManager;
        private Bundle savedInstanceState;
        private List<Boolean> mSbStates;

        public HomeAdapter() {

        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sugggest_cctv_list,parent,false);
            return new HomeViewHolder(view, onItemClickListener,savedInstanceState);
        }

        @Override
        public void onBindViewHolder(final HomeViewHolder holder, final int position) {
            holder.sbtext.setOnCheckedChangeListener(null);
            holder.sbtext.setCheckedImmediately(mSbStates.get(position));

            String url = TRAFFIC_URL + sortedList.get(position).getRef_key() + ".JPG";
            imageLoader.displayImage(url, holder.cctvImage, displayImageOptions);
            //holder.cctvImage.setVisibility(View.INVISIBLE);
            //holder.mapView.setVisibility(View.VISIBLE);
            if (MyApplication.CURR_LANG.equals(MyApplication.Language.ENGLISH)) {
                holder.title.setText(sortedList.get(position).getDescription()[0]);
            } else {
                holder.title.setText(sortedList.get(position).getDescription()[1]);
            }

            holder.distance.setText(String.format("%s %s %s",
                    getResources().getString(R.string.distance),
                    sortedList.get(position).getDistance(),
                    getResources().getString(R.string.km)));

            holder.sbtext.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mSbStates.set(position, isChecked);
                    boolean isSet = mSbStates.get(position);
                    if (isSet) {
                        changeImageToMap(holder.cctvImage, holder.mapView);
                    }else {
                        changeMapToImage(holder.cctvImage, holder.mapView);
                    }
                }

                private void changeImageToMap(ImageView cctvImage, MapView mapView) {
                    mapView.setVisibility(View.VISIBLE);
                    cctvImage.setVisibility(View.INVISIBLE);
                }

                private void changeMapToImage(ImageView cctvImage, MapView mapView) {
                    mapView.setVisibility(View.INVISIBLE);
                    cctvImage.setVisibility(View.VISIBLE);
                }
            });

            holder.mapView.onCreate(savedInstanceState);
            holder.mapView.onResume();
            holder.mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    final GoogleMap map = googleMap;
                    map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    MapsInitializer.initialize(getContext());
                    LatLng latLng = new LatLng(sortedList.get(position).getLatLngs()[0], sortedList.get(position).getLatLngs()[1]);
                    Log.e(TAG,""+latLng.toString());
                    map.addMarker(new MarkerOptions()
                            .title(sortedList.get(position).getName())
                            .snippet(sortedList.get(position).getDescription()[0])
                            .position(latLng));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));
                }
            });

        }



        @Override
        public int getItemCount() {
            return sortedList.size();
        }

        public void setSortedList(SortedList<RouteCCTV> sortedList) {
            this.sortedList = sortedList;
            mSbStates = new ArrayList<>(getItemCount());
            for (int i = 0; i < getItemCount(); i++) {
                mSbStates.add(false);
            }

        }

        public SortedList<RouteCCTV> getSortedList() {
            return sortedList;
        }


        public void setFragmentManager(FragmentManager fragmentManager) {
            this.fragmentManager = fragmentManager;
        }

        public void setSavedInstanceState(Bundle savedInstanceState) {
            this.savedInstanceState = savedInstanceState;
        }
    }


    private class HomeViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        ImageView cctvImage;
        TextView title, distance;
        OnItemClickListener onItemClickListener;
        MapView mapView;
        SwitchButton sbtext;

        public HomeViewHolder(View itemView, OnItemClickListener onItemClickListener, Bundle savedInstanceState) {
            super(itemView);
            cctvImage = (ImageView) itemView.findViewById(R.id.cctv);
            title = (TextView) itemView.findViewById(R.id.txtCCTVName);
            distance = (TextView) itemView.findViewById(R.id.txtCCTVDistance);
            mapView = (MapView)itemView.findViewById(R.id.mapview);
            sbtext = (SwitchButton)itemView.findViewById(R.id.sb_text);
            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onClick(getAdapterPosition(), false);
        }
    }
}
