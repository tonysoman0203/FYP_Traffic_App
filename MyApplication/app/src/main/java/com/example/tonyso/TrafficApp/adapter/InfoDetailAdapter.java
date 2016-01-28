package com.example.tonyso.TrafficApp.adapter;

import android.content.res.Resources;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tonyso.TrafficApp.FindNearLocationAsyncTask;
import com.example.tonyso.TrafficApp.InfoDetailActivity;
import com.example.tonyso.TrafficApp.MyApplication;
import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.fragment.Tab_BookMarkFragment;
import com.example.tonyso.TrafficApp.listener.OnInfoDetailMenuClickListener;
import com.example.tonyso.TrafficApp.listener.OnSetTimeListener;
import com.example.tonyso.TrafficApp.model.RouteCCTV;
import com.example.tonyso.TrafficApp.model.TimedBookMark;
import com.example.tonyso.TrafficApp.utility.CommonUtils;
import com.example.tonyso.TrafficApp.utility.LanguageSelector;
import com.example.tonyso.TrafficApp.utility.SQLiteHelper;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by soman on 2015/12/25.
 */
public class InfoDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements OnInfoDetailMenuClickListener.RecyclerViewListener {


    private static final String TAG = InfoDetailAdapter.class.getName();
    //Constant Integer
    private static final int TYPE_BASE = 0;
    private static final int TYPE_NEAR_HEADER = 1;
    private static final int TYPE_BOOKMAKR_HEADER = 3;
    private static final int TYPE_BOOKMARK_ITEM = 4;
    private static final int TYPE_SHARE_HEADER = 5;
    private static final int TYPE_NEAR_ITEM = 2;
    private static final int VERIFIY_INPUT_SAME_VALUE = 100001;
    private static final int VERIFIY_INPUT_END_IS_BIGGER_THAN_FRONT_VALUE = 100002;
    private static final int VERIFIY_INPUT_FRONT_IS_BIGGER_THAN_END_VALUE = 100003;
    private static final int VERIFIY_INPUT_DATE_FRONT_IS_BIGGER_THAN_END_VALUE = 100004;
    public InfoDetailActivity context;
    OnSetTimeListener startTimeListener, endTImeListener;
    private int size;
    private CoordinatorLayout coordinatorLayout;
    private RouteCCTV route;
    private TimedBookMark timedBookMark;
    private String type;
    private Resources res;
    private LanguageSelector languageSelector;
    private GregorianCalendar calendar;
    private String startTime, endTime;
    private int bookmark_id = 0;
    private GoogleApiClient googleApiClient;

    private InfoDetailAdapter(InfoDetailAdapter infoDetailAdapter) {
        this.context = infoDetailAdapter.getContext();
        this.size = infoDetailAdapter.getSize();
        this.coordinatorLayout = infoDetailAdapter.coordinatorLayout;
        this.route = infoDetailAdapter.route;
        this.type = infoDetailAdapter.type;
        this.googleApiClient = infoDetailAdapter.googleApiClient;
        this.timedBookMark = infoDetailAdapter.timedBookMark;
        res = context.getResources();
        getObjectInstance();
    }

    public InfoDetailAdapter() {
        //Required Empty Constructor to Build adapter instance
    }

    public static int getRemainTime(String startTime, String endTime) {
        long start = 0, end = 0;
        try {
            start = new SimpleDateFormat("yyyy-mm-dd HH:mm").parse(startTime).getTime();
            end = new SimpleDateFormat("yyyy-mm-dd HH:mm").parse(endTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.e(TAG + " GetRemainTime Method ", "" + (int) ((end - start) / 60000));
        return (int) ((end - start) / 60000);
    }

    public InfoDetailAdapter build() {
        return new InfoDetailAdapter(this);
    }

    public TimedBookMark getTimedBookMark() {
        return timedBookMark;
    }

    public InfoDetailAdapter setTimedBookMark(TimedBookMark timedBookMark) {
        this.timedBookMark = timedBookMark;
        return this;
    }

    public InfoDetailActivity getContext() {
        return context;
    }

    public InfoDetailAdapter setContext(InfoDetailActivity context) {
        this.context = context;
        return this;
    }

    public int getSize() {
        return size;
    }

    public InfoDetailAdapter setSize(int size) {
        this.size = size;
        return this;
    }

    public RouteCCTV getRoute() {
        return route;
    }

    public InfoDetailAdapter setRoute(RouteCCTV route) {
        this.route = route;
        return this;
    }

    public CoordinatorLayout getCoordinatorLayout() {
        return coordinatorLayout;
    }

    public InfoDetailAdapter setCoordinatorLayout(CoordinatorLayout coordinatorLayout) {
        this.coordinatorLayout = coordinatorLayout;
        return this;
    }

    public String getType() {
        return type;
    }

    public InfoDetailAdapter setType(String type) {
        this.type = type;
        return this;
    }

    private void getObjectInstance() {
        languageSelector = LanguageSelector.getInstance(context);
        calendar = new GregorianCalendar();
    }

    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    public InfoDetailAdapter setGoogleApiClient(GoogleApiClient googleApiClient) {
        this.googleApiClient = googleApiClient;
        return this;
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
                View v2 = inflater.inflate(R.layout.item_traffic_info_detatl_nearby, parent, false);
                viewHolder = new NearPlaceViewHolder(v2);
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
            configNearItems(holder, position);
        }
    }

    private void configNearItems(RecyclerView.ViewHolder holder, int pos) {
        RecyclerView recyclerView = ((NearPlaceViewHolder) holder).recyclerView;
        ProgressBar progressBar = ((NearPlaceViewHolder) holder).progressBar;
        new FindNearLocationAsyncTask(this, context, recyclerView, progressBar, route.getLatLngs(), googleApiClient, route.getDescription()[0]).execute();
    }

    private void configBookmarkItem(final RecyclerView.ViewHolder holder,int p) {

        if (languageSelector.getLanguage().equals(MyApplication.Language.ENGLISH)){
            ((AddBookMarkViewHolder)holder).routeWrapper.getEditText().setText(route.getDescription()[0]);
            ((AddBookMarkViewHolder)holder).regionWrapper.getEditText().setText(route.getRegion()[0]);
        }else{
            ((AddBookMarkViewHolder)holder).routeWrapper.getEditText().setText(route.getDescription()[1]);
            ((AddBookMarkViewHolder)holder).regionWrapper.getEditText().setText(route.getRegion()[1]);
        }

        if (type.equals(Tab_BookMarkFragment.TYPE_EDIT_BOOKMARK)) {
            ((AddBookMarkViewHolder) holder).startTimeWrapper.getEditText().setText(timedBookMark.getStartTime());
            ((AddBookMarkViewHolder) holder).TargetTimeWrapper.getEditText().setText(timedBookMark.getTargetTime());
        } else if (type.equals(InfoDetailActivity.VIEW_HISTORY_RECORD)) {
            ((AddBookMarkViewHolder) holder).startTimeWrapper.getEditText().setText(timedBookMark.getStartTime());
            ((AddBookMarkViewHolder) holder).TargetTimeWrapper.getEditText().setText(timedBookMark.getTargetTime());

            ((AddBookMarkViewHolder) holder).regionWrapper.getEditText().setEnabled(false);
            ((AddBookMarkViewHolder) holder).routeWrapper.getEditText().setEnabled(false);

            ((AddBookMarkViewHolder) holder).startTimeWrapper.getEditText().setEnabled(false);
            ((AddBookMarkViewHolder) holder).TargetTimeWrapper.getEditText().setEnabled(false);

            ((AddBookMarkViewHolder) holder).btnAdd.setVisibility(View.GONE);
            ((AddBookMarkViewHolder) holder).btnReset.setVisibility(View.GONE);

        }

        startTimeListener = new OnSetTimeListener(((AddBookMarkViewHolder) holder).startTimeWrapper.getEditText(), context);
        endTImeListener = new OnSetTimeListener(((AddBookMarkViewHolder) holder).TargetTimeWrapper.getEditText(), context);

        if (type.equals(Tab_BookMarkFragment.TYPE_EDIT_BOOKMARK)) {
            ((AddBookMarkViewHolder) holder).btnAdd.setText(res.getString(R.string.edit_bookmark));
        } else {
            ((AddBookMarkViewHolder) holder).btnAdd.setText(res.getString(R.string.route_detail_btnSave));
        }

        ((AddBookMarkViewHolder)holder).btnAdd.setTag(4);
        ((AddBookMarkViewHolder)holder).btnReset.setTag(5);

        ((AddBookMarkViewHolder) holder).btnAdd.setOnClickListener(new OnInfoDetailMenuClickListener(this, p, holder));
        ((AddBookMarkViewHolder) holder).btnReset.setOnClickListener(new OnInfoDetailMenuClickListener(this, p, holder));
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

    /**
     * To Determine whether the position is which type of Header
     *
     * @param position
     * @return
     */
    private boolean isPositionHeader(int position) {
        return position == TYPE_NEAR_HEADER || position ==TYPE_BOOKMAKR_HEADER || position == TYPE_SHARE_HEADER;
    }

    @Override
    public void onRecyclerViewIndex(int tag,int pos) {
        Snackbar.make(coordinatorLayout,"Button click : "+tag,Snackbar.LENGTH_SHORT).show();
        switch (tag){
            case 1:
                this.notifyItemMoved(pos, TYPE_NEAR_ITEM);
                break;
            case 2:
                this.notifyItemMoved(pos, TYPE_BOOKMARK_ITEM);
                break;
            default:break;
        }
    }

    @Override
    public void onAddBookmarkClick(RecyclerView.ViewHolder viewHolder) {
        startTime = ((AddBookMarkViewHolder) viewHolder).startTimeWrapper.getEditText().getText().toString();
        endTime = ((AddBookMarkViewHolder) viewHolder).TargetTimeWrapper.getEditText().getText().toString();
        if (!checkInputField(viewHolder)) {
            Log.e("Error Handling ...", "OOps...");
        } else {
            ((AddBookMarkViewHolder) viewHolder).startTimeWrapper.setErrorEnabled(false);
            ((AddBookMarkViewHolder) viewHolder).TargetTimeWrapper.setErrorEnabled(false);
            int isVerifyDateAndTime = checkValidDateAndTime(startTime, endTime);
            Log.e("VerificationDateAndTime", "" + isVerifyDateAndTime);
            Log.e(TAG, "CheckPoint: StartTime:" + startTime + " EndTIme: " + endTime);

            switch (isVerifyDateAndTime) {
                case VERIFIY_INPUT_SAME_VALUE:
                    Snackbar.make(coordinatorLayout, "Same Value", Snackbar.LENGTH_SHORT).show();
                    break;
                case VERIFIY_INPUT_FRONT_IS_BIGGER_THAN_END_VALUE:
                    Snackbar.make(coordinatorLayout, "End is Larger than Front", Snackbar.LENGTH_SHORT).show();
                    break;
                case VERIFIY_INPUT_DATE_FRONT_IS_BIGGER_THAN_END_VALUE:
                    Snackbar.make(coordinatorLayout, "Front Date is Larger than Front", Snackbar.LENGTH_SHORT).show();
                    break;
                case -1:
                    new Exception("Unknown Error In Checking").printStackTrace();
                    break;
                default:
                    if (type.equals("Add_ROUTE")) {
                        SQLiteHelper sql = new SQLiteHelper(context);
                        long success = sql.add_Bookmark(new TimedBookMark.Builder()
                                .set_id(bookmark_id++)
                                .setBkRouteName(route.getDescription())
                                .setDistrict(route.getRegion())
                                .setRouteImageKey(route.getRef_key())
                                .setTimestamp(startTime)
                                .setTargetTime(endTime)
                                .setRemainTime(getRemainTime(startTime, endTime))
                                .setLatLngs(route.getLatLngs())
                                .setType(route.getType())
                                .setIsTimeOver(false).build());
                        if (success != -1) {
                            Snackbar.make(coordinatorLayout, "Adding Bookmark Success...", Snackbar.LENGTH_SHORT).show();
                        } else {
                            Snackbar.make(coordinatorLayout, "Error Inserting Bookmark...", Snackbar.LENGTH_SHORT).show();
                        }
                        break;
                    } else if (type.equals(Tab_BookMarkFragment.TYPE_EDIT_BOOKMARK)) {
                        SQLiteHelper sql = new SQLiteHelper(context);
                        long success = sql.editBookmark(new TimedBookMark.Builder()
                                .set_id(timedBookMark.get_id())
                                .setBkRouteName(route.getDescription())
                                .setDistrict(route.getRegion())
                                .setRouteImageKey(route.getRef_key())
                                .setTimestamp(startTime)
                                .setTargetTime(endTime)
                                .setRemainTime(getRemainTime(startTime, endTime))
                                .setLatLngs(route.getLatLngs())
                                .setType(route.getType())
                                .setIsTimeOver(false).build());
                        if (success != -1) {
                            Snackbar.make(coordinatorLayout, "Editing Bookmark Success...", Snackbar.LENGTH_SHORT).show();
                            context.setResult(Tab_BookMarkFragment.EDIT_BOOKMARK_RESULT_CODE);
                            context.finish();
                        } else {
                            Snackbar.make(coordinatorLayout, "Error Inserting Bookmark...", Snackbar.LENGTH_SHORT).show();
                        }
                        break;
                    } else
                        break;
            }
        }
    }

    @Override
    public void onResetTimerClick(RecyclerView.ViewHolder viewHolder) {
        ((AddBookMarkViewHolder) viewHolder).startTimeWrapper.getEditText().setText("");
        ((AddBookMarkViewHolder) viewHolder).TargetTimeWrapper.getEditText().setText("");
    }

    private boolean checkInputField(RecyclerView.ViewHolder viewHolder) {
        if (TextUtils.isEmpty(((AddBookMarkViewHolder) viewHolder).routeWrapper.getEditText().toString())) {
            ((AddBookMarkViewHolder) viewHolder).routeWrapper.setError("Please Enter a route name...");
            return false;
        } else if (TextUtils.isEmpty(((AddBookMarkViewHolder) viewHolder).regionWrapper.getEditText().toString())) {
            ((AddBookMarkViewHolder) viewHolder).routeWrapper.setError("Please Enter a region name...");
            return false;
        } else if (TextUtils.isEmpty(startTime)) {
            ((AddBookMarkViewHolder) viewHolder).startTimeWrapper.setError("Please Enter a valid date and time...");
            return false;
        } else if (TextUtils.isEmpty(endTime)) {
            ((AddBookMarkViewHolder) viewHolder).TargetTimeWrapper.setError("Please Enter a valid date and time...");
            return false;
        } else
            return true;
    }

    private int checkValidDateAndTime(String startTime, String endTime) {
        Date start = null, end = null, currDate = null;
        long startInSec = 0, endInSec = 0;
        try {
            start = new SimpleDateFormat("yyyy-mm-dd HH:mm").parse(startTime);
            end = new SimpleDateFormat("yyyy-mm-dd HH:mm").parse(endTime);
            currDate = new SimpleDateFormat("yyyy-mm-dd HH:mm").parse(CommonUtils.getCurrentDateTime());
            startInSec = start.getTime();
            endInSec = end.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (start.after(currDate) || end.after(currDate)) {
            Log.e("CheckValidDateAndTime", "start < end");
            return VERIFIY_INPUT_END_IS_BIGGER_THAN_FRONT_VALUE;
        } else if (start.before(currDate) || end.before(currDate)) {
            Log.e("CheckValidDateAndTime", "start Date > end Date");
            return VERIFIY_INPUT_DATE_FRONT_IS_BIGGER_THAN_END_VALUE;
        }
        if (startInSec == (endInSec)) {
            Log.e("CheckValidDateAndTime", "start = end");
            return VERIFIY_INPUT_SAME_VALUE;
        } else if (startInSec > endInSec) {
            Log.e("CheckValidDateAndTime", "start > end");
            return VERIFIY_INPUT_FRONT_IS_BIGGER_THAN_END_VALUE;
        } else if (startInSec < endInSec) {
            Log.e("CheckValidDateAndTime", "start < end");
            return VERIFIY_INPUT_END_IS_BIGGER_THAN_FRONT_VALUE;
        }
        return -1;
    }

    public static class AddBookMarkViewHolder extends RecyclerView.ViewHolder {
        TextInputLayout routeWrapper, regionWrapper, startTimeWrapper, TargetTimeWrapper;
        Button btnAdd, btnReset;

        public AddBookMarkViewHolder(View itemView) {
            super(itemView);
            routeWrapper = (TextInputLayout) itemView.findViewById(R.id.routeWrapper);
            regionWrapper = (TextInputLayout) itemView.findViewById(R.id.regionWrapper);
            startTimeWrapper = (TextInputLayout) itemView.findViewById(R.id.startTimeWrapper);
            TargetTimeWrapper = (TextInputLayout) itemView.findViewById(R.id.targetTimeWrapper);
            btnAdd = (Button) itemView.findViewById(R.id.btnAdd);
            btnReset = (Button) itemView.findViewById(R.id.btnReset);
        }
    }

    /**
     * Header Class extends ViewHolder to display Header TextView
     */
    public static class ViewHolderHeader extends RecyclerView.ViewHolder {
        public final TextView header;

        public ViewHolderHeader(View itemView) {
            super(itemView);
            header = (TextView) itemView.findViewById(R.id.txtHeaderText);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout share, bookmark, near;
        InfoDetailAdapter infoDetailAdapter;

        public ViewHolder(View itemView, InfoDetailAdapter infoDetailAdapter) {
            super(itemView);
            share = (LinearLayout) itemView.findViewById(R.id.shareLayout);
            bookmark = (LinearLayout) itemView.findViewById(R.id.bookmarkLayout);
            near = (LinearLayout) itemView.findViewById(R.id.nearLayout);
            this.infoDetailAdapter = infoDetailAdapter;

            //setTag
            near.setTag(1);
            bookmark.setTag(2);
            share.setTag(3);

            //Set Button Behavior
            bookmark.setOnClickListener(new OnInfoDetailMenuClickListener(infoDetailAdapter, TYPE_BASE, this));
            near.setOnClickListener(new OnInfoDetailMenuClickListener(infoDetailAdapter, TYPE_BASE, this));
            share.setOnClickListener(new OnInfoDetailMenuClickListener(infoDetailAdapter, TYPE_BASE, this));
        }
    }

    public class NearPlaceViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        RecyclerView recyclerView;

        public NearPlaceViewHolder(View v2) {
            super(v2);
            progressBar = (ProgressBar) v2.findViewById(R.id.progressBar);
            recyclerView = (RecyclerView) v2.findViewById(R.id.nearbyList);
            recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        }
    }

}


