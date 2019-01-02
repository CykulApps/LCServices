package com.cykulapps.lcservices.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cykulapps.lcservices.R;
import com.cykulapps.lcservices.common.ImageCacheHandler;
import com.cykulapps.lcservices.listeners.ParkItemListener;
import com.cykulapps.lcservices.model.Response;
import com.cykulapps.lcservices.views.CustomTextView;

import java.util.List;

public class EventsSubAdapter extends RecyclerView.Adapter<EventsSubAdapter.DetailsViewHolder> {

    private final List<Response> mImagesOfficeList;
    private final ParkItemListener parkItemListener;
    private Context context;

    public EventsSubAdapter( Context context,List<Response> mImagesOfficeList, ParkItemListener parkItemListener) {
        this.mImagesOfficeList = mImagesOfficeList;
        this.parkItemListener = parkItemListener;
        this.context = context;
    }

    @NonNull
    @Override
    public EventsSubAdapter.DetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new com.cykulapps.lcservices.adapter.EventsSubAdapter
                .DetailsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.events_subdept_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsViewHolder holder, final int position) {

        final Response response = mImagesOfficeList.get(position);

            if (response != null) {
                String deptName = mImagesOfficeList.get(position).getDepartmentName();
                String price = mImagesOfficeList.get(position).getPrice();
                String deptNameCust  = "<font color=#FF5D6791>" + deptName + "</font>";
                String priceCust = "<font color=#3B3C43>" +"₹ "+ price + "</font>";

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    holder.price.setText(Html.fromHtml(priceCust,0));
                    holder.deptName.setText(Html.fromHtml(deptNameCust,0));
                }else{
                    holder.price.setText(Html.fromHtml(priceCust));
                    holder.deptName.setText(Html.fromHtml(deptNameCust));
                }
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        parkItemListener.itemClickListener(position, response);
                    }
                });
            }
    }

    /*@Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

    }*/
    @Override
    public int getItemCount() {
        return mImagesOfficeList.size();
    }

     class DetailsViewHolder extends RecyclerView.ViewHolder {

        private CustomTextView deptName, price;
        DetailsViewHolder(View view) {
            super(view);
            deptName = view.findViewById(R.id.deptName);
            price = view.findViewById(R.id.price);
        }
    }
}
