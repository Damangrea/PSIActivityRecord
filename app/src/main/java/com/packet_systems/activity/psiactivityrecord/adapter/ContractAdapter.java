package com.packet_systems.activity.psiactivityrecord.adapter;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.packet_systems.activity.psiactivityrecord.R;
import com.packet_systems.activity.psiactivityrecord.data.ActivityData;
import com.packet_systems.activity.psiactivityrecord.data.ContractData;

import java.util.List;

/**
 * Created by damangrea on 09/04/18.
 */

public class ContractAdapter extends RecyclerView.Adapter<ContractAdapter.ContractHolder> {
    List<ContractData> contractDataList;

    public ContractAdapter(List<ContractData> contractDataList) {
        this.contractDataList = contractDataList;
    }

    @Override
    public ContractHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contract_listing_row, parent, false);
        ContractHolder contractHolder = new ContractHolder(v);
        return contractHolder;
    }

    public ContractData getItem(int position) {
        return contractDataList.get(position);
    }

    @Override
    public void onBindViewHolder(ContractHolder holder, int position) {

        holder.tv_customer.setText(contractDataList.get(position).getCompany_profile());
        holder.tv_so_number.setText(contractDataList.get(position).getSo());
        holder.tv_proj_name.setText(contractDataList.get(position).getProject_name());
        switch (position % 2) {
            case 0: {
                holder.cardView.setCardBackgroundColor(Color.TRANSPARENT);
                break;
            }
            case 1: {
                holder.cardView.setCardBackgroundColor(Color.LTGRAY);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return contractDataList.size();
    }

    public static class ContractHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tv_customer, tv_proj_name, tv_so_number;

        public ContractHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cv_contract);
            tv_customer = (TextView) itemView.findViewById(R.id.tv_customer);
            tv_so_number = (TextView) itemView.findViewById(R.id.tv_so_number);
            tv_proj_name = (TextView) itemView.findViewById(R.id.tv_proj_name);
        }
    }


}
