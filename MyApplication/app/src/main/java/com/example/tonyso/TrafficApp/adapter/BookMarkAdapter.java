package com.example.tonyso.TrafficApp.adapter;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cocosw.bottomsheet.BottomSheet;
import com.example.tonyso.TrafficApp.MyApplication;
import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.fragment.Tab_BookMarkFragment;
import com.example.tonyso.TrafficApp.listener.OnItemClickListener;
import com.example.tonyso.TrafficApp.listener.OnRemainingTimeListener;
import com.example.tonyso.TrafficApp.model.RouteCCTV;
import com.example.tonyso.TrafficApp.model.TimedBookMark;
import com.example.tonyso.TrafficApp.utility.LanguageSelector;
import com.example.tonyso.TrafficApp.utility.SQLiteHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TonySo on 28/10/15.
 */
public class BookMarkAdapter extends RecyclerView.Adapter<BookMarkAdapter.ViewHolder>
        implements OnRemainingTimeListener {

    public static final String TAG = BookMarkAdapter.class.getSimpleName();
    private static final String TRAFFIC_URL = "http://tdcctv.data.one.gov.hk/";
    private static final String TRAFFIC_SPEED_MAP = "http://resource.data.one.gov.hk/td/";
    private static final String TC = "TC";
    private static final String EN = "EN";

    private static final String JPG = ".JPG";
    private static final String PNG = ".png";

    List<TimedBookMark> myDatasets;
    SortedList<TimedBookMark> sortedList;
    Tab_BookMarkFragment frag;
    ViewHolder viewHolderInstance;
    OnItemClickListener onItemClickListener;
    private ImageLoader imageLoader;
    private DisplayImageOptions imageOptions;
    private LanguageSelector languageSelector;
    private int position;

    public BookMarkAdapter(List<TimedBookMark> dataSets,
                           Tab_BookMarkFragment tab_bookMarkFragment) {
        myDatasets = dataSets;
        addDatatoSortedList(myDatasets);
        this.frag = tab_bookMarkFragment;
        setOnItemClickListener(frag);
        imageLoader = tab_bookMarkFragment.getImageLoader();
        imageOptions = tab_bookMarkFragment.getImageOptions();
        languageSelector = tab_bookMarkFragment.getLanguageSelector();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
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
    }

    public void removeItemWithoutSQLite(int i) {
        sortedList.removeItemAt(i);
        notifyItemRemoved(i);
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

        if (languageSelector.getLanguage().equals(MyApplication.Language.ENGLISH)) {
            holder.roadName.setText(sortedList.get(position).getBkRouteName()[0]);
            //holder.satLevel.setText(myDatasets.get(position).getSat_level());
            holder.district.setText(sortedList.get(position).getRegions()[0]);
        } else {
            holder.roadName.setText(sortedList.get(position).getBkRouteName()[1]);
            //holder.satLevel.setText(myDatasets.get(position).getSat_level());
            holder.district.setText(sortedList.get(position).getRegions()[1]);
        }

        String url = "";
        //Compare Two String
        TimedBookMark route = sortedList.get(position);
        if (route.getType().equals(RouteCCTV.TYPE_CCTV)) {
            url = TRAFFIC_URL + route.getRouteImageKey() + JPG;
            Log.d(TAG, url);
        } else {
            url = TRAFFIC_SPEED_MAP + route.getRouteImageKey() +
                    (languageSelector.getLanguage().equals(MyApplication.Language.ENGLISH) ? EN : TC) + PNG;
            Log.d(TAG, "Else: " + url);
        }

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

    @Override
    public int getItemCount() {
        return sortedList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView imageView;
        TextView time, roadName, remainTime, district;
        ProgressBar progressBar;
        View itemView;
        OnItemClickListener onItemClickListener;

        public ViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            this.itemView = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.bkImage);
            time = (TextView) itemView.findViewById(R.id.bkTime);
            remainTime = (TextView) itemView.findViewById(R.id.txtRemainTIme);
            roadName = (TextView) itemView.findViewById(R.id.txtRoadName);
            district = (TextView) itemView.findViewById(R.id.txtDistrict);
            progressBar = (ProgressBar) itemView.findViewById(R.id.bkprogressbar);

            this.onItemClickListener = onItemClickListener;

            this.itemView.setOnClickListener(this);
            this.itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "ItemView Click at @ At POS " + getAdapterPosition());
            onItemClickListener.onClick(getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            new BottomSheet.Builder(frag.getActivity()).title("title").sheet(R.menu.bookmark).listener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case R.id.bookmark_delete:
                            onItemClickListener.onClick(position, true);
                            break;
                        case R.id.bookmark_share:
                            break;
                        default:
                            break;
                    }
                }
            }).show();
            return true;
        }
    }
}
