package com.packet_systems.activity.psiactivityrecord.adapter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.packet_systems.activity.psiactivityrecord.ListingActivity;
import com.packet_systems.activity.psiactivityrecord.R;
import com.packet_systems.activity.psiactivityrecord.data.ActivityData;

import java.util.List;

/**
 * Created by damangrea on 09/04/18.
 */

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityHolder> {
    List<ActivityData> activityDataList;

    public ActivityAdapter(List<ActivityData> activityDataList) {
        this.activityDataList = activityDataList;
    }

    @Override
    public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_listing_row, parent, false);
        ActivityHolder activityHolder = new ActivityHolder(v);
        return activityHolder;
    }

    public ActivityData getItem(int position) {
        return activityDataList.get(position);
    }

    @Override
    public void onBindViewHolder(ActivityHolder holder, int position) {

        holder.tv_scheduled.setText(activityDataList.get(position).getScheduled());
        holder.tv_timestart.setText(activityDataList.get(position).getTime_start());
        holder.tv_timeend.setText(activityDataList.get(position).getTime_end());
        holder.tv_customer.setText(activityDataList.get(position).getCustomer());
        holder.tv_so_number.setText(activityDataList.get(position).getSo());
        holder.tv_activity.setText(activityDataList.get(position).getActivity());
        holder.tv_category.setText(activityDataList.get(position).getActivity_type());
        switch (activityDataList.get(position).getApproved()) {
            case "0": {
                holder.tv_status.setText("Draft");
                holder.cardView.setCardBackgroundColor(Color.TRANSPARENT);
                break;
            }
            case "1": {
                holder.tv_status.setText("Submitted");
                holder.cardView.setCardBackgroundColor(Color.GRAY);
                break;
            }
            case "3": {
                holder.tv_status.setText("Approved");
                holder.cardView.setCardBackgroundColor(Color.GREEN);
                break;
            }
            case "4": {
                holder.tv_status.setText("Rejected");
                holder.cardView.setCardBackgroundColor(Color.RED);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return activityDataList.size();
    }

    public static class ActivityHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tv_status, tv_scheduled, tv_timestart, tv_timeend, tv_customer, tv_so_number, tv_activity, tv_category;

        public ActivityHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cv_activity);
            tv_status = (TextView) itemView.findViewById(R.id.tv_status);
            tv_scheduled = (TextView) itemView.findViewById(R.id.tv_scheduled);
            tv_timestart = (TextView) itemView.findViewById(R.id.tv_timestart);
            tv_timeend = (TextView) itemView.findViewById(R.id.tv_timeend);
            tv_customer = (TextView) itemView.findViewById(R.id.tv_customer);
            tv_so_number = (TextView) itemView.findViewById(R.id.tv_so_number);
            tv_activity = (TextView) itemView.findViewById(R.id.tv_activity);
            tv_category = (TextView) itemView.findViewById(R.id.tv_category);
        }
    }


}
