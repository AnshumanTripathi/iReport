package com.ireport.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ireport.R;
import com.ireport.controller.utils.httpUtils.APIHandlers.GetReportByIdHandler;
import com.ireport.controller.utils.httpUtils.APIHandlers.UpdateReportByIdHandler;
import com.ireport.controller.utils.locationUtils.CurrentLocationUtil;
import com.ireport.model.AppContext;
import com.ireport.model.ReportData;

import java.util.ArrayList;

public class ViewReportActivity extends AppCompatActivity implements ICallbackActivity,
        RadioGroup.OnCheckedChangeListener {
    public static String TAG = "ViewReportActivity";
    Geocoder geocoder;

    private TextView mDescriptionTextView,mDateTimeTextView, mScreenNameTextView, mEmailTextView,
            mSizeTextView, mSeverityTextView, mLocationTextView;
    private FloatingActionButton mUpdateReportBtn;
    private RadioGroup radioGroupStatus;
    private LinearLayout mImageLayout;

    private RadioGroup.OnCheckedChangeListener onCheckedChangeListener;

    // for status logic
    private ReportData reportData = null;
    private String litterStatus;

    // handlers
    private UpdateReportByIdHandler updateReportByIdHandler;
    private GetReportByIdHandler getReportByIdHandler;

    private ProgressDialog progressDialog;

    AppContext ctx = AppContext.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "in view report activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Report Details from Server");
        Toolbar toolbar = (Toolbar) findViewById(R.id.viewReportToolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detailed Report");

        String itemIdiInMongo = getIntent().getStringExtra("report_id_in_mongo");
        Log.d(TAG, "Item's mongo id is: " + itemIdiInMongo);
        radioGroupStatus = (RadioGroup) findViewById(R.id.radio_group_status);
        radioGroupStatus.setEnabled(false);

        if (!ctx.getCurrentLoggedInUser().isOfficial()) {
            CurrentLocationUtil.getCurrentLocation(ViewReportActivity.this,ctx);
        }

        getReportByIdHandler = new GetReportByIdHandler(
                this,
                "view_report_activity",
                itemIdiInMongo
        );


        progressDialog.show();
        getReportByIdHandler.getReportForReportId(getApplicationContext());


        //Set all assets
        mDescriptionTextView = (TextView) findViewById(R.id.litterDescTV);
        mDateTimeTextView = (TextView) findViewById(R.id.litterDateTimeTV);
        mScreenNameTextView = (TextView) findViewById(R.id.screenNameTV);
        mEmailTextView = (TextView) findViewById(R.id.emailTV);
        mSizeTextView = (TextView) findViewById(R.id.litterSizeTV);
        mSeverityTextView = (TextView) findViewById(R.id.litterSeverityTV);
        mLocationTextView = (TextView) findViewById(R.id.litterLocationTV);

        mUpdateReportBtn = (FloatingActionButton) findViewById(R.id.update_report_button);

        mImageLayout = (LinearLayout)findViewById(R.id.image_layout);

        onCheckedChangeListener = this;
    }

    private float getReportDistance(ReportData data) {
        Float distance = 0f;

        //Get current location
        Location currLocation = new Location("");
        Location reportLocation = new Location("");

        float[] dist = new float[1];

        if(ctx.getCurrentLocation() != null) {
            currLocation.setLatitude(ctx.getCurrentLocation().getLatitude());
            currLocation.setLongitude(ctx.getCurrentLocation().getLongitude());


            reportLocation.setLatitude(data.getLocation().getLatitude());
            reportLocation.setLongitude(data.getLocation().getLongitude());

            distance = currLocation.distanceTo(reportLocation) * 0.328084f;
            return distance;

        } else {
            return -1;
        }

    }

    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(ctx.getCurrentLocation() == null && !ctx.getCurrentLoggedInUser().isOfficial()) {
            CurrentLocationUtil.getCurrentLocation(ViewReportActivity.this,ctx);
        }
        if (reportData == null) {        // redundant check
            Toast.makeText(getBaseContext(),
                    "Could not get report Data",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        // checkedId is the RadioButton selected
        String oldLitterStatus = litterStatus;
        String newLitterStatus = ((RadioButton) findViewById(radioGroupStatus.
                getCheckedRadioButtonId())).
                getText().
                toString();
        Log.v(TAG, "Old Status = " + oldLitterStatus + " New Status = " + newLitterStatus);

        if (ctx.getCurrentLoggedInUser().isOfficial()) {
            litterStatus = newLitterStatus;
            reportData.setStatus(litterStatus);
            Log.v(TAG, "Updating status to " + litterStatus);

        } else {
            // user case
            float distance = this.getReportDistance(this.reportData);
            Log.v(TAG, "Setting distance to " + distance);

            if (distance > 30f || distance == -1) {
                if(distance > 30f) {
                    Toast.makeText(
                            getBaseContext(),
                            "You must be within 30ft of the reported location to update the report",
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    Toast.makeText(
                            getBaseContext(),
                            "Unable to get Current Location",
                            Toast.LENGTH_SHORT
                    ).show();
                }

                radioGroupStatus.setOnCheckedChangeListener(null);

                if (oldLitterStatus.equals("still_there")) {
                    Log.v(TAG, "still_there im here");
                    radioGroupStatus.check(R.id.radio_present);
                } else if (oldLitterStatus.equals("removal_claimed")) {
                    Log.v(TAG, "removal_claimed im here");
                    radioGroupStatus.check(R.id.radio_claimed);
                } else if (oldLitterStatus.equals("removal_confirmed")) {
                    Log.v(TAG, "removal_confirmed im here");
                    radioGroupStatus.check(R.id.radio_confirmed);
                }
                radioGroupStatus.setOnCheckedChangeListener(this);
            } else {
                litterStatus = newLitterStatus;
                reportData.setStatus(litterStatus);
                Log.v(TAG, "Updating status to " + litterStatus);
            }
        }
    }


    private ArrayList<Bitmap> getImage(String imageString) {
        String ResponseImageArrayString[] = imageString.split(",");
        ArrayList<Bitmap> responseImages = new ArrayList<>();
        for (int i=0; i<ResponseImageArrayString.length; i++) {
            byte[] decodedString = Base64.decode(ResponseImageArrayString[i].getBytes(), Base64.DEFAULT);
            Bitmap responseimage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            responseImages.add(responseimage);
        }
        Log.v(TAG, "Returning " + Integer.toString(responseImages.size()) + " images");
        return responseImages;
    }

    @Override
    public void onPostProcessCompletion(Object responseObj, String identifier, boolean isSuccess) {
        Log.d("VIEWING", "We have something");
        if (responseObj instanceof ReportData) {
            Log.d("VIEWING", "We have a report!!!!");
            updatePageWithNewData((ReportData) responseObj);
            progressDialog.dismiss();
        }
    }

    public void updatePageWithNewData(ReportData testReportData) {
        radioGroupStatus.setEnabled(true);
        reportData = testReportData;
        // Load description, datetime, screenname, email, size, severity and location
        mDescriptionTextView.setText(testReportData.getDescription());
        mDateTimeTextView.setText(testReportData.getTimestamp());
        mScreenNameTextView.setText(testReportData.getReporteeID());
        mEmailTextView.setText(testReportData.getReporteeID());
        mSizeTextView.setText(testReportData.getSize());
        mSeverityTextView.setText(testReportData.getSeverityLevel());
        mLocationTextView.setText(testReportData.getStreetAddress());

        mUpdateReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateReportByIdHandler = new UpdateReportByIdHandler(
                        ViewReportActivity.this,
                        "view_report_activity",
                        reportData.getReportId(),
                        reportData.getStatus()
                );
                updateReportByIdHandler.updateReportForReportId(getApplicationContext());
                Intent upIntent = getParentActivityIntent();
                startActivity(upIntent);
            }
        });

        // Decode images
        ArrayList<Bitmap> imageList = getImage(testReportData.getImages());
        Log.v(TAG, "Totally " + imageList.size() + " images");

        /////////////////////////////////////////////
        //Show images taken from camera in linear layout

        if(imageList.size() > 0) {
            mImageLayout.removeAllViews();
            for (int i = 0; i < imageList.size(); i++) {
                Bitmap yourbitmap = imageList.get(i);
                ImageView imageView = new ImageView(this);
                imageView.setImageBitmap(yourbitmap);
                imageView.setAdjustViewBounds(true);
                ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(mImageLayout.getLayoutParams());
                params.setMargins(10, 10, 10, 10);
                imageView.setPadding(10, 10, 10, 10);
                mImageLayout.addView(imageView, params);
            }
        }
        /////////////////////////////////////////////

        //Status
        litterStatus = testReportData.getStatus();
        boolean is_official = ctx.getCurrentLoggedInUser().isOfficial();
        switch (litterStatus) {
            case "still_there":
                radioGroupStatus.check(R.id.radio_present);
                if (is_official) {
                    radioGroupStatus.getChildAt(2).setEnabled(false);
                } else {
                    radioGroupStatus.getChildAt(1).setEnabled(false);
                }
                break;
            case "removal_claimed":
                radioGroupStatus.check(R.id.radio_claimed);
                if (is_official) {
                    radioGroupStatus.getChildAt(0).setEnabled(false);
                    radioGroupStatus.getChildAt(2).setEnabled(false);
                }
                break;
            case "removal_confirmed":
                radioGroupStatus.check(R.id.radio_confirmed);
                radioGroupStatus.getChildAt(0).setEnabled(false);
                radioGroupStatus.getChildAt(1).setEnabled(false);
                break;
            default:
                Log.v(TAG, "Invalid status found!");
        }

        radioGroupStatus.setOnCheckedChangeListener(onCheckedChangeListener);
    }


    // Make it modular
    private static final int ACCESS_COARSE_LOCATION = 1;
    /*
       These functions will help in fetching the current location for the user.
       This one checks for GPS Permission
     */
    public void checkGPSPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_COARSE_LOCATION
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults
    ) {
        LocationManager gpsStatus = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        switch (requestCode) {
            case ACCESS_COARSE_LOCATION:
                if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (!gpsStatus.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        android.app.AlertDialog.Builder builder = new
                                android.app.AlertDialog.Builder(this);
                        builder.setMessage("GPS is disabled. Enable for Location service? ")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(@SuppressWarnings("unused")
                                                                DialogInterface dialog,
                                                        @SuppressWarnings("unused") int which) {
                                        startActivity(
                                                new Intent(
                                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS
                                                )
                                        );
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                        android.app.AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                } else {
                    Toast.makeText(getBaseContext(),
                            "App requries Location to perform all Features",
                            Toast.LENGTH_SHORT).show();
                    try {
                        ctx.getCurrentLocation().setLatitude(gpsStatus.getLastKnownLocation(
                                LocationManager.GPS_PROVIDER).
                                getLatitude());
                        ctx.getCurrentLocation().
                                setLongitude(
                                        gpsStatus.getLastKnownLocation
                                                (
                                                        LocationManager.GPS_PROVIDER).
                                                getLongitude());
                    } catch (SecurityException permissionException) {
                        Toast.makeText(getBaseContext(),
                                "Exception in Fetching last known location",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

}
