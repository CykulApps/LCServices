package com.cykulapps.lcservices.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cykulapps.lcservices.R;
import com.cykulapps.lcservices.common.ImageCacheHandler;
import com.cykulapps.lcservices.listeners.HomeItemListener;
import com.cykulapps.lcservices.model.Response;

import java.util.List;

public class HomeItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final List<Response> mImagesOfficeList;
    private final HomeItemListener homeItemListener;
    private Context context;

    public HomeItemsAdapter(Context context, List<Response> imagesOfficeList, HomeItemListener homeItemListener) {
        this.mImagesOfficeList = imagesOfficeList;
        this.context = context;
        this.homeItemListener = homeItemListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomeItemsAdapter.DetailsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_card, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        HomeItemsAdapter.DetailsViewHolder detailsViewHolder = (HomeItemsAdapter.DetailsViewHolder) holder;
        final Response response = mImagesOfficeList.get(position);
        if (response != null ) {
            ImageCacheHandler.getInstance(context).setImage(detailsViewHolder.imageView, response.getDepartmentID(), response.getImage());
            detailsViewHolder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    homeItemListener.itemClickListener(position,response);
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
