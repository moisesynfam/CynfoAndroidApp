package com.example.moises.myapplication.Model;

/**
 * Created by Moises on 3/8/2017.
 */

public class Advertisement {

    public  String title;
    public String description;
    public String imageURL;


    public Advertisement() {

    }

    public Advertisement(String title, String description, String imageURL) {

        this.title = title;
        this.description = description;
        this.imageURL = imageURL;


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