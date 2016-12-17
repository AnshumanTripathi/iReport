package com.ireport.controller.utils.locationUtils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.ireport.activities.CreateReportActivity;
import com.ireport.activities.ViewReportActivity;
import com.ireport.controller.utils.GenericToastManager;
import com.ireport.model.AppContext;
import com.ireport.model.LocationDetails;

/**
 * Created by Somya on 12/5/2016.
 */

public class CurrentLocationUtil {

    static Location userLastLocation;
    private static final int ACCESS_COARSE_LOCATION = 1;

    public static void getCurrentLocation(
            AppCompatActivity appActivity,
            AppContext ctx
    ) {

        LocationManager locationManager = (LocationManager) appActivity.getSystemService(
                Context.LOCATION_SERVICE);
        boolean isGpsOn = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkOn = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        try {

            final LocationDetails currentLocation = new LocationDetails();
            LocationListener listener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    userLastLocation = location;
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };
            if (isGpsOn) {
                userLastLocation = locationManager.getLastKnownLocation(
                        LocationManager.GPS_PROVIDER);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);

                if (userLastLocation != null) {
                    currentLocation.setLatitude(userLastLocation.getLatitude());
                    currentLocation.setLongitude(userLastLocation.getLongitude());
                    ctx.setCurrentLocation(currentLocation);
                    Log.d("LAT:",String.valueOf(userLastLocation.getLatitude()));
                    Log.d("LNG:", String.valueOf(userLastLocation.getLongitude()));
                } else {
                    GenericToastManager.showGenericMsg(
                            appActivity.getBaseContext(),
                            "Failed to get user location even though gps is on"
                    );
                }
            } else if (isNetworkOn) {
                String bestProvider = locationManager.getBestProvider(new Criteria(), false);
                Location location = locationManager.getLastKnownLocation(bestProvider);
                if (location != null) {
                    currentLocation.setLatitude(location.getLatitude());
                    currentLocation.setLongitude(location.getLongitude());
                    ctx.setCurrentLocation(currentLocation);
                    Log.d("LAT:",String.valueOf(userLastLocation.getLatitude()));
                    Log.d("LNG:", String.valueOf(userLastLocation.getLongitude()));
                    GenericToastManager.showGenericMsg(
                            appActivity.getBaseContext(),
                            currentLocation.toString()
                    );
                } else {
                    GenericToastManager.showGenericMsg(
                            appActivity.getBaseContext(),
                            "Failed to get user location. even though network is on"
                    );
                }
            } else {
                GenericToastManager.showGenericMsg(
                        appActivity.getBaseContext(),
                        "Cannot get Location! Check Network or GPS."
                );
            }
        } catch (Exception e) {
            System.out.println("Exception while getting user location: " + e.getMessage());
            GenericToastManager.showGenericMsg(
                    appActivity.getBaseContext(),
                    "Location permission might be missing. Check GPS."
            );
            if(appActivity instanceof CreateReportActivity) {
                ((CreateReportActivity)appActivity).checkGPSPermission();
            } else if(appActivity instanceof ViewReportActivity) {
                ((ViewReportActivity)appActivity).checkGPSPermission();
            }
        }
    }
}
