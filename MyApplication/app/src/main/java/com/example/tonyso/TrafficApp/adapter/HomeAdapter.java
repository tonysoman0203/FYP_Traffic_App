package com.example.tonyso.TrafficApp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.model.Route;

import java.util.ArrayList;

import it.sephiroth.android.library.picasso.Picasso;

/**
 * Created by TonySo on 18/9/2015.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder>{

    Context context;
    ArrayList<Route> routes;

    private String TRAFFIC_URL = "http://tdcctv.data.one.gov.hk/";
    String[] keys = {
            "H429F",
            "H210F",
            "H421F",
            "H422F2",
            "H904F",
            "H216F",
            "H305F",
            "K810F",
            "FH107F",
            "FH106F",
            "NH101F"
    };

    private static final String JPG_FORMAT = ".JPG";

    public HomeAdapter(Context context) {
        this.context = context;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_traffic_home,parent,false);
        HomeViewHolder homeViewHolder = new HomeViewHolder(view);
        return homeViewHolder;
    }

    @Override
    public void onBindViewHolder(HomeViewHolder holder, int position) {
        ImageView imgview = holder.imgRoute;
        String url = TRAFFIC_URL.concat(keys[position]).concat(JPG_FORMAT);
        //Log.e("URl",url);
        try {
            /*PlaceHolder Error Fetch .... API
            * See in http://square.github.io/picasso/javadoc/index.html
            * */
//            Picasso.Builder picasso = new Picasso.Builder(context);
//            picasso.memoryCache(new LruCache(context));
//
            Picasso.with(context).load(url).into(imgview);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return keys.length;
    }

    /**
     * Created by TonySo on 18/9/2015.
     */
    public class HomeViewHolder extends RecyclerView.ViewHolder{
        ImageView imgRoute;

        public HomeViewHolder(View itemView) {
            super(itemView);
            imgRoute = (ImageView)itemView.findViewById(R.id.imgRouteImage);
        }

    }
}
