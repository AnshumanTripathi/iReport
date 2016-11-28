package com.ireport.activites;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.amazonaws.mobileconnectors.apigateway.ApiRequest;
import com.amazonaws.util.StringUtils;
import com.ireport.R;

import java.util.HashMap;
import java.util.Map;

import static android.R.id.content;

public class MainActivity extends AppCompatActivity {

    private Button signUpBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initElements();
    }

    private void initElements(){
        signUpBtn = (Button) findViewById(R.id.signUpBtn);
    }

    public void signUp(){

    }
}
