package com.example.moises.myapplication.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Moises on 3/10/2017.
 */

public class Area {

    public int id_Minor;
    public String name;
    public Map<String,Advertisement> ads;

    public Area() {
    }

    public Area(int id_minor, String name) {
        this.id_Minor = id_minor;
        this.name = name;
        ads = new HashMap<>();
    }

    public int getId_Minor() {
        return id_Minor;
    }

    public void setId_Minor(int id_Minor) {
        this.id_Minor = id_Minor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String,Advertisement> getAds() {
        return ads;
    }

    public void setAds(Map<String,Advertisement> ads) {
        this.ads = ads;
    }
}


