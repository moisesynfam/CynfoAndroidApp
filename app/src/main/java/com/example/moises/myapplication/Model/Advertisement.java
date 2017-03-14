package com.example.moises.myapplication.Model;

import java.io.Serializable;

/**
 * Created by Moises on 3/8/2017.
 */

public class Advertisement implements Serializable{

    public  String title;
    public String description;
    public String imageURL;
    public int id;
    public int priority;


    public Advertisement() {

    }

    public Advertisement(int id, String title, String description, String imageURL, int priority) {

        this.title = title;
        this.description = description;
        this.imageURL = imageURL;
        this.id = id;
        this.priority = priority;


    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}