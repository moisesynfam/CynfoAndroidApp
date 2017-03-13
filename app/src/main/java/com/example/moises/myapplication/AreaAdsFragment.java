package com.example.moises.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.moises.myapplication.Model.Advertisement;
import com.example.moises.myapplication.Model.Area;
import com.example.moises.myapplication.Model.Business;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Set;


public class AreaAdsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    public AdsRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public Business FragmentBusiness;
    public int presentArea = 0;


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

                mAdapter.adsDataset =a.ads;
                presentArea = minor;
                mAdapter.getKeys();
                mAdapter.notifyDataSetChanged();
                break;
            }
        }
    }

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
        Button b2 = (Button) view.findViewById(R.id.button2);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeAreaDataSet(1);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeAreaDataSet(2);
            }
        });
        mRecyclerView = (RecyclerView) view.findViewById(R.id.ads_recycler_view);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new AdsRecyclerViewAdapter(FragmentBusiness.areas.get("qwasdmc").ads,getContext());
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }






}
