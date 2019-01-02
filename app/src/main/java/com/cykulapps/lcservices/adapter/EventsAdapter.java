package com.cykulapps.lcservices.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cykulapps.lcservices.R;
import com.cykulapps.lcservices.common.ImageCacheHandler;
import com.cykulapps.lcservices.listeners.ParkItemListener;
import com.cykulapps.lcservices.model.Response;

import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final List<Response> mImagesOfficeList;
    private final ParkItemListener parkItemListener;
    private Context context;

    public EventsAdapter(Context context, List<Response> imagesOfficeList, ParkItemListener parkItemListener) {
        this.mImagesOfficeList = imagesOfficeList;
        this.context = context;
        this.parkItemListener = parkItemListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new com.cykulapps.lcservices.adapter.EventsAdapter.DetailsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_card, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        com.cykulapps.lcservices.adapter.EventsAdapter.DetailsViewHolder detailsViewHolder = (com.cykulapps.lcservices.adapter.EventsAdapter.DetailsViewHolder) holder;

        final Response response = mImagesOfficeList.get(position);
        if (response != null ) {
            ImageCacheHandler.getInstance(context).setImage(detailsViewHolder.imageView, response.getDepartmentID(), response.getImage());
            detailsViewHolder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    parkItemListener.itemClickListener(position,response);
                }
            });
        }

    }



    //This is for set Chat History Details

    @Override
    public int getItemCount() {
        return mImagesOfficeList.size();
    }

    private class DetailsViewHolder extends RecyclerView.ViewHolder {
      private ImageView imageView;
        DetailsViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageDepartment);
        }
    }

}
