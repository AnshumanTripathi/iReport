package com.ireport.activities;

import com.ireport.R;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewReportActivity extends AppCompatActivity {
    public static String TAG = "ViewReportActivity";
    private TextView mDescriptionTextView;
    private TextView mDate;
    private Button mUpdateReportBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG,"in view report activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);

        //Get report details from report data object

        //Get report data from the server
        //ReportData reportData =

        //Set the description and date,time

        //Set the appropriate buttons to be checked - size,severity,status

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
