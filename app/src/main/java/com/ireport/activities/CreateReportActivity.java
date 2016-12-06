package com.ireport.activities;

import com.ireport.R;
import com.ireport.controller.utils.Constants;
import com.ireport.controller.utils.httpUtils.APIHandlers.AddReportHandler;
import com.ireport.controller.utils.locationUtils.CurrentLocationUtil;
import com.ireport.controller.utils.locationUtils.LocationUtils;
import com.ireport.model.AppContext;
import com.ireport.model.LocationDetails;
import com.ireport.model.ReportData;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import android.widget.Toast;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

public class CreateReportActivity extends AppCompatActivity implements ICallbackActivity {

    private String TAG = "CreateReportActivity";
    private EditText descriptionText, locationText;
    private Button mUploadImagesButton, saveButton;
    private RadioGroup radioGroupSize, radioGroupSeverity;

    private ReportData reportData;

    //Location variables
    private static final int ACCESS_COARSE_LOCATION = 1;
    AppContext ctx = AppContext.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_report);

        ///////////////////////////////////////////////////////////
        //Calling to get the current location of the user.
        //Test Code: Sandhya , use this fn for reference
        //CurrentLocationUtil lu = new CurrentLocationUtil();
        //lu.getCurrentLocation(this,ctx);
        ///////////////////////////////////////////////////////////

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        descriptionText = (EditText) findViewById(R.id.user_litter_desc);

        radioGroupSize = (RadioGroup) findViewById(R.id.radio_group_size);
        radioGroupSeverity = (RadioGroup) findViewById(R.id.radio_group_severity);

        reportData = new ReportData();
        // always set emailid
        reportData.setReporteeID(Constants.SANDHYA_EMAIL);


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
