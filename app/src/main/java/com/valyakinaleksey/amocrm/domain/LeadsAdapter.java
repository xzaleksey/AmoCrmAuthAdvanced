package com.valyakinaleksey.amocrm.domain;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.valyakinaleksey.amocrm.R;
import com.valyakinaleksey.amocrm.models.MyLead;

import java.util.List;

public class LeadsAdapter extends RecyclerView.Adapter<LeadsAdapter.ViewHolder> {

    private List<MyLead> leadList;

    public LeadsAdapter(List<MyLead> leadList) {
        this.leadList = leadList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(
                inflater.inflate(R.layout.item_leads, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindLead(leadList.get(position));
    }

    @Override
    public int getItemCount() {
        return leadList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvId;
        private final TextView tvName;
        private final TextView tvPrice;
        private final TextView tvStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            tvId = (TextView) itemView.findViewById(R.id.tv_id);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
        }

        void bindLead(MyLead lead) {
            tvId.setText("Id " + lead.getId());
            tvName.setText("Name: " + lead.getName());
            tvPrice.setText("Price:" + lead.getPrice());
            tvStatus.setText("Status: " + lead.getStatus());
        }
    }

}
