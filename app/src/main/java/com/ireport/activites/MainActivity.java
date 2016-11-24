package com.ireport.activites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.ireport.R;

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
