package com.ireport.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Geocoder;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Text;
import com.ireport.R;
import com.ireport.controller.utils.httpUtils.APIHandlers.GetReportByIdHandler;
import com.ireport.controller.utils.httpUtils.APIHandlers.UpdateReportByIdHandler;
import com.ireport.controller.utils.httpUtils.APIHandlers.UpdateSettingsHandler;
import com.ireport.controller.utils.locationUtils.CurrentLocationUtil;
import com.ireport.model.AppContext;
import com.ireport.model.LocationDetails;
import com.ireport.model.ReportData;

import java.util.ArrayList;

public class ViewReportActivity extends AppCompatActivity implements ICallbackActivity {
    public static String TAG = "ViewReportActivity";
    Geocoder geocoder;

    private TextView mDescriptionTextView,mDateTimeTextView, mScreenNameTextView, mEmailTextView,
            mSizeTextView, mSeverityTextView, mLocationTextView;
    private FloatingActionButton mUpdateReportBtn;
    private RadioGroup radioGroupStatus;
    private LinearLayout mImageLayout;

    private RadioGroup.OnCheckedChangeListener onCheckedChangeListener;

    // for status logic
    private ReportData reportData;
    private String litterStatus;

    // handlers
    private UpdateReportByIdHandler updateReportByIdHandler;
    private GetReportByIdHandler getReportByIdHandler;

    AppContext ctx = AppContext.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "in view report activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);

        Toolbar toolbar = (Toolbar) findViewById(R.id.viewReportToolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detailed Report");

        String itemIdiInMongo = getIntent().getStringExtra("report_id_in_mongo");
        Log.d(TAG, "Item's mongo id is: " + itemIdiInMongo);
        getReportByIdHandler = new GetReportByIdHandler(this, "view_report_activity", itemIdiInMongo);
        getReportByIdHandler.getReportForReportId(getApplicationContext());


        //Set all assets
        mDescriptionTextView = (TextView) findViewById(R.id.litterDescTV);
        mDateTimeTextView = (TextView) findViewById(R.id.litterDateTimeTV);
        mScreenNameTextView = (TextView) findViewById(R.id.screenNameTV);
        mEmailTextView = (TextView) findViewById(R.id.emailTV);
        mSizeTextView = (TextView) findViewById(R.id.litterSizeTV);
        mSeverityTextView = (TextView) findViewById(R.id.litterSeverityTV);
        mLocationTextView = (TextView) findViewById(R.id.litterLocationTV);

        radioGroupStatus = (RadioGroup) findViewById(R.id.radio_group_status);
        mUpdateReportBtn = (FloatingActionButton) findViewById(R.id.update_report_button);

        mImageLayout = (LinearLayout)findViewById(R.id.image_layout);


        onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                Get current location
                Location currLocation = new Location("");
                Location reportLocation = new Location("");


                Float distance = 0f;

                if(ctx.getCurrentLocation() == null) {
                    Toast.makeText(getBaseContext(), "Could not get the user's current location. Retrying....", Toast.LENGTH_SHORT).show();
                    CurrentLocationUtil.getCurrentLocation(ViewReportActivity.this,ctx);
                    return;
                } else if (reportLocation.equals(null)) {
                    Toast.makeText(getBaseContext(), "Could not get report location", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    float[] dist = new float[1];
                    currLocation.setLatitude(ctx.getCurrentLocation().getLatitude());
                    currLocation.setLongitude(ctx.getCurrentLocation().getLongitude());
                    reportLocation.setLatitude(reportData.getLocation().getLatitude());
                    reportLocation.setLongitude(reportData.getLocation().getLongitude());

                    distance = currLocation.distanceTo(reportLocation) * 0.328084f;
                    Log.v(TAG,"Setting distance to " + distance);

                }




                // checkedId is the RadioButton selected
                String oldLitterStatus = litterStatus;
                String newLitterStatus = ((RadioButton) findViewById(radioGroupStatus.getCheckedRadioButtonId())).getText().toString();
                Log.v(TAG,"Old Status = " + oldLitterStatus + " New Status = " + newLitterStatus);


                if(distance > 30f) {
                    Log.v(TAG,"User not within range!!");
                    Log.v(TAG,"Distance = " + distance);
                    Toast.makeText(getBaseContext(), "User must be within 30ft of radius from the posted location!", Toast.LENGTH_SHORT).show();
                    radioGroupStatus.setOnCheckedChangeListener(null);
                    if(oldLitterStatus.equals("still_there")) {
                        Log.v(TAG,"still_there im here");
                        radioGroupStatus.check(R.id.radio_present);
                    } else if (oldLitterStatus.equals("removal_claimed")) {
                        Log.v(TAG,"removal_claimed im here");
                        radioGroupStatus.check(R.id.radio_claimed);
                    } else if (oldLitterStatus.equals("removal_confirmed")) {
                        Log.v(TAG,"removal_confirmed im here");
                        radioGroupStatus.check(R.id.radio_confirmed);
                    }
                    radioGroupStatus.setOnCheckedChangeListener(this);
                } else {
                    litterStatus = newLitterStatus;
                    reportData.setStatus(litterStatus);
                    Log.v(TAG,"Updating status to " + litterStatus);
                }
            }
        };
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
        }
    }

    public void updatePageWithNewData(ReportData testReportData) {
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
                updateReportByIdHandler = new UpdateReportByIdHandler(ViewReportActivity.this,
                        "view_report_activity", reportData.getReportId(), reportData.getStatus());
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
        if(litterStatus.equals("still_there")) {
            radioGroupStatus.check(R.id.radio_present);
        } else if (litterStatus.equals("removal_claimed")) {
            radioGroupStatus.check(R.id.radio_claimed);
        } else if(litterStatus.equals("removal_confirmed")) {
            radioGroupStatus.check(R.id.radio_confirmed);
        } else {
            Log.v(TAG, "Invalid status found!");
        }

        radioGroupStatus.setOnCheckedChangeListener(onCheckedChangeListener);

    }
}
