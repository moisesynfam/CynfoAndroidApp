package com.example.moises.myapplication.Data;

import java.util.ArrayList;

/**
 * Created by Moises on 3/10/2017.
 */

public class Business {

    public int id_Major;
    public String name;
    public ArrayList<Area> areas;


    public Business() {
    }

    public Business(int id_Major, String name) {
        this.id_Major = id_Major;
        this.name = name;
        this.areas = new ArrayList<>();
    }

    public int getId_Major() {
        return id_Major;
    }

    public void setId_Major(int id_Major) {
        this.id_Major = id_Major;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Area> getAreas() {
        return areas;
    }

    public void setAreas(ArrayList<Area> Areas) {
        this.areas = Areas;
    }
}


