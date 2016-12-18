package com.ireport.model;

import java.util.ArrayList;

/**
 * Created by Somya on 11/28/2016.
 */
// Global app context to store the global app session data
public class AppContext {

    public static UserInfo currentLoggedInUser;
    private static LocationDetails currentLocation;
    //private static ReportData currentR
    private static AppContext instance = null;
    public ArrayList<ReportData> getCurrentUserReportsToShow() {
        return currentUserReportsToShow;
    }

    public void setCurrentUserReportsToShow(ArrayList<ReportData> currentUserReportsToShow) {
        this.currentUserReportsToShow = currentUserReportsToShow;
    }

    private static ArrayList<ReportData> currentUserReportsToShow;

    public static LocationDetails getCurrentLocation() {
        return currentLocation;
    }

    public static void setCurrentLocation(LocationDetails currentLocation) {
        AppContext.currentLocation = currentLocation;
    }

    private AppContext() {}

    public void reset() {
        currentLoggedInUser.resetInfo();
    }

    public static AppContext getInstance() {
        if(instance == null){
            return new AppContext();
        }else{
            return instance;
        }
    }

    public UserInfo getCurrentLoggedInUser() {
        return currentLoggedInUser;
    }

    public void setUserInfo(UserInfo info) {
        AppContext.currentLoggedInUser = info;
    }

    public static void setCurrentLoggedInUser(UserInfo newCurrentLoggedInUser) {
        currentLoggedInUser = newCurrentLoggedInUser;
    }
}
