package com.ireport.activities;

import com.ireport.R;
import com.ireport.controller.utils.httpUtils.APIHandlers.UpdateUserInfoHandler;
import com.ireport.model.AppContext;
import com.ireport.model.UserInfo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ViewProfileActivity extends AppCompatActivity implements ICallbackActivity {

    private EditText screenNameEditText, firstNameEditText, lastNameEditText, homeAddressEditText;
    private Button saveButton;
    private static String TAG = "ViewProfileActivity";
    private UserInfo userInfo = AppContext.getInstance().getCurrentLoggedInUser();
    UpdateUserInfoHandler updateUserInfoHandler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Your Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Log.d(TAG, "Got userinfo: " + userInfo.toString());

        // initialize all the edit texts and buttons
        screenNameEditText = (EditText) findViewById(R.id.screenNameEditText);
        firstNameEditText = (EditText) findViewById(R.id.firstNameEditText);
        lastNameEditText = (EditText) findViewById(R.id.lastNameEditText);
        homeAddressEditText = (EditText) findViewById(R.id.homeAddressEditText);

        screenNameEditText.setText(userInfo.getScreenName());
        firstNameEditText.setText(userInfo.getFirstName());
        lastNameEditText.setText(userInfo.getLastName());
        homeAddressEditText.setText(userInfo.getHomeAddress());


        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Save clicked");
                userInfo.setScreenName(screenNameEditText.getText().toString());
                userInfo.setFirstName(firstNameEditText.getText().toString());
                userInfo.setLastName(lastNameEditText.getText().toString());
                userInfo.setHomeAddress(homeAddressEditText.getText().toString());

                updateUserInfoHandler = new UpdateUserInfoHandler(
                        ViewProfileActivity.this,
                        "view_profile_activity",
                        userInfo
                );
                updateUserInfoHandler.updateUserInfo(getApplicationContext());
                Log.d(TAG, "New user info: " + userInfo.toString());
            }
        });
    }

    @Override
    public void onPostProcessCompletion(Object responseObj, String identifier, boolean isSuccess) {
        //direct the user to list reports activity
        Toast.makeText(ViewProfileActivity.this,"Profile details updated!",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(
                ViewProfileActivity.this,
                ListReportsActivity.class
        );
        startActivity(intent);
    }
}
