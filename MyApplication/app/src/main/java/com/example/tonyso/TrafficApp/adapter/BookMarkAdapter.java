package com.example.tonyso.TrafficApp.adapter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tonyso.TrafficApp.Interface.OnRemainingTimeListener;
import com.example.tonyso.TrafficApp.MyApplication;
import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.Singleton.LanguageSelector;
import com.example.tonyso.TrafficApp.Singleton.SQLiteHelper;
import com.example.tonyso.TrafficApp.Tab_BookMarkFragment;
import com.example.tonyso.TrafficApp.model.TimedBookMark;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by TonySo on 28/10/15.
 */
public class BookMarkAdapter extends SelectableRecyclerViewAdapter<BookMarkAdapter.ViewHolder>
        implements OnRemainingTimeListener {

    public static final String TAG = BookMarkAdapter.class.getSimpleName();
    List<TimedBookMark> myDatasets;
    SortedList<TimedBookMark> sortedList;

    Tab_BookMarkFragment frag;
    private static String TRAFFIC_URL = "http://tdcctv.data.one.gov.hk/";
    private static String JPG = ".JPG";
    private ImageLoader imageLoader;
    private DisplayImageOptions imageOptions;
    private LanguageSelector languageSelector;
    ViewHolder viewHolderInstance;
    OnItemClickListener onItemClickListener;

    /* Interface for handling clicks - both normal and long ones. */
    public interface OnItemClickListener {
        /**
         * Called when the view is clicked.
         *
         * @param v           view that is clicked
         * @param position    of the clicked item
         * @param isLongClick true if long click, false otherwise
         */
        void onClick(View v, int position, boolean isLongClick);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public BookMarkAdapter(List<TimedBookMark> dataSets,
                           Tab_BookMarkFragment tab_bookMarkFragment) {
        myDatasets = dataSets;
        addDatatoSortedList(myDatasets);

        this.frag = tab_bookMarkFragment;
        setOnItemClickListener(frag);
        imageLoader = ImageLoader.getInstance();
        imageOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_error_black_24dp)
                .cacheInMemory(true)
                .considerExifParams(true)
                .displayer(new SimpleBitmapDisplayer())
                .build();
        languageSelector = LanguageSelector.getInstance(frag.getContext());
    }

    private void addDatatoSortedList(List<TimedBookMark> myDatasets) {
        sortedList = new SortedList<TimedBookMark>(TimedBookMark.class, new SortedList.Callback<TimedBookMark>() {
            @Override
            public int compare(TimedBookMark o1, TimedBookMark o2) {
                return (o1.getRemainTime() < o2.getRemainTime()) ? -1 : 1;
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
                notifyItemRangeRemoved(fromPosition, toPosition);
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
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
        for (TimedBookMark item : myDatasets) {
            sortedList.add(item);
        }
    }

    @Override
    public void onRemainingTimeChanged(ArrayList<TimedBookMark> list) {
        myDatasets = list;
        addDatatoSortedList(myDatasets);
        notifyDataSetChanged();
    }

    public void removeSelectedItem(Integer pos) {
        //myDatasets = bookmarklist;
        //addDatatoSortedList(myDatasets);
        //myDatasets.remove(pos);
        sortedList.beginBatchedUpdates();
        try {
            SQLiteHelper sqLiteHelper = new SQLiteHelper(frag.getContext());
            long success = sqLiteHelper.delete_bookmark(sortedList.get(pos));
            if (success != -1)
                sortedList.removeItemAt(pos);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        sortedList.endBatchedUpdates();
        notifyItemRemoved(pos);
        notifyItemChanged(pos);
        notifyDataSetChanged();
    }

    public SortedList<TimedBookMark> getSortedList() {
        return sortedList;
    }

    public BookMarkAdapter setSortedList(SortedList<TimedBookMark> sortedList) {
        this.sortedList = sortedList;
        return this;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener
    {
        ImageView imageView;
        TextView time, roadName, remainTime, district;
        ProgressBar progressBar;
        View itemView;
        ImageView selectedView;
        OnItemClickListener onItemClickListener;

        public ViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            this.itemView = itemView;
            imageView = (ImageView)itemView.findViewById(R.id.bkImage);
            time = (TextView)itemView.findViewById(R.id.bkTime);
            remainTime = (TextView)itemView.findViewById(R.id.txtRemainTIme);
            roadName = (TextView)itemView.findViewById(R.id.txtRoadName);
            district = (TextView)itemView.findViewById(R.id.txtDistrict);
            progressBar = (ProgressBar)itemView.findViewById(R.id.bkprogressbar);
            selectedView = (ImageView) itemView.findViewById(R.id.selected_overlay);

            this.onItemClickListener = onItemClickListener;

            this.itemView.setOnClickListener(this);
            this.itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "ItemView Click at @ At POS " + getAdapterPosition());
            if (onItemClickListener != null) {
                onItemClickListener.onClick(v, getAdapterPosition(), false);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            Log.d(TAG, "ItemView Long Click at @ At POS " + getAdapterPosition());
            if (onItemClickListener != null) {
                onItemClickListener.onClick(v, getAdapterPosition(), true);
            }
            return false;
        }
    }

    @Override
    public BookMarkAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(frag.getContext()).inflate(R.layout.item_tab_bookmark_recycleritem, parent, false);
        viewHolderInstance = new ViewHolder(view,onItemClickListener);
        return viewHolderInstance;
    }


    @Override
    public void onBindViewHolder(final BookMarkAdapter.ViewHolder holder, final int position) {
        final String concatString = sortedList.get(position).getStartTime() + "--" + sortedList.get(position).getTargetTime();
        holder.time.setText(concatString);

        holder.remainTime.setText("" + sortedList.get(position).getRemainTime());

        int color = randomizedRoutePlaceHolderColor();
        holder.roadName.setBackgroundColor(color);

        if (languageSelector.getLanguage().equals(MyApplication.Language.ENGLISH)) {
            holder.roadName.setText(sortedList.get(position).getBkRouteName()[0]);
            //holder.satLevel.setText(myDatasets.get(position).getSat_level());
            holder.district.setText(sortedList.get(position).getRegions()[0]);
        } else {
            holder.roadName.setText(sortedList.get(position).getBkRouteName()[1]);
            //holder.satLevel.setText(myDatasets.get(position).getSat_level());
            holder.district.setText(sortedList.get(position).getRegions()[1]);
        }

        String url = TRAFFIC_URL.concat(sortedList.get(position).getRouteImageKey()).concat(JPG);
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
        // Highlight the item if it's selected
        holder.selectedView.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);
    }

    private int randomizedRoutePlaceHolderColor() {
        Random random = new Random();
        return Color.argb(random.nextInt(255), random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }

    @Override
    public int getItemCount() {
        return sortedList.size();
    }
}
