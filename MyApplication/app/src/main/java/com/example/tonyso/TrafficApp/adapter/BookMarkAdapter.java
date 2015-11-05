package com.example.tonyso.TrafficApp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.Tab_BookMarkFragment;
import com.example.tonyso.TrafficApp.model.TimedBookMark;

import java.util.List;

import it.sephiroth.android.library.picasso.Picasso;

/**
 * Created by TonySo on 28/10/15.
 */
public class BookMarkAdapter extends RecyclerView.Adapter<BookMarkAdapter.ViewHolder>{

    List<TimedBookMark> myDatasets;
    Context context;
    private static String TRAFFIC_URL = "http://tdcctv.data.one.gov.hk/";
    private static String JPG = ".JPG";
    public BookMarkAdapter(List<TimedBookMark> dataSets, Tab_BookMarkFragment tab_bookMarkFragment) {
        myDatasets = dataSets;
        this.context = tab_bookMarkFragment.getContext();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView time,roadName,remainTime,district;
        Button btnDetail;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.bkImage);
            time = (TextView)itemView.findViewById(R.id.bkTime);
            remainTime = (TextView)itemView.findViewById(R.id.txtRemainTime);
            roadName = (TextView)itemView.findViewById(R.id.txtRoadName);
            //satLevel = (TextView)itemView.findViewById(R.id.txtSatLevel);
            btnDetail = (Button)itemView.findViewById(R.id.btnDetail);
            district = (TextView)itemView.findViewById(R.id.txtDistrict);
        }
    }

    @Override
    public BookMarkAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tab_bookmark_recycleritem, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BookMarkAdapter.ViewHolder holder, int position) {
        holder.time.setText(myDatasets.get(position).getTimestamp().toString());
        holder.roadName.setText(myDatasets.get(position).getBkRouteName());
        //holder.satLevel.setText(myDatasets.get(position).getSatuationLevel());
        holder.district.setText(myDatasets.get(position).getDistrict());
        Picasso.with(context).load(
                TRAFFIC_URL.concat(
                        myDatasets.get(position).getRouteImageKey()).concat(JPG))
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return myDatasets.size();
    }
}
