package com.ireport.activities;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.ireport.R;
import com.ireport.controller.utils.httpUtils.APIHandlers.AddReportHandler;
import com.ireport.controller.utils.httpUtils.APIHandlers.GetAllReportsHandler;
import com.ireport.controller.utils.httpUtils.APIHandlers.UpdateSettingsHandler;
import com.ireport.model.LocationDetails;
import com.ireport.model.ReportData;
import com.ireport.model.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
    ICallbackActivity {
    private static int RC_SIGN_IN = 0;
    private static String TAG = "MAIN_ACTIVITY";
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private String AUTH_TAG = "AUTH";

    /**********************************TEST CODE*********************/
    @Override
    public void onPostProcessCompletion(Object responseObj, String identifier, boolean isSuccess) {
        // sample code.
        if (responseObj instanceof String) {
            Log.d("Status Code is:", responseObj.toString());
        }
    }
    /*********************TEST CODE**********************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //fb
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_main);

        /************TEST CODE****************************************************/
        ReportData repObj = new ReportData();
        repObj.setLocation(new LocationDetails(12.12,12.13));
        repObj.setImages("la bla bla");
        repObj.setSize("huge huge huge");
        repObj.setDescription("kachra found");
        repObj.setReporteeID("sanjay_dutt@email.com");
        repObj.setSeverityLevel("high severe");
        AddReportHandler uih = new AddReportHandler(
                this,
                "getAllReports",
                repObj
        );
        uih.addNewReport();

        /***************************************************************************/

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            Log.d(AUTH_TAG, mAuth.getCurrentUser().getEmail());
        } else {
            Log.d(AUTH_TAG, "Trying to sign in");
            Intent intent = new Intent(this,ListReportsActivity.class);
            startActivity(intent);
            //Firebase code starts here
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setProviders(
                            AuthUI.FACEBOOK_PROVIDER,
                            AuthUI.GOOGLE_PROVIDER)
                    .build(), RC_SIGN_IN

            );

        }

        findViewById(R.id.sign_out_button).setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(AUTH_TAG, "In onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                //user logged in
                Log.d(AUTH_TAG, mAuth.getCurrentUser().getEmail());

            } else {
                //user not authenticated
                Log.d(AUTH_TAG, "Not authenticated");
            }
        }
    }

    @Override
    public void onClick (View view){
        if (view.getId() == R.id.sign_out_button) {
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d("SIGNOUT", "signed out");
                    finish();
                }
            });
        }
    }
}
