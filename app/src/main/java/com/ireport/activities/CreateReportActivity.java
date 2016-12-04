package com.ireport.activities;

import com.ireport.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class CreateReportActivity extends AppCompatActivity {

    private String TAG = "CreateReportActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Button mUploadImages = (Button) findViewById(R.id.media_actions);
        mUploadImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG,"Attempting to upload images");
                Intent intent = new Intent(getApplicationContext(),UploadImagesActivity.class);
                startActivity(intent);
            }
        })
        ;
        //setSupportActionBar(toolbar);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onRadioButtonClicked(View view) {
        RadioGroup radioGroupSize = (RadioGroup) findViewById(R.id.radio_group_size);
        RadioGroup radioGroupSeverity = (RadioGroup) findViewById(R.id.radio_group_severity);

        boolean checked = ((RadioButton) view).isChecked();
        String litterSize = "", litterSeverity="";
        // Check which radio button was clicked
        if (-1 != radioGroupSize.getCheckedRadioButtonId()) {
            litterSize = ((RadioButton) findViewById(radioGroupSize.getCheckedRadioButtonId())).getText().toString();
        }
        if (-1 != radioGroupSeverity.getCheckedRadioButtonId()) {
            litterSeverity = ((RadioButton) findViewById(radioGroupSeverity.getCheckedRadioButtonId())).getText().toString();
        }
        Log.v(TAG, "Size is: " + litterSize);
        Log.v(TAG, "Severity is: " + litterSeverity);

    }
}
