package com.example.moises.myapplication.Data;

import com.example.moises.myapplication.Model.Advertisement;

import java.util.Comparator;

/**
 * Created by Moises on 3/12/2017.
 */

public class AdsPriorityComparator implements Comparator<Advertisement> {
    @Override
    public int compare(Advertisement o1, Advertisement o2) {
        return o2.priority - o1.priority;
    }
}
