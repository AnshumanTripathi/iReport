package com.ireport.model;

import android.location.Location;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Created by AnshumanTripathi on 11/24/16.
 */

public class ReportData {

    private String images;
    private LocationDetails location;
    private String description;
    private String size;
    private String severityLevel;
    private String reporteeID;
    private String status;
    private String streetAddress;
    private String reportId;
    private String timestamp;

    public ReportData() {

    }

    public ReportData(
            String email,
            String images,
            String description,
            String size,
            String severityLevel,
            LocationDetails location,
            String streetAddress,
            String reportid,
            String timestamp
    ) {
        this.reporteeID = email;
        this.images = images;
        this.description = description;
        this.size = size;
        this.severityLevel = severityLevel;
        this.location = location;
        this.streetAddress = streetAddress;
        this.reportId =  reportid;
        this.timestamp =  timestamp;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String toString() {
        String ans = "";
        ans += this.reporteeID + " -> " +
                this.description + ", " +
                this.size + ", " +
                this.severityLevel + ", " +
                this.location + "," +
                this.streetAddress;
        return ans;
    }


}