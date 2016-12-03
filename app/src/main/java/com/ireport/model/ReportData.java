package com.ireport.model;

import android.location.Location;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Created by AnshumanTripathi on 11/24/16.
 */

public class ReportData {
    //Images
    private String images;

    //UserInfo Location
    private LocationDetails location;
    private String description;

    //Area size
    private String size;

    //Severity Level
    private String severityLevel;

    private String reporteeID;      // the userID who reported it

    public void ReportData() {

    }

    public ReportData(
            String email,
            String images,
            String description,
            String size,
            String severityLevel,
            LocationDetails location
    ) {
        this.reporteeID = email;
        this.images = images;
        this.description = description;
        this.size = size;
        this.severityLevel = severityLevel;
        this.location = location;
    }


    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public LocationDetails getLocation() {
        return location;
    }

    public void setLocation(LocationDetails location) {
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

    public String getReporteeID() {
        return reporteeID;
    }

    public void setReporteeID(String reporteeID) {
        this.reporteeID = reporteeID;
    }
}