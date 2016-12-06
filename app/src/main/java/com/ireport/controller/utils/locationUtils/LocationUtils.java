package com.ireport.controller.utils.locationUtils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.ireport.controller.utils.Constants;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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

    /*
    For ctx pass application context
     */
    public String getAddress(Context ctx, double lat,double long1) {
        Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
        String retAddress = null;

        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat, long1, 1);

            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                retAddress = returnedAddress.getAddressLine(0).toString();
                Log.d("Address is :", retAddress);
            } else {
                Log.d("No address", "No Address returned!");
            }

        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return retAddress;
    }


}
