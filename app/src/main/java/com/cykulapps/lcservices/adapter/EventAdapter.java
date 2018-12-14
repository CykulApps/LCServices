package com.cykulapps.lcservices.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cykulapps.lcservices.BuildConfig;
import com.cykulapps.lcservices.model.EventModel;
import com.cykulapps.lcservices.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {
    ArrayList<EventModel> arrayList;
    Context context;
    protected ItemListener mListener;
    EventModel eventModel;

    public EventAdapter(ArrayList<EventModel> arrayList, Context context, ItemListener itemListener) {
        this.arrayList = arrayList;
        this.context = context;
        this.mListener = itemListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        eventModel = arrayList.get(position);

        String eventID = arrayList.get(position).getEventID();
        Log.e("url", "" + eventModel.getEventUrl());
        String departID = eventModel.getDepartID();
        String category = eventModel.getCategory();
        String deptName = eventModel.getDeptName();

       /* Glide.with(context)
                .load(arrayList.get(position).getEventUrl())
                .into(holder.imageView);*/

       /* Picasso picasso = Picasso.get();
        picasso.setLoggingEnabled(BuildConfig.DEBUG);
        picasso.setIndicatorsEnabled(BuildConfig.DEBUG);
        picasso.load(eventModel.getEventUrl()).into(holder.imageView);*/
        /*holder.textView.setText(eventModel.getEventUrl());*/


        Picasso.get().load(eventModel.getEventUrl()).into(holder.imageView);


        holder.bind(eventID,departID,category,deptName,mListener);
    }

    @Override
    public int getItemCount() {
        Log.e("no", "" + arrayList.size());
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;



        public MyViewHolder(View itemView) {
            super(itemView);
            //itemView.setOnClickListener(this);
            imageView = (ImageView) itemView.findViewById(R.id.iv);

            //cardView=(CardView)itemView.findViewById(R.id.card);
        }

        /*@Override
        public void onClick(View view)
        {
            if(mListener!=null)
            {
                mListener.onItemClick(eventModel);
            }

        }
*/
        public void bind(final String eventID,final String departID, final String category,final String deptName, final ItemListener mListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClick(eventID,departID, category,deptName);
                }
            });
        }
    }

    public interface ItemListener {
        void onItemClick(String eventID,String departID, String category,String deptName);
    }
}
