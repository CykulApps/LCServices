package com.cykulapps.lcservices.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
        View view = LayoutInflater.from(context).inflate(R.layout.events_dashboard_card, null, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i)
    {
        String headerText = dashboardModelItemList.get(i).getHeader();
        String headerValue = dashboardModelItemList.get(i).getHeaderValue();
        String name = "<font color=#FF5D6791>" + headerText + "</font>";
        String value = "<font color=#3B3C43>" + headerValue + "</font>";
        holder.textViewHeader.setText(name);
        holder.textViewValue.setText(value);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.textViewHeader.setText(Html.fromHtml(name,0));
            holder.textViewValue.setText(Html.fromHtml(value,0));
        }else{
            holder.textViewHeader.setText(Html.fromHtml(name));
            holder.textViewValue.setText(Html.fromHtml(value));
        }
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
