package com.ireport.activities;

import com.ireport.R;
import com.ireport.controller.utils.httpUtils.APIHandlers.AddReportHandler;
import com.ireport.model.LocationDetails;
import com.ireport.model.ReportData;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class CreateReportActivity extends AppCompatActivity implements ICallbackActivity {

    private String TAG = "CreateReportActivity";
    private EditText descriptionText, locationText;
    private Button mUploadImagesButton, saveButton;
    private RadioGroup radioGroupSize, radioGroupSeverity;

    private ReportData reportData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        descriptionText = (EditText) findViewById(R.id.user_litter_desc);

        radioGroupSize = (RadioGroup) findViewById(R.id.radio_group_size);
        radioGroupSeverity = (RadioGroup) findViewById(R.id.radio_group_severity);

        reportData = new ReportData();
        // always set emailid
        reportData.setReporteeID("sandhyafeb1990@gmail.com");


        mUploadImagesButton = (Button) findViewById(R.id.add_images_button);
        mUploadImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG,"Attempting to upload images");
                Intent intent = new Intent(getApplicationContext(),UploadImagesActivity.class);
                startActivity(intent);
            }
        });


        saveButton = (Button) findViewById(R.id.create_report_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create a new report
                reportData.setLocation(new LocationDetails(12.12,12.13));
                reportData.setImages("la bla bla");
                reportData.setDescription(descriptionText.getText().toString());
                AddReportHandler uih = new AddReportHandler(
                    CreateReportActivity.this, "create_report_activity", reportData);
                uih.addNewReport();
                // grab a handler

                // upload
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //Intent intent = new Intent(this,SettingsActivity.class);
            //startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        String litterSize = "", litterSeverity="";
        // Check which radio button was clicked
        if (-1 != radioGroupSize.getCheckedRadioButtonId()) {
            litterSize = ((RadioButton) findViewById(radioGroupSize.getCheckedRadioButtonId())).getText().toString();
            reportData.setSize(litterSize);
        }
        if (-1 != radioGroupSeverity.getCheckedRadioButtonId()) {
            litterSeverity = ((RadioButton) findViewById(radioGroupSeverity.getCheckedRadioButtonId())).getText().toString();
            reportData.setSeverityLevel(litterSeverity);
        }
        Log.d(TAG, reportData.toString());
    }

    @Override
    public void onPostProcessCompletion(Object responseObj, String identifier, boolean isSuccess) {

    }
}
