package com.example.tonyso.TrafficApp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tonyso.TrafficApp.R;

/**
 * Created by soman on 2016/2/14.
 */
public class CCTVListAdapter extends RecyclerView.Adapter<CCTVListAdapter.ViewHolder> {

    public CCTVListAdapter() {

    }

    @Override
    public CCTVListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sugggest_cctv_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CCTVListAdapter.ViewHolder holder, int position) {
        holder.cctvImage.setImageResource(R.drawable.placeholder);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cctvImage;
        TextView title, distance;

        public ViewHolder(View itemView) {
            super(itemView);
            cctvImage = (ImageView) itemView.findViewById(R.id.cctv);
            title = (TextView) itemView.findViewById(R.id.txtCCTVName);
            distance = (TextView) itemView.findViewById(R.id.txtCCTVDistance);
        }
    }
}
