package com.ireport.activities;

import android.graphics.Bitmap;

/**
 * Created by Somya on 12/6/2016.
 */

public class ListActivityRowClass {

    private Bitmap thumbnail;
    private String description;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    private String dateTime;
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
        this.dateTime = desc;
        this.status = status;
        this.id = id;
    }
    public Bitmap getImageId() {
        return thumbnail;
    }
    public void setImageId(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }


    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public String toString() {
        return getDescription() + "\n" + getDateTime();
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}

