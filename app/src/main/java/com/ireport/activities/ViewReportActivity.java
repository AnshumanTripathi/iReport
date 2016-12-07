package com.ireport.activities;

import com.ireport.R;
import com.ireport.model.LocationDetails;
import com.ireport.model.ReportData;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ViewReportActivity extends AppCompatActivity {
    public static String TAG = "ViewReportActivity";
    private TextView mDescriptionTextView,mLocationTextView;
    private TextView mDate;
    private Button mUpdateReportBtn;
    private RadioGroup radioGroupSize, radioGroupSeverity,radioGroupStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG,"in view report activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);


        //Set all assets
        mDescriptionTextView = (TextView) findViewById(R.id.user_litter_desc);
        mLocationTextView =(TextView) findViewById(R.id.user_litter_location);
        radioGroupSize = (RadioGroup) findViewById(R.id.radio_group_size);
        radioGroupSeverity = (RadioGroup) findViewById(R.id.radio_group_severity);
        radioGroupStatus = (RadioGroup) findViewById(R.id.radio_group_status);
        mDate = (TextView) findViewById(R.id.user_litter_date_time);
        mUpdateReportBtn = (Button) findViewById(R.id.update_report_button);


        //Sandhya : Test with sample input - Needs to be commented/cleaned out
        ReportData testReportData =  new ReportData();
        testReportData.setDescription("Cigarette Bulb");
        testReportData.setImages("sampleimage");
        testReportData.setSeverityLevel("Urgent");
        testReportData.setStatus("still_there");
        LocationDetails testLocation = new LocationDetails();
        testReportData.setLocation(testLocation);
        testReportData.setSize("Small");
        testReportData.setReporteeID("sandhyafeb1990@gmail.com");


        //Get report details from report data object

        //Get report data from the server

        //Set the description and date,time
        Log.v(TAG,"Test report description is " + testReportData.getDescription());
        mDescriptionTextView.setText(testReportData.getDescription());

        mDate.setText("blah blah");

        mLocationTextView.setText("Street address comes here!");
        //mDate.setText(String.valueOf(System.currentTimeMillis()));


        //Set the appropriate buttons to be checked - size,severity,status

        /*
        Log.v(TAG, ((RadioButton) findViewById(radioGroupSize.getCheckedRadioButtonId())).getText().toString());
        String litterSize = ((RadioButton) findViewById(radioGroupSize.getCheckedRadioButtonId())).getText().toString();
        if(litterSize.equals("Small"))
            radioGroupSize.check(R.id.radio_small);
        else {
            Log.v(TAG,"Unable to check the radio button to small");
        }
        */
        //Set the location of the report

        //Get images to be displayed

        mUpdateReportBtn = (Button) findViewById(R.id.update_report_button);
        mUpdateReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1. Get current location

                //2.Check if it is <30ft

                //3.If yes, update the status and send a request to the server

                //4.If not, show a toast

            }
        });
    }


}
