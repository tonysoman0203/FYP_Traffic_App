package com.example.tonyso.TrafficApp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.model.Place;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlacePhotoResult;

import java.util.List;

/**
 * Created by TonySo on 18/9/2015.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder>{

    Context context;
    List<Place> places;
    private GoogleApiClient mGoogleApiClient;

    public HomeAdapter(Context context, List<Place> places) {
        this.places = places;
        this.context = context;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_traffic_home,parent,false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeViewHolder holder, int position) {
        holder.placeName.setText(places.get(position).getName());
    }


    private ResultCallback<PlacePhotoResult> mDisplayPhotoResultCallback
            = new ResultCallback<PlacePhotoResult>() {
        @Override
        public void onResult(PlacePhotoResult placePhotoResult) {
            if (!placePhotoResult.getStatus().isSuccess()) {
                return;
            }

        }
    };

    /**
     * Load a bitmap from the photos API asynchronously
     * by using buffers and result callbacks.

     private void placePhotosAsync(final HomeAdapter.HomeViewHolder viewHolder, String placeid) {
     Places.GeoDataApi.getPlacePhotos(mGoogleApiClient, placeid)
     .setResultCallback(new ResultCallback<PlacePhotoMetadataResult>() {
    @Override public void onResult(PlacePhotoMetadataResult photos) {
    if (!photos.getStatus().isSuccess()) {
    return;
    }
    PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
    if (photoMetadataBuffer.getCount() > 0) {
    // Display the first bitmap in an ImageView in the size of the view
    photoMetadataBuffer.get(0)
    .getScaledPhoto(mGoogleApiClient, viewHolder.imageView.getWidth(),
    viewHolder.imageView.getHeight())
    .setResultCallback(mDisplayPhotoResultCallback);
    }
    photoMetadataBuffer.release();
    }
    });
     }
     */

    @Override
    public int getItemCount() {
        return places.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder{
        TextView placeName;

        public HomeViewHolder(View itemView) {
            super(itemView);
            placeName = (TextView) itemView.findViewById(R.id.lblPlace);
        }

    }
}
