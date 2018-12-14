package com.cykulapps.lcservices.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cykulapps.lcservices.R;
import com.cykulapps.lcservices.model.SubActivityModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SubActivityAdapter extends RecyclerView.Adapter<SubActivityAdapter.MyViewHolder> {
    Context context;
    ArrayList<SubActivityModel> subActivityModelArrayList;
    clickListener clickListener;


    public SubActivityAdapter(Context context, ArrayList<SubActivityModel> subActivityModelArrayList, SubActivityAdapter.clickListener clickListener) {
        this.context = context;
        this.subActivityModelArrayList = subActivityModelArrayList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, null, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(subActivityModelArrayList.get(position).getDepturl()).into(holder.imageView);
        holder.bind(subActivityModelArrayList.get(position).getDeptName(), clickListener);


    }


    @Override
    public int getItemCount()
    {
        Log.e("time",""+subActivityModelArrayList.size());
        return subActivityModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageView;

        public MyViewHolder(View itemView) {
            super(itemView);
           // cardView = itemView.findViewById(R.id.card);
            imageView = itemView.findViewById(R.id.iv);


        }

        public void bind(final String deptName, final SubActivityAdapter.clickListener clickListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onClick(deptName);
                }
            });

        }


    }

    public interface clickListener {
        void onClick(String deptName);
    }




}
