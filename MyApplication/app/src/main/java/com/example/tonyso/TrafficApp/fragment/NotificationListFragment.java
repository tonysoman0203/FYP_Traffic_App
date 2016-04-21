package com.example.tonyso.TrafficApp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.baseclass.BaseFragment;

/**
 * Created by TonySo on 21/4/16.
 */
public class NotificationListFragment extends BaseFragment {

    View view;
    RecyclerView recyclerView ;
    NotificationAdapter adapter;

    public static NotificationListFragment newInstance() {
        NotificationListFragment baseFragment = new NotificationListFragment();
        Bundle bundle = new Bundle();
        baseFragment.setArguments(bundle);
        return baseFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notification,container);
        recyclerView = (RecyclerView) view.findViewById(R.id.notificationList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    private class NotificationAdapter extends RecyclerView.Adapter<NotificationViewHolder>{


        public NotificationAdapter() {

        }

        @Override
        public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            NotificationViewHolder notificationViewHolder = new NotificationViewHolder(parent);
            return notificationViewHolder;
        }

        @Override
        public void onBindViewHolder(NotificationViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

    private class NotificationViewHolder extends RecyclerView.ViewHolder{

        public NotificationViewHolder(View itemView) {
            super(itemView);
        }
    }
}
