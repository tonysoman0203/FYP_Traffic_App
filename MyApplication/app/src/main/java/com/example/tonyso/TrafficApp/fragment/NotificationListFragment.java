package com.example.tonyso.TrafficApp.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.SortedList;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.baseclass.BaseDialogFragment;
import com.example.tonyso.TrafficApp.gcm.GCMStartPreference;
import com.example.tonyso.TrafficApp.model.Notification;
import com.example.tonyso.TrafficApp.utility.ShareStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by TonySo on 21/4/16.
 */
public class NotificationListFragment extends BaseDialogFragment {

    View view;
    RecyclerView recyclerView ;
    NotificationAdapter adapter;
    Toolbar toolbar;

    public static NotificationListFragment newInstance() {
        NotificationListFragment baseFragment = new NotificationListFragment();
        Bundle bundle = new Bundle();
        baseFragment.setArguments(bundle);
        return baseFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notification, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        recyclerView = (RecyclerView) view.findViewById(R.id.notificationList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getNotificationFromSP();
        //adapter = new NotificationAdapter();
    }

    public List<Notification> getNotificationFromSP() {
        List<Notification> notifications = new ArrayList<>();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(ShareStorage.SP.PrivateData, 0);
        Map<String, ?> keys = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            Log.d("map values", entry.getKey() + ": " +
                    entry.getValue().toString());
        }
        Integer index = ShareStorage.getInteger(GCMStartPreference.ID, ShareStorage.SP.PrivateData, getContext());

        return notifications;
    }

    private class NotificationAdapter extends RecyclerView.Adapter<NotificationViewHolder>{

        SortedList<Notification> notificationSortedList;

        public NotificationAdapter(List<Notification> notifications) {
            notificationSortedList = new SortedList<Notification>(Notification.class, new SortedList.Callback<Notification>() {
                @Override
                public int compare(Notification o1, Notification o2) {
                    return 0;
                }

                @Override
                public void onInserted(int position, int count) {
                    notifyItemInserted(position);
                }

                @Override
                public void onRemoved(int position, int count) {
                    notifyItemRemoved(position);
                }

                @Override
                public void onMoved(int fromPosition, int toPosition) {
                    notifyItemMoved(fromPosition, toPosition);
                }

                @Override
                public void onChanged(int position, int count) {
                    notifyItemRangeChanged(position, count);
                }

                @Override
                public boolean areContentsTheSame(Notification oldItem, Notification newItem) {
                    return oldItem.equals(newItem);
                }

                @Override
                public boolean areItemsTheSame(Notification item1, Notification item2) {
                    return item1.getId().equals(item2.getId());
                }
            });
            for (Notification notification : notifications)
                notificationSortedList.add(notification);
        }

        @Override
        public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
            return new NotificationViewHolder(view);
        }

        @Override
        public void onBindViewHolder(NotificationViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return notificationSortedList.size();
        }
    }

    private class NotificationViewHolder extends RecyclerView.ViewHolder{
        TextView date, title, from;
        public NotificationViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.NotificationDate);
            title = (TextView) itemView.findViewById(R.id.NotificationTitle);
            from = (TextView) itemView.findViewById(R.id.NotificationFrom);
        }
    }
}
