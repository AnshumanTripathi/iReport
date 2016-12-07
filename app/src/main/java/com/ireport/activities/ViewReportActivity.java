package com.ireport.activities;

import com.ireport.R;
import com.ireport.model.AppContext;
import com.ireport.model.LocationDetails;
import com.ireport.model.ReportData;

import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ViewReportActivity extends AppCompatActivity {
    public static String TAG = "ViewReportActivity";
    Geocoder geocoder;
    private TextView mDescriptionTextView,mLocationTextView;
    private TextView mDate;
    private Button mUpdateReportBtn;
    private RadioGroup radioGroupSize, radioGroupSeverity,radioGroupStatus;
    String litterStatus = "",litterSize = "", litterSeverity="";

    AppContext ctx = AppContext.getInstance();

    public ViewReportActivity() {
        ctx.statusChangedByApp = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "in view report activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);


        //Set all assets
        mDescriptionTextView = (TextView) findViewById(R.id.user_litter_desc);
        mLocationTextView = (TextView) findViewById(R.id.user_litter_location);
        radioGroupSize = (RadioGroup) findViewById(R.id.radio_group_size);
        radioGroupSeverity = (RadioGroup) findViewById(R.id.radio_group_severity);
        radioGroupStatus = (RadioGroup) findViewById(R.id.radio_group_status);
        mDate = (TextView) findViewById(R.id.user_litter_date_time);
        mUpdateReportBtn = (Button) findViewById(R.id.update_report_button);


        //Sandhya : Test with sample input - Needs to be commented/cleaned out
        final ReportData testReportData = new ReportData();
        testReportData.setDescription("Cigarette Bulb");
        testReportData.setImages("sampleimage");
        testReportData.setSeverityLevel("Urgent");
        testReportData.setStatus("still_there");
        final LocationDetails testLocation = new LocationDetails();
        testReportData.setLocation(testLocation);
        testReportData.setSize("Small");
        testReportData.setReporteeID("sandhyafeb1990@gmail.com");


        //Get report details from report data object

        //Get report data from the server

        //Set the description and date,time
        Log.v(TAG, "Test report description is " + testReportData.getDescription());
        mDescriptionTextView.setText(testReportData.getDescription());

        mDate.setText("blah blah");

        mLocationTextView.setText("Street address comes here!");
        //mDate.setText(String.valueOf(System.currentTimeMillis()));


        //Set the appropriate buttons to be checked - size,severity,status
        Log.v(TAG, "Litter Size " + testReportData.getSize());
        Log.v(TAG, "Litter severity " + testReportData.getSeverityLevel());

        litterSize = testReportData.getSize();
        litterSeverity = testReportData.getSeverityLevel();
        litterStatus = testReportData.getStatus();

        //Size
        if (litterSize.equals("Small") || litterSize.equals("small")) {
            radioGroupSize.check(R.id.radio_small);
        } else if (litterSize.equals("Medium") || litterSize.equals("medium")) {
            radioGroupSize.check(R.id.radio_medium);
        } else if (litterSize.equals("Large") || litterSize.equals("large")) {
            radioGroupSize.check(R.id.radio_large);
        } else if (litterSize.equals("xl")) {
            radioGroupSize.check(R.id.radio_xl);
        } else {
            Log.v(TAG, "Unable to check the radio button to small");
        }


        //Severity
        if (litterSeverity.equals("Minor") || litterSeverity.equals("minor")) {
            radioGroupSeverity.check(R.id.radio_severity_minor);
        } else if (litterSeverity.equals("Medium") || litterSeverity.equals("medium")) {
            radioGroupSeverity.check(R.id.radio_severity_medium);
        } else if (litterSeverity.equals("Urgent") || litterSeverity.equals("urgent")) {
            radioGroupSeverity.check(R.id.radio_severity_urgent);
        } else {
            Log.v(TAG, "Unable to check the radio button to small");
        }

        //Status
        if(litterStatus.equals("still_there")) {
            radioGroupStatus.check(R.id.radio_present);
        } else if (litterStatus.equals("removal_claimed")) {
            radioGroupStatus.check(R.id.radio_claimed);
        } else if(litterStatus.equals("removal_confirmed")) {
            radioGroupStatus.check(R.id.radio_confirmed);
        } else {
            Log.v(TAG,"Invalid status found!");
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
                    testReportData.setStatus(litterStatus);
                    litterStatus = newLitterStatus;
                    Log.v(TAG,"Updating status to " + litterStatus);
                }

            }
        });

        //Set the location of the report


         mUpdateReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Update the report

            }
        });

    }


    public void onRadioButtonClickeradd(View view) {
        Toast.makeText(getBaseContext(), "User can't edit size/severity values!", Toast.LENGTH_SHORT).show();
    }

}
