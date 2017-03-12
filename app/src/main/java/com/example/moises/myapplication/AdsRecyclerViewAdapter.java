package com.example.moises.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.moises.myapplication.Model.Advertisement;

import java.util.ArrayList;

/**
 * Created by Moises on 3/8/2017.
 */

public class AdsRecyclerViewAdapter extends RecyclerView.Adapter<AdsRecyclerViewAdapter.ViewHolder> {


    private ArrayList<Advertisement> adsDataset;
    public Context mainContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public ImageView mImageView;
        public TextView mTextView;


        public ViewHolder(View v) {
            super(v);
            mImageView = (ImageView) v.findViewById(R.id.ad_display_image);
            mTextView = (TextView) v.findViewById(R.id.ad_title_text);


        }

    }
    public AdsRecyclerViewAdapter(ArrayList<Advertisement> ads, Context context){
        adsDataset = ads;
        mainContext = context;
    }

    @Override
    public AdsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ads_recycler_view_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(adsDataset.get(position).title);
        Glide.with(mainContext)
                .load(adsDataset.get(position).imageURL)
                .crossFade()
                .into(holder.mImageView);





    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return adsDataset.size();
    }
}
