package com.ireport.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Text;
import com.ireport.R;
import com.ireport.model.AppContext;
import com.ireport.model.LocationDetails;
import com.ireport.model.ReportData;

import java.util.ArrayList;

public class ViewReportActivity extends AppCompatActivity {
    public static String TAG = "ViewReportActivity";
    Geocoder geocoder;

    private TextView mDescriptionTextView,mDateTimeTextView, mScreenNameTextView, mEmailTextView,
            mSizeTextView, mSeverityTextView, mLocationTextView;
    private Button mUpdateReportBtn;
    private RadioGroup radioGroupStatus;

    // for status logic
    private String litterStatus;

    AppContext ctx = AppContext.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "in view report activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);

        String itemIdiInMongo = getIntent().getStringExtra("report_id_in_mongo");
        Log.d(TAG, "Item's mongo id is: " + itemIdiInMongo);

        //TEst code, sandhya
        String images = getIntent().getStringExtra("images");
        String location_lat = getIntent().getStringExtra("location_lat");
        String location_long = getIntent().getStringExtra("location_long");
        String street_address = getIntent().getStringExtra("street_address");
        // END TEST


        //Set all assets
        mDescriptionTextView = (TextView) findViewById(R.id.litterDescTV);
        mDateTimeTextView = (TextView) findViewById(R.id.litterDateTimeTV);
        mScreenNameTextView = (TextView) findViewById(R.id.screenNameTV);
        mEmailTextView = (TextView) findViewById(R.id.emailTV);
        mSizeTextView = (TextView) findViewById(R.id.litterSizeTV);
        mSeverityTextView = (TextView) findViewById(R.id.litterSeverityTV);
        mLocationTextView = (TextView) findViewById(R.id.litterLocationTV);

        radioGroupStatus = (RadioGroup) findViewById(R.id.radio_group_status);
        mUpdateReportBtn = (Button) findViewById(R.id.update_report_button);


        //Sandhya : Test with sample input - Needs to be commented/cleaned out
        final ReportData testReportData = new ReportData();
        testReportData.setDescription("Cigarette Bulb");
        testReportData.setTimestamp("my timestamp");
        testReportData.setImages("sampleimage");
        testReportData.setSeverityLevel("Urgent");
        testReportData.setStatus("still_there");
        LocationDetails testLocation = new LocationDetails();
        testLocation.setLatitude(37.37310915);
        testLocation.setLongitude(-122.06109646);
        testReportData.setLocation(testLocation);
        testReportData.setStreetAddress(street_address);
        testReportData.setSize("Small");
        testReportData.setReporteeID("sandhyafeb1990@gmail.com");
        testReportData.setImages(images);
        // End test


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
                //Update the report
            }
        });

        // Decode images
        ArrayList<Bitmap> imageList = getImage(testReportData.getImages());
        Log.v(TAG, "Totally " + imageList.size() + " images");
        litterStatus = testReportData.getStatus();
        //Status
        if(litterStatus.equals("still_there")) {
            radioGroupStatus.check(R.id.radio_present);
        } else if (litterStatus.equals("removal_claimed")) {
            radioGroupStatus.check(R.id.radio_claimed);
        } else if(litterStatus.equals("removal_confirmed")) {
            radioGroupStatus.check(R.id.radio_confirmed);
        } else {
            Log.v(TAG, "Invalid status found!");
        }

        radioGroupStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                /* Get current location
                Location currLocation = new Location();
                Location reportLocation = new Location();

                CurrentLocationUtil.getCurrentLocation(ViewReportActivity.this, ctx);

                //set the latitude and longitude
                if(ctx.getCurrentLocation() != null) {
                    currLocation.setLatitude(ctx.getCurrentLocation().getLatitude());
                    currLocation.setLongitude(ctx.getCurrentLocation().getLongitude());
                }
                //Get report location from street address


                2.Check if it is <30ft
                float distance = currLocation.distanceTo(reportLocation);
                */


                // checkedId is the RadioButton selected
                String oldLitterStatus = litterStatus;
                String newLitterStatus = ((RadioButton) findViewById(radioGroupStatus.getCheckedRadioButtonId())).getText().toString();
                Log.v(TAG,"Old Status = " + oldLitterStatus + " New Status = " + newLitterStatus);

                float distance = 9.5f;
                if(distance > 9.144) {
                    Log.v(TAG,"User not within range!!");
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
                    testReportData.setStatus(litterStatus);
                    Log.v(TAG,"Updating status to " + litterStatus);
                }
            }
        });
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


}
