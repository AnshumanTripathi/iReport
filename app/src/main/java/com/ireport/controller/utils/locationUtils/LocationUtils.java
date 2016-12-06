package com.ireport.controller.utils.locationUtils;

import android.location.Location;
import android.provider.SyncStateContract;

import com.ireport.controller.utils.Constants;

/**
 * Created by Somya on 12/5/2016.
 */

public class LocationUtils {

    public double getDistanceBetweenTwoPoints(
            double lat1,
            double lng1,
            double lat2,
            double lng2
    ) {
        Location loc1 = new Location("");
        loc1.setLatitude(lat1);
        loc1.setLongitude(lng1);

        Location loc2 = new Location("");
        loc2.setLatitude(lat2);
        loc2.setLongitude(lng2);

        float distanceInMeters = loc1.distanceTo(loc2);
        double distanceInFeet = distanceInMeters * 3.28084;

        return distanceInFeet;

    }

    public boolean checkIfWithin30Feet(float distance) {
        if(distance <= Constants.DISTANCE_TO_UPDATE_REPORT)
            return true;
        else return false;
    }
}
