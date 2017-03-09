package com.example.moises.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class AreaAdsFragment extends Fragment {


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


        View view =  inflater.inflate(R.layout.fragment_area_ads, container, false);

        textView = (TextView) view.findViewById(R.id.fragment_text);
        textView.setText("You are in the area "+ AreaMinor);
        return view;
    }



}
