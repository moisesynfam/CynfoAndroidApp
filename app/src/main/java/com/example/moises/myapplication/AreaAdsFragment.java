package com.example.moises.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.moises.myapplication.AdsDetailsActivity;

import com.example.moises.myapplication.Model.Area;
import com.example.moises.myapplication.Model.Business;


public class AreaAdsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    public AdsRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public Business FragmentBusiness;
    public int presentArea = 0;
    private ImageView areaImage;
    private TextView areaTitle;
    private RelativeLayout relativeLayout;
    public boolean loaded = false;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView textView;
    // TODO: Rename and change types of parameters
    private int AreaMinor;
    private int AreaMajor;



    public AreaAdsFragment() {
        // Required empty public constructor
    }




    // TODO: Rename and change types and number of parameters
    public static AreaAdsFragment newInstance() {
        AreaAdsFragment fragment = new AreaAdsFragment();
        Bundle args = new Bundle();
//        args.putInt(ARG_PARAM1, idMinor);
//        args.putInt(ARG_PARAM2, idMajor);






        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //AreaMajor = getArguments().getInt(ARG_PARAM2);
             FragmentBusiness = (Business) getArguments().getSerializable(ARG_PARAM1);

        }


    }

    public void ChangeAreaDataSet(int minor){


        for (Area a : FragmentBusiness.areas.values()){
            if(a.id_Minor == minor){



//                Glide.with(this).load(a.backgroundImage).asBitmap().centerCrop().into(new SimpleTarget<Bitmap>(relativeLayout.getWidth(), relativeLayout.getHeight()) {
//                    @Override
//                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                        Drawable drawable = new BitmapDrawable(resource);
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                            relativeLayout.setBackground(drawable);
//                        }
//                    }
//                });


                Glide.with(getContext())
                        .load(a.backgroundImage)
                        .centerCrop()
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(areaImage);

                areaTitle.setText(a.name);
                mAdapter.adsDataset =a.ads;
                presentArea = minor;
                mAdapter.getKeys();
                mAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

    //En caso de algun cambio;
    public void BusinessChange(Business business){
        FragmentBusiness = business;
        ChangeAreaDataSet(presentArea);





        Log.d("CYNFO FRAGMENT", "Fragment Business modified");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        View view =  inflater.inflate(R.layout.fragment_area_ads, container, false);

        Button b1 = (Button) view.findViewById(R.id.button1);



        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeAreaDataSet(1);
            }
        });


        TextView t = (TextView) view.findViewById(R.id.business_title);
        t.setText(FragmentBusiness.name);
        areaImage = (ImageView)view.findViewById(R.id.ads_area_imageview);
        areaTitle = (TextView) view.findViewById(R.id.ads_area_title);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.ads_area_image);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.ads_recycler_view);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new AdsRecyclerViewAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(),mRecyclerView,
                        new RecyclerItemClickListener.OnItemClickListener(){

                            @Override
                            public void onItemClick(View view, int position) {
                                Log.d("CYNFO ITEM", "Position TOUCHED "+position);

                                Intent intent = new Intent(getContext(), AdsDetailsActivity.class);
                                intent.putExtra("ad", mAdapter.ads.get(mAdapter.getItemCount()-1-position));
                                startActivity(intent);

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }
                        })
        );
        ChangeAreaDataSet(presentArea);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
