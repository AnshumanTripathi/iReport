package com.ireport.activities;

import com.ireport.R;
import com.ireport.controller.utils.Constants;
import com.ireport.controller.utils.cameraUtils.CameraUtility;
import com.ireport.controller.utils.httpUtils.APIHandlers.AddReportHandler;
import com.ireport.controller.utils.locationUtils.CurrentLocationUtil;
import com.ireport.controller.utils.locationUtils.LocationUtils;
import com.ireport.model.AppContext;
import com.ireport.model.LocationDetails;
import com.ireport.model.ReportData;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class CreateReportActivity extends AppCompatActivity implements ICallbackActivity {

    private String TAG = "CreateReportActivity";
    private EditText descriptionText;
    private TextView mLocationText;
    private TextView ErrorMessage;
    private Button mUploadImagesButton, saveButton, locationButton;
    private RadioGroup radioGroupSize, radioGroupSeverity;

    // For camera
    private String userChoosenTask;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private List<String> imageStringArray;
    private final ArrayList<Bitmap> ResponseimageArray = new ArrayList<Bitmap>();
    private final ArrayList<Bitmap> ShowImagesCaptured = new ArrayList<Bitmap>();
    private final int PICK_IMAGE_MULTIPLE =1;
    private LinearLayout lnrImages;
    private ArrayList<String> imagesPathList;
    private Bitmap yourbitmap;
    private TextView numImagesTextView;
    private int numImages = 0;

    // Report data
    private ReportData reportData;
    private StringBuilder Allerrors=new StringBuilder("");

    //Location variables
    private static final int ACCESS_COARSE_LOCATION = 1;
    AppContext ctx = AppContext.getInstance();

    //handlers
    AddReportHandler uih = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_report);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        imageStringArray = new ArrayList<String>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        descriptionText = (EditText) findViewById(R.id.user_litter_desc);
        radioGroupSize = (RadioGroup) findViewById(R.id.radio_group_size);
        radioGroupSeverity = (RadioGroup) findViewById(R.id.radio_group_severity);
        lnrImages=(LinearLayout)findViewById(R.id.lnrImages);
        ErrorMessage=(TextView) findViewById(R.id.error_message);
        mLocationText = (TextView) findViewById(R.id.streetAdd);

        saveButton = (Button) findViewById(R.id.create_report_button);
        numImagesTextView = (TextView) findViewById(R.id.number_of_images);

        locationButton = (Button) findViewById(R.id.enterLocation);

        //create a new object for report data
        reportData = new ReportData();

        // always set emailid
        reportData.setReporteeID(AppContext.getInstance().getCurrentLoggedInUser().getEmail());

        mUploadImagesButton = (Button) findViewById(R.id.add_images_button);
        mUploadImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        //Reset radio group of size
        int selectedid = radioGroupSize.getCheckedRadioButtonId();
        if (selectedid > 0) {
            radioGroupSize.clearCheck();
        }
        radioGroupSize.clearCheck();
        //read the litter size from the add report page
        radioGroupSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String litterSize = ((RadioButton) findViewById(
                        radioGroupSize.getCheckedRadioButtonId())).
                        getText().
                        toString();

                //set the size of the litter in the report data object
                reportData.setSize(litterSize);
            }
        });

        //Reset radio group of Severity
        int selectedid1 = radioGroupSeverity.getCheckedRadioButtonId();
        if (selectedid1 > 0) {
            radioGroupSeverity.clearCheck();
        }
       radioGroupSeverity.clearCheck();
        //read the litter severity from the add report page
        radioGroupSeverity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String severity = ((RadioButton) findViewById(
                        radioGroupSeverity.getCheckedRadioButtonId())).
                        getText().
                        toString();
                //set the severity of the litter in the report data object
                reportData.setSeverityLevel(severity);
            }
        });

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get current location and fill it in the context
                CurrentLocationUtil.getCurrentLocation(CreateReportActivity.this, ctx);

                //using the currnet coordinated, give the street address back
                LocationUtils LU = new LocationUtils();

                //get the street address only if the current location is available
                if(ctx.getCurrentLocation() != null) {

                    double curLat = ctx.getCurrentLocation().getLatitude();
                    double curLng = ctx.getCurrentLocation().getLongitude();
                    reportData.setLocation(new LocationDetails(curLat, curLng));

                    String locationStreetAddress = LU.getAddress(
                            getApplicationContext(),
                            curLat,
                            curLng
                    );

                    //if street address is available, then only set the address
                    if(locationStreetAddress == null) {
                        reportData.setStreetAddress(Constants.DEF_STREET_ADDRESS);
                    } else{
                        if(locationStreetAddress.length() > 0) {
                            Log.d(TAG, "Address of coordinates:" + locationStreetAddress);
                            reportData.setStreetAddress(locationStreetAddress);
                        } else {
                            reportData.setStreetAddress(Constants.DEF_STREET_ADDRESS);
                        }
                    }
                } else {
                    // if the current location is null, then fill the defaults from constants
                    Log.d(TAG,"No Location available, " +
                            "setting defaults for lat, lng and street add");
                    reportData.setLocation(new LocationDetails(Constants.DEF_LAT,
                            Constants.DEF_LNG));
                    reportData.setStreetAddress(Constants.DEF_STREET_ADDRESS);
                }

                //After setting the street data to the context, to either current or default
                //values, make sure to set that value on the ui as well.
                mLocationText.setText(reportData.getStreetAddress());

            }
        });


        //numImagesTextView.setText(Integer.toString(numImages) + " images added to report");


        //save button is create new report button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create a new report object and send to the server

                //first clear the Error text
                ErrorMessage.setText("");
                Allerrors.delete(0,Allerrors.toString().length());

                //Save the desc in report data now
                reportData.setDescription(descriptionText.getText().toString());

                //save the images in report data now
                // put images together
                String images = TextUtils.join(",", imageStringArray);;
                reportData.setImages(images);

                //If validation passes set everything in the reportData Object
                if(ValidateReportFields(reportData))
                {
                   uih = new AddReportHandler(
                            CreateReportActivity.this, "create_report_activity", reportData);
                    Log.d(TAG, "Sending: " + reportData.toString());
                    uih.addNewReport(getApplicationContext());

                    Toast.makeText(getBaseContext(), "Report Created!", Toast.LENGTH_SHORT).show();

                    //Go back to parent activity
                    Intent upIntent = NavUtils.getParentActivityIntent(CreateReportActivity.this);
                    startActivity(upIntent);
                }
                else
                {
                    Toast.makeText(
                            getBaseContext(),
                            "Report Can not be created\n"+ Allerrors.toString(),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
    }


    // This function will validate the report fields.
    public boolean ValidateReportFields(ReportData reportdata)
    {
        if(reportdata.getDescription().equals(""))
        {
            Allerrors.append("Description is mandatory\n");
        }
        if(reportData.getSize() == null)
        {
            Allerrors.append("Please select size\n");
        }
        if(reportData.getSeverityLevel()==null)
        {
            Allerrors.append("Please select Severity Level\n");
        }
        if(reportdata.getImages().length() == 0 || imageStringArray.size() == 0)
        {
            Allerrors.append("Atleast one Image is necessary\n");
        }
        if(reportdata.getStreetAddress() == null || reportdata.getLocation() == null) {
            Allerrors.append("Add location to the report\n");
        }

        if(Allerrors.length() == 0)
            return true;
        return false;
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


    //read the response back from the add report server call and make sure it is uploaded successfully.
    @Override
    public void onPostProcessCompletion(Object responseObj, String identifier, boolean isSuccess) {
        //Go back to parent activity
        Intent upIntent = NavUtils.getParentActivityIntent(CreateReportActivity.this);
        startActivity(upIntent);
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

        switch (requestCode) {
            case CameraUtility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                    {
                        ResponseimageArray.clear();
                        imageStringArray.clear();
                        galleryIntent();

                    }

                } else {
                    //code for deny
                }
        }
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


    // Code below is for camera
    // For camera
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    // For camera
    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), 1);
    }

    // For camera
    private void selectImage() {
        Log.d(TAG, "in selectImage");
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(CreateReportActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = CameraUtility.checkPermission(CreateReportActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    Log.d(TAG,userChoosenTask);
                    if (result) {
                        Intent intent = new Intent(CreateReportActivity.this, CustomPhotoGalleryActivity.class);
                        startActivityForResult(intent, PICK_IMAGE_MULTIPLE);
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    // For camera
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE){
                Log.d(TAG, "Select from gallery chosen");
                //onCaptureImageResult(data);
                numImagesTextView.setText(numImagesTextView.getText().toString() + "\n. Image Received from gallery.");
            } else if (requestCode == REQUEST_CAMERA) {
                Log.d(TAG, "Took a pic.");
                onCaptureImageResult(data);
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        numImages++;
        numImagesTextView.setText(numImagesTextView.getText().toString() +
                "\n" + Integer.toString(numImages) +
                " images added to report.");
        if (data != null)
            imageStringArray.add(getStringImage((Bitmap) data.getExtras().get("data")));

        ////Store captured image in sdcard of device
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        Log.d("captured",destination.getPath());

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /////////////////////////////////////////////

        //Show images taken from camera in list format i.e. in linear layout
        ShowImagesCaptured.add((Bitmap) data.getExtras().get("data"));

        if(ShowImagesCaptured.size()!=0) {
            lnrImages.removeAllViews();
            for (int i = 0; i < ShowImagesCaptured.size(); i++) {
                yourbitmap = ShowImagesCaptured.get(i);
                ImageView imageView = new ImageView(this);
                imageView.setImageBitmap(yourbitmap);
                imageView.setAdjustViewBounds(true);
                ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(lnrImages.getLayoutParams());
                params.setMargins(10, 10, 10, 10);
                imageView.setPadding(10, 10, 10, 10);
                //Take lnrImages as a linear layout in Activity
                lnrImages.addView(imageView, params);
            }
        }
        /////////////////////////////////////////////

    }

    // For camera. This method converts Image to String
    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        //Log.d("String Image",encodedImage);
        return encodedImage;

    }


}
