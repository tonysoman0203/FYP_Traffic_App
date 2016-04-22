package com.example.tonyso.TrafficApp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.util.SortedList;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.tonyso.TrafficApp.MyApplication;
import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.baseclass.BaseDialogFragment;
import com.example.tonyso.TrafficApp.listener.OnItemClickListener;
import com.example.tonyso.TrafficApp.model.NotifyMsg;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by TonySo on 21/4/16.
 */
public class NotificationListFragment extends BaseDialogFragment implements OnItemClickListener {

    private static final String TAG = NotificationListFragment.class.getCanonicalName();
    View view;
    RecyclerView recyclerView ;
    NotificationAdapter adapter;
    Toolbar toolbar;
    CoordinatorLayout coordinatorLayout;
    View bottomSheet ;
    BottomSheetBehavior behavior;
    MyApplication myapp;

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
        toolbar.setTitle(getString(R.string.Notification_Title));
        myapp = (MyApplication) getActivity().getApplication();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinate_layout);
        bottomSheet = coordinatorLayout.findViewById(R.id.bottom_sheet);
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        recyclerView = (RecyclerView) view.findViewById(R.id.notificationList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new NotificationAdapter(MyApplication.notifyMsgList,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(int position, boolean isLongClick) {
        final NotifyMsg msg = MyApplication.notifyMsgList.get(position);
        Animation growAnimation = AnimationUtils.loadAnimation(this.getContext(), R.anim.grow_color);
        Animation shrinkAnimation = AnimationUtils.loadAnimation(this.getContext(), R.anim.shrink_color);
        TextView title = (TextView) bottomSheet.findViewById(R.id.title);
        final TextView date = (TextView) bottomSheet.findViewById(R.id.date);
        final TextView message = (TextView) bottomSheet.findViewById(R.id.text);
        final TextView from = (TextView) bottomSheet.findViewById(R.id.from);
        title.setText(msg.getTitle());
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_DRAGGING:
                      break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        date.setText(msg.getDate());
                        message.setText(msg.getMessage());
                        from.setText(msg.getFrom());
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {}
        });
    }



    private class NotificationAdapter extends RecyclerView.Adapter<NotificationViewHolder>{

        List<NotifyMsg> notificationSortedList;
        OnItemClickListener onItemClickListener;
        public NotificationAdapter(List<NotifyMsg> notifyMsgs,OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
            this.notificationSortedList = notifyMsgs;
        }


        @Override
        public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
            return new NotificationViewHolder(view,onItemClickListener);
        }

        @Override
        public void onBindViewHolder(NotificationViewHolder holder, int position) {
            holder.date.setText(notificationSortedList.get(position).getDate());
            holder.from.setText(notificationSortedList.get(position).getFrom());
            holder.title.setText(notificationSortedList.get(position).getTitle());
        }

        @Override
        public int getItemCount() {
            return notificationSortedList.size();
        }
    }

    private class NotificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView date, title, from;
        OnItemClickListener onItemClickListener;

        public NotificationViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.NotificationDate);
            title = (TextView) itemView.findViewById(R.id.NotificationTitle);
            from = (TextView) itemView.findViewById(R.id.NotificationFrom);
            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onClick(getAdapterPosition(),false);
        }
    }
}
