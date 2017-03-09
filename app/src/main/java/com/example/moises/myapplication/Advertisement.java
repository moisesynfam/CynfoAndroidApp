package com.example.moises.myapplication;

/**
 * Created by Moises on 3/8/2017.
 */

public class Advertisement {

    private String title;
    private String description;
    private String imageURL;
    private int minor;
    private int major;

    public Advertisement(){

    }

    public Advertisement(String title, String description,String imageURL,int minor, int major){

        this.title = title;
        this.description = description;
        this.imageURL = imageURL;
        this.minor = minor;
        this.major = major;

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

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }
}
