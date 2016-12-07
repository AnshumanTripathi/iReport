package com.ireport.activities;

/**
 * Created by Somya on 12/6/2016.
 */

public class ListActivityRowClass {

    private int imageId;
    private String title;
    private String desc;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ListActivityRowClass(int imageId, String title, String desc, String status) {
        this.imageId = imageId;
        this.title = title;
        this.desc = desc;
        this.status = status;
    }
    public int getImageId() {
        return imageId;
    }
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    @Override
    public String toString() {
        return title + "\n" + desc;
    }
}

