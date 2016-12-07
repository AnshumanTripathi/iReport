package com.ireport.activities;

/**
 * Created by Somya on 12/6/2016.
 */

public class ListActivityRowClass {

    private int imageId;
    private String description;
    private String streetAddress;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ListActivityRowClass(int imageId, String title, String desc, String status) {
        this.imageId = imageId;
        this.description = title;
        this.streetAddress = desc;
        this.status = status;
    }
    public int getImageId() {
        return imageId;
    }
    public void setImageId(int imageId) {
        this.imageId = imageId;
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
}

