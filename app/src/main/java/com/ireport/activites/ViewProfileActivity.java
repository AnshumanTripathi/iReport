package com.ireport.activites;

import com.ireport.R;
import com.ireport.model.Settings;
import com.ireport.model.UserInfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ViewProfileActivity extends AppCompatActivity {

    private EditText screenNameEditText, firstNameEditText, lastNameEditText, homeAddressEditText;
    private Button saveButton;
    private static String TAG = "ViewProfileActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        Intent i = getIntent();
        UserInfo userInfo = i.getParcelableExtra("user_info");
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
                String screenName = screenNameEditText.getText().toString();
                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();
                String homeAddress= homeAddressEditText.getText().toString();
                UserInfo userInfo = new UserInfo(screenName, "sandhyafeb1990@gmail.com",
                        firstName, lastName, homeAddress);
                Log.d(TAG, "New user info: " + userInfo.toString());
            }
        });
    }
}
