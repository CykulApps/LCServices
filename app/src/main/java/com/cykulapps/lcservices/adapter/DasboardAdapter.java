package com.cykulapps.lcservices.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cykulapps.lcservices.R;
import com.cykulapps.lcservices.model.DashboardModel;

import java.util.ArrayList;

public class DasboardAdapter extends RecyclerView.Adapter<DasboardAdapter.MyViewHolder> {
    ArrayList<DashboardModel> dashboardModelItemList;
    Context context;

    public DasboardAdapter(ArrayList<DashboardModel> dashboardModelItemList, Context context) {
        this.dashboardModelItemList = dashboardModelItemList;
        this.context = context;
    }

    @NonNull


    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.cp_dashboard_card, null, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i)
    {
        myViewHolder.textViewHeader.setText(dashboardModelItemList.get(i).getHeader());
        myViewHolder.textViewValue.setText(dashboardModelItemList.get(i).getHeaderValue());


    }

    @Override
    public int getItemCount() {
        return dashboardModelItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView textViewHeader, textViewValue;


        public MyViewHolder(View itemView)
        {
            super(itemView);
            textViewHeader = itemView.findViewById(R.id.textHeader);
            textViewValue = itemView.findViewById(R.id.textvalue);
        }
    }

}
