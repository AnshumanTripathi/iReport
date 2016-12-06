package com.ireport.model;

/**
 * Created by Somya on 11/28/2016.
 */
// Global app context to store the global app session data
public class AppContext {

    public static UserInfo currentLoggedInUser;
    private static LocationDetails currentLocation;
    private static AppContext instance;

    public static LocationDetails getCurrentLocation() {
        return currentLocation;
    }

    public static void setCurrentLocation(LocationDetails currentLocation) {
        AppContext.currentLocation = currentLocation;
    }

    public void reset() {
        currentLoggedInUser.resetInfo();
    }


    public static AppContext getInstance() {
        return new AppContext();
    }

    public static void setInstance(AppContext instance) {
        AppContext.instance = instance;
    }
}
