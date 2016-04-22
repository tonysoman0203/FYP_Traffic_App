package com.example.tonyso.TrafficApp.adapter;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tonyso.TrafficApp.MyApplication;
import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.fragment.NavTrafficSuggestCCTVFragment;
import com.example.tonyso.TrafficApp.model.RouteCCTV;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kyleduo.switchbutton.SwitchButton;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soman on 2016/2/14.
 */
public class CCTVListAdapter extends RecyclerView.Adapter<CCTVListAdapter.ViewHolder> {

    private NavTrafficSuggestCCTVFragment context;
    List<RouteCCTV> routeCCTVList;
    private static final String TRAFFIC_URL = "http://tdcctv.data.one.gov.hk/";
    private ImageLoader imageLoader;
    private DisplayImageOptions displayImageOptions;
    private SortedList<RouteCCTV> sortedList;
    private Bundle savedInstanceState;
    private List<Boolean> mSbStates;

    public void setImageLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    public void setDisplayImageOptions(DisplayImageOptions displayImageOptions) {
        this.displayImageOptions = displayImageOptions;
    }

    public CCTVListAdapter(List<RouteCCTV> routeCCTVList, NavTrafficSuggestCCTVFragment context) {
        this.routeCCTVList = routeCCTVList;
        this.context = context;
        sortedList = new SortedList<>(RouteCCTV.class, new SortedList.Callback<RouteCCTV>() {
            @Override
            public int compare(RouteCCTV o1, RouteCCTV o2) {
                double p1 = Double.parseDouble(o1.getDistance());
                double p2 = Double.parseDouble(o2.getDistance());
                return (p1 < p2) ? -1 : 1;
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(RouteCCTV oldItem, RouteCCTV newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areItemsTheSame(RouteCCTV item1, RouteCCTV item2) {
                return item1 == item2;
            }
        });
        for (RouteCCTV i : routeCCTVList)
            sortedList.add(i);

        mSbStates = new ArrayList<>(getItemCount());
        for (int i = 0; i < getItemCount(); i++) {
            mSbStates.add(false);
        }
    }


    @Override
    public CCTVListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sugggest_cctv_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CCTVListAdapter.ViewHolder holder, final int position) {
        String url = TRAFFIC_URL + sortedList.get(position).getRef_key() + ".JPG";
        imageLoader.displayImage(url, holder.cctvImage, displayImageOptions);
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
                MapsInitializer.initialize(context.getContext());
                LatLng latLng = new LatLng(sortedList.get(position).getLatLngs()[0], sortedList.get(position).getLatLngs()[1]);
                Log.e(context.getTag(),""+latLng.toString());
                map.addMarker(new MarkerOptions()
                        .title(sortedList.get(position).getName())
                        .snippet(sortedList.get(position).getDescription()[0])
                        .position(latLng));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));
            }
        });
    }


    public void setSavedInstanceState(Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
    }
    public Resources getResources() {
        return context.getResources();
    }

    @Override
    public int getItemCount() {
        return routeCCTVList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cctvImage;
        TextView title, distance;
        MapView mapView;
        GoogleMap map;
        SwitchButton sbtext;

        public ViewHolder(View itemView) {
            super(itemView);
            cctvImage = (ImageView) itemView.findViewById(R.id.cctv);
            title = (TextView) itemView.findViewById(R.id.txtCCTVName);
            distance = (TextView) itemView.findViewById(R.id.txtCCTVDistance);
            mapView = (MapView)itemView.findViewById(R.id.mapview);
            sbtext = (SwitchButton)itemView.findViewById(R.id.sb_text);
            sbtext.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        cctvImage.setVisibility(View.INVISIBLE);
                        mapView.setVisibility(View.VISIBLE);
                    }else{
                        cctvImage.setVisibility(View.VISIBLE);
                        mapView.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
    }
}
