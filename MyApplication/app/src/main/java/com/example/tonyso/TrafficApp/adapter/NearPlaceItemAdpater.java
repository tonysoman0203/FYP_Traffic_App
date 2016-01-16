package com.example.tonyso.TrafficApp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.model.NearbyLocation;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by soman on 2016/1/3.
 */
public class NearPlaceItemAdpater extends RecyclerView.Adapter<NearPlaceItemAdpater.ViewHolder> {

    Context context;
    SortedList<NearbyLocation> sortedList;
    GoogleApiClient mGoogleApiClient;
    ViewHolder viewHolder;
    PlacePhotoResult photoResult;
    ImageLoader imageLoader;
    DisplayImageOptions imageOptions;

    public NearPlaceItemAdpater(Context context, final List<NearbyLocation> locationlist, GoogleApiClient mGoogleApiClient) {
        this.context = context;
        this.mGoogleApiClient = mGoogleApiClient;
        imageLoader = ImageLoader.getInstance();
        imageOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_error_black_24dp)
                .cacheInMemory(true)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .build();

        sortedList = new SortedList<NearbyLocation>(NearbyLocation.class, new SortedList.Callback<NearbyLocation>() {
            @Override
            public int compare(NearbyLocation o1, NearbyLocation o2) {
                return o1.getName().toString().compareTo(o2.getName().toString());
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
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //https://maps.googleapis.com/maps/api/place/photo?maxwidth=[IMAGESIZE]&photoreference=[REFERENCEKEY]&sensor=false&key=[YOURKEYHERE]
        StringBuilder urlString = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=");
        urlString.append(sortedList.get(position).getPhotoReference());
        urlString.append("&sensor=true&key=" + context.getResources().getString(R.string.place_api_server_key));
        ImageLoader.getInstance().displayImage(urlString.toString(), holder.imageView, imageOptions, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                holder.imageView.setImageResource(R.drawable.placeholder);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                holder.imageView.setImageResource(R.drawable.placeholder);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                holder.imageView.setImageBitmap(loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                holder.imageView.setImageResource(R.drawable.placeholder);
            }
        });
        holder.title.setText(sortedList.get(position).getName());
        StringBuilder sb;
        if (sortedList.get(position).getDistanceInKm().equals("NOT_FOUND")) {
            holder.Distance.setText(context.getResources().getString(R.string.distance) + context.getString(R.string.notfound));
        } else {
            sb = new StringBuilder(context.getString(R.string.distance) + sortedList.get(position).getDistanceInKm());
            holder.Distance.setText(sb.toString());
        }

    }

    @Override
    public int getItemCount() {
        //Log.e("Size", "" + sortedList.size());
        return sortedList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView title, Distance;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.txtNearLocationTitle);
            Distance = (TextView) itemView.findViewById(R.id.txtNearLocationDistance);
            imageView = (ImageView) itemView.findViewById(R.id.imgNearLocation);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Uri gmmIntentUri = Uri.parse("geo:" + sortedList.get(getAdapterPosition()).getLatlngs().latitude + "," + sortedList.get(getAdapterPosition()).getLatlngs().longitude + "?z=19");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            context.startActivity(mapIntent);
        }
    }

}