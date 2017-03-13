package com.example.moises.myapplication.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Moises on 3/10/2017.
 */

public class Business{

    public int id_Major;
    public String name;
    public Map<String,Area> areas;


    public Business() {
    }

    public Business(int id_Major, String name) {
        this.id_Major = id_Major;
        this.name = name;
        this.areas = new HashMap<>();
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

    public Map<String,Area>  getAreas() {
        return areas;
    }

    public void setAreas(Map<String,Area> Areas) {
        this.areas = Areas;
    }
    public Set GetAreasKeySet(){
        return areas.keySet();

    }

}


