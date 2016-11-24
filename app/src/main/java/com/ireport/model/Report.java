package com.ireport.model;

import android.location.Location;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Created by AnshumanTripathi on 11/24/16.
 */

public class Report {
    //Images
    private List<File> images;
    //User Location
    private Location location;
    private String description;
    //Area size
    private String size;
    //Severity Level
    private String severityLevel;
    private Date date;
    //Reporting User
    private User userReporting; //We can later change this to take only a specific user detail.

    public List<File> getImages() {
        return images;
    }

    public void setImages(List<File> images) {
        this.images = images;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSeverityLevel() {
        return severityLevel;
    }

    public void setSeverityLevel(String severityLevel) {
        this.severityLevel = severityLevel;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUserReporting() {
        return userReporting;
    }

    public void setUserReporting(User userReporting) {
        this.userReporting = userReporting;
    }
}