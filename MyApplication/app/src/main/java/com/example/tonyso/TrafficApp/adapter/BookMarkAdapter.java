package com.example.tonyso.TrafficApp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tonyso.TrafficApp.MyApplication;
import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.Singleton.LanguageSelector;
import com.example.tonyso.TrafficApp.model.TimedBookMark;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;
import java.util.Random;

/**
 * Created by TonySo on 28/10/15.
 */
public class BookMarkAdapter extends RecyclerView.Adapter<BookMarkAdapter.ViewHolder>{

    private static final String TAG = BookMarkAdapter.class.getSimpleName();
    List<TimedBookMark> myDatasets;
    Context context;
    private static String TRAFFIC_URL = "http://tdcctv.data.one.gov.hk/";
    private static String JPG = ".JPG";
    private ImageLoader imageLoader;
    private DisplayImageOptions imageOptions;
    private LanguageSelector languageSelector;
    Random random;
    ViewHolder viewHolderInstance;

    public BookMarkAdapter(List<TimedBookMark> dataSets,
                           Context tab_bookMarkFragment) {
        myDatasets = dataSets;
        this.context = tab_bookMarkFragment;
        imageLoader = ImageLoader.getInstance();
        imageOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_error_black_24dp)
                .cacheInMemory(true)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .build();
        languageSelector = LanguageSelector.getInstance(context);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView time, roadName, remainTime, district, routePlaceholder, txtRemainingTime;
        ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.bkImage);
            time = (TextView)itemView.findViewById(R.id.bkTime);
            remainTime = (TextView)itemView.findViewById(R.id.txtRemainTIme);
            roadName = (TextView)itemView.findViewById(R.id.txtRoadName);
            txtRemainingTime = (TextView) itemView.findViewById(R.id.txtRemainTIme);
            //btnDetail = (Button)itemView.findViewById(R.id.btnDetail);
            district = (TextView)itemView.findViewById(R.id.txtDistrict);
            progressBar = (ProgressBar)itemView.findViewById(R.id.bkprogressbar);
            routePlaceholder = (TextView)itemView.findViewById(R.id.txtRoutePlaceHolder);

        }
    }

    @Override
    public BookMarkAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tab_bookmark_recycleritem, parent,false);
        viewHolderInstance = new ViewHolder(view);
        return viewHolderInstance;
    }

    @Override
    public void onBindViewHolder(final BookMarkAdapter.ViewHolder holder, int position) {
        holder.time.setText(myDatasets.get(position).getStartTime().toString());

        int color = randomizedRoutePlaceHolderColor();
        holder.routePlaceholder.setBackgroundColor(color);

        if (languageSelector.getLanguage().equals(MyApplication.Language.ENGLISH)){
            holder.roadName.setText(myDatasets.get(position).getBkRouteName()[0]);
            //holder.satLevel.setText(myDatasets.get(position).getSat_level());
            holder.district.setText(myDatasets.get(position).getRegions()[0]);
            String first_char = String.valueOf(myDatasets.get(position).getRegions()[0].charAt(0));
            holder.routePlaceholder.setText(first_char);
        }else{
            holder.roadName.setText(myDatasets.get(position).getBkRouteName()[1]);
            //holder.satLevel.setText(myDatasets.get(position).getSat_level());
            holder.district.setText(myDatasets.get(position).getRegions()[1]);
            String first_char = String.valueOf(myDatasets.get(position).getRegions()[1].charAt(0));
            holder.routePlaceholder.setText(first_char);
        }
        String url = TRAFFIC_URL.concat(myDatasets.get(position).getRouteImageKey()).concat(JPG);
        imageLoader.displayImage(url, holder.imageView, imageOptions, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                holder.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }

    private int randomizedRoutePlaceHolderColor() {
        random = new Random();
        return Color.argb(random.nextInt(256), random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    @Override
    public int getItemCount() {
        return myDatasets.size();
    }
}
