package com.example.tonyso.TrafficApp.adapter;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.tonyso.TrafficApp.Interface.RecyclerViewHelper;
import com.example.tonyso.TrafficApp.MyApplication;
import com.example.tonyso.TrafficApp.OnButtonClickListener;
import com.example.tonyso.TrafficApp.OnSetTimeListener;
import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.Singleton.LanguageSelector;
import com.example.tonyso.TrafficApp.Singleton.SQLiteHelper;
import com.example.tonyso.TrafficApp.Tab_BookMarkFragment;
import com.example.tonyso.TrafficApp.model.RouteCCTV;
import com.example.tonyso.TrafficApp.model.TimedBookMark;
import com.example.tonyso.TrafficApp.utility.CommonUtils;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by soman on 2015/12/25.
 */
public class InfoDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements RecyclerViewHelper{

    Context context;
    private int size;

    private static final int TYPE_BASE = 0;
    private static final int TYPE_NEAR_HEADER = 1;
    private static final int TYPE_BOOKMAKR_HEADER = 3;
    private static final int TYPE_SHARE_HEADER = 5;
    private static final int TYPE_NEAR_ITEM = 2;
    private static final int TYPE_BOOKMARK_ITEM = 4;

    SQLiteDatabase sqLiteDatabase;
    Resources res;
    private static CoordinatorLayout coordinatorLayout;

    private static final String TAG = InfoDetailAdapter.class.getName();
    RouteCCTV route;
    LanguageSelector languageSelector;
    GregorianCalendar calendar;
    private String tempString;
    private String startTime, endTime;
    private int bookmark_id = 0;

    public InfoDetailAdapter(Context content, int recyclerViewSize, CoordinatorLayout coordinatorLayout,RouteCCTV routeCCTV){
        this.context = content;
        this.size = recyclerViewSize;
        res = context.getResources();
        this.coordinatorLayout = coordinatorLayout;
        this.route = routeCCTV;
        getObjectInstance();
    }

    private void getObjectInstance(){
        sqLiteDatabase =SQLiteHelper.getDatabase(context);
        languageSelector = LanguageSelector.getInstance(context);
        calendar = new GregorianCalendar();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            default:
                View v1 = inflater.inflate(R.layout.item_traffic_info_detail_menu, parent, false);
                viewHolder = new ViewHolder(v1,this);
                break;
            case TYPE_NEAR_ITEM:
                View v2 = inflater.inflate(R.layout.item_traffic_info_detail_near, parent, false);
                viewHolder = new RecyclerView.ViewHolder(v2) {
                    @Override
                    public String toString() {
                        return super.toString();
                    }
                };
                break;
            case TYPE_NEAR_HEADER :case TYPE_BOOKMAKR_HEADER: case TYPE_SHARE_HEADER:
                View vh = inflater.inflate(R.layout.item_traffic_info_detail_header, parent, false);
                viewHolder = new ViewHolderHeader(vh);
                break;
            case TYPE_BOOKMARK_ITEM:
                View bk = inflater.inflate(R.layout.item_traffic_info_detail_bookmark, parent, false);
                viewHolder = new AddBookMarkViewHolder(bk);
                break;
        }
            return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.e(TAG, "" + position);
        if (position == TYPE_NEAR_HEADER){
            ((ViewHolderHeader)holder).header.setText(res.getString(R.string.popup_near));
        }else if (position == TYPE_BOOKMAKR_HEADER){
            ((ViewHolderHeader)holder).header.setText(res.getString(R.string.popup_add_bookMark));
        }else if (position == TYPE_SHARE_HEADER){
            ((ViewHolderHeader)holder).header.setText(res.getString(R.string.popup_share));
        }else if (position == TYPE_BOOKMARK_ITEM){
            configBookmarkItem(holder,position);
        }else if (position == TYPE_NEAR_ITEM) {

        }
    }

    private void configBookmarkItem(final RecyclerView.ViewHolder holder,int p) {

        if (languageSelector.getLanguage().equals(MyApplication.Language.ENGLISH)){
            ((AddBookMarkViewHolder)holder).routeWrapper.getEditText().setText(route.getDescription()[0]);
            ((AddBookMarkViewHolder)holder).regionWrapper.getEditText().setText(route.getRegion()[0]);
        }else{
            ((AddBookMarkViewHolder)holder).routeWrapper.getEditText().setText(route.getDescription()[1]);
            ((AddBookMarkViewHolder)holder).regionWrapper.getEditText().setText(route.getRegion()[1]);
        }
        OnSetTimeListener startTimeListener = new OnSetTimeListener(((AddBookMarkViewHolder)holder).startTimeWrapper.getEditText(),context);
        OnSetTimeListener endTimeListener = new OnSetTimeListener(((AddBookMarkViewHolder)holder).TargetTimeWrapper.getEditText(),context);
        //GetTime
        startTime =  ((AddBookMarkViewHolder)holder).startTimeWrapper.getEditText().getText().toString();
        endTime =  ((AddBookMarkViewHolder)holder).TargetTimeWrapper.getEditText().getText().toString();

        ((AddBookMarkViewHolder)holder).btnAdd.setTag(4);
        ((AddBookMarkViewHolder)holder).btnReset.setTag(5);

        ((AddBookMarkViewHolder)holder).btnAdd.setOnClickListener(new OnButtonClickListener(this, p));
        ((AddBookMarkViewHolder)holder).btnReset.setOnClickListener(new OnButtonClickListener(this, p));
    }

    @Override
    public int getItemCount() {
        return size + 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return position;
        } else if (position == TYPE_NEAR_ITEM) {
            return TYPE_NEAR_ITEM;
        } else if (position == TYPE_BOOKMARK_ITEM) {
            return TYPE_BOOKMARK_ITEM;
        } else
            return TYPE_BASE;
    }

    private boolean isPositionHeader(int position) {
        return position == TYPE_NEAR_HEADER || position ==TYPE_BOOKMAKR_HEADER || position == TYPE_SHARE_HEADER;
    }

    @Override
    public void onRecyclerViewIndex(int tag,int pos) {
        Snackbar.make(coordinatorLayout,"Button click : "+tag,Snackbar.LENGTH_SHORT).show();
        switch (tag){
            case 1:
                this.notifyItemMoved(pos,TYPE_NEAR_ITEM);
                break;
            case 2:
                this.notifyItemMoved(pos,TYPE_BOOKMARK_ITEM);
                break;
            default:break;
        }
    }

    @Override
    public void onAddBookmarkClick() {
        SQLiteHelper sql = new SQLiteHelper(context);
        long success = sql.add_Bookmark(new TimedBookMark.Builder()
                        .set_id(bookmark_id++)
                        .setBkRouteName(route.getDescription())
                        .setDistrict(route.getRegion())
                        .setRouteImageKey(route.getRef_key())
                        .setTimestamp(startTime)
                        .setTargetTime(endTime)
                        .setIsTimeOver(false).build());
        if (success != -1){
            Snackbar.make(coordinatorLayout,"Adding Bookmark Success...",Snackbar.LENGTH_SHORT).show();
        }else{
            Snackbar.make(coordinatorLayout,"Error Inserting Bookmark...",Snackbar.LENGTH_SHORT).show();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        Button btnShare,btnNear,btnBookmark;
        InfoDetailAdapter infoDetailAdapter;
        public ViewHolder(View itemView, InfoDetailAdapter infoDetailAdapter) {
            super(itemView);
            btnBookmark = (Button) itemView.findViewById(R.id.btnBookmark);
            btnNear = (Button) itemView.findViewById(R.id.btnNear);
            btnShare = (Button) itemView.findViewById(R.id.btnShare);
            this.infoDetailAdapter = infoDetailAdapter;

            //setTag
            btnNear.setTag(1);
            btnBookmark.setTag(2);
            btnShare.setTag(3);

            btnBookmark.setOnClickListener(new OnButtonClickListener(infoDetailAdapter,TYPE_BASE));
            btnNear.setOnClickListener(new OnButtonClickListener(infoDetailAdapter, TYPE_BASE));
            btnShare.setOnClickListener(new OnButtonClickListener(infoDetailAdapter,TYPE_BASE));
        }
    }

    public static class AddBookMarkViewHolder extends RecyclerView.ViewHolder{
        TextInputLayout routeWrapper,regionWrapper,startTimeWrapper,TargetTimeWrapper;
        Button btnAdd,btnReset;

        public AddBookMarkViewHolder(View itemView) {
            super(itemView);
            routeWrapper = (TextInputLayout) itemView.findViewById(R.id.routeWrapper);
            regionWrapper = (TextInputLayout) itemView.findViewById(R.id.regionWrapper);
            startTimeWrapper = (TextInputLayout) itemView.findViewById(R.id.startTimeWrapper);
            TargetTimeWrapper = (TextInputLayout) itemView.findViewById(R.id.targetTimeWrapper);
            btnAdd  = (Button)itemView.findViewById(R.id.btnAdd);
            btnReset  = (Button)itemView.findViewById(R.id.btnReset);
        }
    }
    public static class ViewHolderHeader extends RecyclerView.ViewHolder {
        public final TextView header;

        public ViewHolderHeader(View itemView){
            super(itemView);
            header = (TextView) itemView.findViewById(R.id.txtHeaderText);
        }
    }
}

