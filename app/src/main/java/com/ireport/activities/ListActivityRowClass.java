package com.ireport.activities;

import android.graphics.Bitmap;

/**
 * Created by Somya on 12/6/2016.
 */

public class ListActivityRowClass {

    private Bitmap thumbnail;
    private String description;
    private String streetAddress;
    private String status;

    private String id;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ListActivityRowClass(Bitmap thumbnail, String title, String desc, String status,String id) {
        this.thumbnail = thumbnail;
        this.description = title;
        this.streetAddress = desc;
        this.status = status;
        this.id = id;
    }
    public Bitmap getImageId() {
        return thumbnail;
    }
    public void setImageId(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }
    public String getStreetAddress() {
        return streetAddress;
    }
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public String toString() {
        return description + "\n" + streetAddress;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}

