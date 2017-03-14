package com.example.moises.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.moises.myapplication.Model.Advertisement;

/**
 * Created by Moises on 3/13/2017.
 */

public class AdsBottomSheetFragment extends BottomSheetDialogFragment {

    public Advertisement advertisement;
    private TextView title;
    private TextView description;
    private ImageView image;

    public AdsBottomSheetFragment() {

    }

    public static AdsBottomSheetFragment newInstance() {
        AdsBottomSheetFragment f = new AdsBottomSheetFragment();
        Bundle args = new Bundle();

        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_modal, container, false);
        title = (TextView) v.findViewById(R.id.bttm_title);
        description = (TextView) v.findViewById(R.id.bttm_description);
        image = (ImageView) v.findViewById(R.id.bttm_image);

        if(advertisement!=null){

            title.setText(advertisement.title);
            description.setText(advertisement.description);
            Glide.with(getContext())
                    .load(advertisement.imageURL)
                    .crossFade()

                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(image);


        }

        return v;
    }
}
