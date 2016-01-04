package com.example.tonyso.TrafficApp.adapter;

import android.content.Context;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.model.NearbyLocation;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by soman on 2016/1/3.
 */
public class NearPlaceItemAdpater extends RecyclerView.Adapter<NearPlaceItemAdpater.ViewHolder> {

    Context context;
    SortedList<NearbyLocation> sortedList;

    public NearPlaceItemAdpater(Context context, final List<NearbyLocation> locationlist) {
        this.context = context;
        sortedList = new SortedList<NearbyLocation>(NearbyLocation.class, new SortedList.Callback<NearbyLocation>() {
            @Override
            public int compare(NearbyLocation o1, NearbyLocation o2) {
                return o1.getName().compareTo(o2.getName());
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
            public boolean areContentsTheSame(NearbyLocation oldItem, NearbyLocation newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areItemsTheSame(NearbyLocation item1, NearbyLocation item2) {
                return item1 == item2;
            }
        });
        for (NearbyLocation item : locationlist) {
            sortedList.add(item);
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_traffic_info_detail_nearby_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageLoader.getInstance().displayImage(sortedList.get(position).getIcon(), holder.imageView);
        holder.title.setText(sortedList.get(position).getName());
        //holder.Distance.setText(sortedList.get(position).getVicinity());
    }

    @Override
    public int getItemCount() {
        Log.e("Size", "" + sortedList.size());
        return sortedList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title, Distance;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.txtNearLocationTitle);
            Distance = (TextView) itemView.findViewById(R.id.txtNearLocationDistance);
            imageView = (ImageView) itemView.findViewById(R.id.imgNearLocation);

        }
    }
}