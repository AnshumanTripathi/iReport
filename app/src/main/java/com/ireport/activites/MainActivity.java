package com.ireport.activites;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.ireport.R;
import com.ireport.controller.utils.httpUtils.GetAllUsersHandler;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
    ICallbackActivity {
    private static int RC_SIGN_IN = 0;
    private static String TAG = "MAIN_ACTIVITY";
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private String AUTH_TAG = "AUTH";

    @Override
    public void onPostProcessCompletion(Object responseObj, String identifier, boolean isSuccess) {
        // sample code.
        if (responseObj instanceof String) {
            System.out.println("Got reponse from handler in asycn manner:" + responseObj);
            System.out.println("Got reponse from handler in asycn manner with id:" + identifier);
            // now we can update the UI here when response has been retrieved
            // note that this function is still called in Volley thread most probably.
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //fb
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_main);

        //String url = "http://ec2-35-165-22-113.us-west-2.compute.amazonaws.com:3000/getAllUsers";
//        HttpUtils.sendHttpGetRequest();

        GetAllUsersHandler hh = new GetAllUsersHandler(this, "getAllUserCall");
        hh.getAllUsersData();

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            Log.d(AUTH_TAG, mAuth.getCurrentUser().getEmail());
        } else {
            Log.d(AUTH_TAG, "Trying to sign in");
            Intent intent = new Intent(this,ListReportsActivity.class);
            startActivity(intent);
            //Firebase code starts here
            /*startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setProviders(
                            AuthUI.FACEBOOK_PROVIDER,
                            AuthUI.GOOGLE_PROVIDER)
                    .build(), RC_SIGN_IN

            );*/

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
