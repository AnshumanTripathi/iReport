package com.ireport.model;

/**
 * Created by Somya on 12/3/2016.
 */

public class LocationDetails {

    private double latitude;
    private double longitude;

    public LocationDetails() {

    }

    public LocationDetails(double lat, double lng) {
        this.latitude = lat;
        this.longitude = lng;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "{lat: " + Double.toString(this.getLatitude()) +
                ", lng: " + Double.toString(this.getLongitude()) +
                "}";
    }
}
