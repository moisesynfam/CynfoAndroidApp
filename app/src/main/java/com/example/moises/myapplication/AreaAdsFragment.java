package com.example.moises.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.moises.myapplication.Data.Advertisement;
import com.example.moises.myapplication.Data.Area;
import com.example.moises.myapplication.Data.Business;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class AreaAdsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private AdsRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


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
    public static AreaAdsFragment newInstance(int idMinor, int idMajor) {
        AreaAdsFragment fragment = new AreaAdsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, idMinor);
        args.putInt(ARG_PARAM2, idMajor);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            AreaMajor = getArguments().getInt(ARG_PARAM2);
            AreaMinor = getArguments().getInt(ARG_PARAM1);






        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment




        ArrayList<Advertisement> ads = new ArrayList<Advertisement>();


        View view =  inflater.inflate(R.layout.fragment_area_ads, container, false);
        for (int i = 0; i <10 ; i++){
            Advertisement ad1 = new Advertisement("Title","lol","http://webneel.com/daily/sites/default/files/images/project/creative-advertisement%20(13).jpg");
            ads.add(ad1);
        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.ads_recycler_view);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new AdsRecyclerViewAdapter(ads,getContext());
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }




}
