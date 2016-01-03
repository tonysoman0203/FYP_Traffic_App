package com.example.tonyso.TrafficApp.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tonyso.TrafficApp.InfoDetailActivity;
import com.example.tonyso.TrafficApp.MyApplication;
import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.Singleton.LanguageSelector;
import com.example.tonyso.TrafficApp.Singleton.SQLiteHelper;
import com.example.tonyso.TrafficApp.Tab_HistoryFragment;
import com.example.tonyso.TrafficApp.model.TimedBookMark;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by soman on 2016/1/1.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private static final String TAG = HistoryAdapter.class.getSimpleName();
    private final DisplayImageOptions imageOptions;
    private Tab_HistoryFragment context;
    private LanguageSelector languageSelector;
    private SortedList<TimedBookMark> mSortedList;

    private static final String TRAFFIC_URL = "http://tdcctv.data.one.gov.hk/";
    private static final String JPG = ".JPG";

    private ImageLoader imageLoader;

    public static final String INTENT_TAG_HISTORY_ITEM = "History Object";
    private static final int HISTORY_VIEW_RECORD_REQUEST_CODE = 00001000;
    private int position;

    public HistoryAdapter(Tab_HistoryFragment context, List<TimedBookMark> historyList) {
        this.context = context;
        this.languageSelector = LanguageSelector.getInstance(context.getContext());
        mSortedList = getmSortedList(historyList);
        imageLoader = ImageLoader.getInstance();
        imageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .build();
    }

    private SortedList<TimedBookMark> getmSortedList(List<TimedBookMark> historylist) {
        SortedList<TimedBookMark> sortedList = new SortedList<TimedBookMark>(TimedBookMark.class, new SortedList.Callback<TimedBookMark>() {
            @Override
            public int compare(TimedBookMark o1, TimedBookMark o2) {
                if (languageSelector.getLanguage().equals(MyApplication.Language.ENGLISH)) {
                    return o1.getRegions()[0].compareTo(o1.getRegions()[0]);
                } else {
                    return o1.getRegions()[1].compareTo(o1.getRegions()[1]);
                }
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
                notifyItemChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(TimedBookMark oldItem, TimedBookMark newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areItemsTheSame(TimedBookMark item1, TimedBookMark item2) {
                return item1.get_id() == item2.get_id();
            }
        });
        for (TimedBookMark t : historylist) {
            sortedList.add(t);
        }
        return sortedList;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public class HistoryViewHolder extends ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, View.OnLongClickListener {


        ImageView imageView;
        TextView time, roadName, district;
        ProgressBar progressBar;
        View itemView;
        LinearLayout timerContainer;

        public HistoryViewHolder(View itemView) {
            super(itemView);
            //initializeLayout
            initializeLayout(itemView);
        }

        private void initializeLayout(View itemView) {
            this.itemView = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.bkImage);
            time = (TextView) itemView.findViewById(R.id.bkTime);
            roadName = (TextView) itemView.findViewById(R.id.txtRoadName);
            district = (TextView) itemView.findViewById(R.id.txtDistrict);
            progressBar = (ProgressBar) itemView.findViewById(R.id.bkprogressbar);
            timerContainer = (LinearLayout) itemView.findViewById(R.id.timerContainer);
            timerContainer.setVisibility(View.GONE);
            this.itemView.setOnClickListener(this);
            this.itemView.setOnCreateContextMenuListener(this);
            this.itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.e(HistoryAdapter.class.getName(), "OnClick at Position " + getAdapterPosition());
            Intent intent = new Intent(context.getContext(), InfoDetailActivity.class);
            intent.putExtra(INTENT_TAG_HISTORY_ITEM, mSortedList.get(getAdapterPosition()).get_id());
            intent.putExtra("type", InfoDetailActivity.VIEW_HISTORY_RECORD);
            context.startActivityForResult(intent, HISTORY_VIEW_RECORD_REQUEST_CODE);
        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(Menu.NONE, R.id.bookmark_delete, Menu.NONE, context.getResources().getString(R.string.history_record_delete));
            menu.add(Menu.NONE, R.id.bookmark_share, Menu.NONE, context.getResources().getString(R.string.popup_share));
        }

        @Override
        public boolean onLongClick(View v) {
            setPosition(getAdapterPosition());
            return false;
        }
    }

    public int removeSelectedItem(Integer pos) {
        mSortedList.beginBatchedUpdates();
        try {
            SQLiteHelper sqLiteHelper = new SQLiteHelper(context.getContext());
            long success = sqLiteHelper.delete_bookmark(mSortedList.get(pos));
            if (success != -1)
                mSortedList.removeItemAt(pos);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        mSortedList.endBatchedUpdates();
        notifyItemRemoved(pos);

        int size = mSortedList.size();
        return size;
    }
    
    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tab_bookmark_recycleritem, null, false);
        HistoryViewHolder historyViewHolder = new HistoryViewHolder(view);
        return historyViewHolder;
    }

    @Override
    public void onBindViewHolder(final HistoryViewHolder holder, int position) {
        final String concatString = mSortedList.get(position).getStartTime() + "--" + mSortedList.get(position).getTargetTime();
        holder.time.setText(concatString);
        if (languageSelector.getLanguage().equals(MyApplication.Language.ENGLISH)) {
            holder.roadName.setText(mSortedList.get(position).getBkRouteName()[0]);
            //holder.satLevel.setText(myDatasets.get(position).getSat_level());
            holder.district.setText(mSortedList.get(position).getRegions()[0]);
        } else {
            holder.roadName.setText(mSortedList.get(position).getBkRouteName()[1]);
            //holder.satLevel.setText(myDatasets.get(position).getSat_level());
            holder.district.setText(mSortedList.get(position).getRegions()[1]);
        }

        String url = TRAFFIC_URL.concat(mSortedList.get(position).getRouteImageKey()).concat(JPG);
        imageLoader.displayImage(url, holder.imageView, imageOptions, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                holder.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                holder.progressBar.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mSortedList.size();
    }


}
