package com.ireport.activities;

import com.ireport.R;
import com.ireport.controller.utils.Constants;
import com.ireport.controller.utils.httpUtils.APIHandlers.GetAllReportsHandler;
import com.ireport.controller.utils.httpUtils.APIHandlers.GetUserForEmailID;
import com.ireport.model.ReportData;
import com.ireport.model.UserInfo;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class ListReportsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ICallbackActivity {

    private static String TAG = "ListReportsActivity";
    private UserInfo userInfo;
    List<ReportData> reportDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "First thing EVER!!");
        setContentView(R.layout.activity_list_reports);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Sandhya - Testing View Report Activity*/

        /*/Send a report object with app context


        Intent intent = new Intent(this,ViewReportActivity.class);
        startActivity(intent);
        */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // load profile details from the server
        GetUserForEmailID getUserForEmailID = new GetUserForEmailID(this, "getUser", Constants.SANDHYA_EMAIL);
        getUserForEmailID.getUserDataForEmail();

        // Load any reports
        reportDataList = new ArrayList<>();
        GetAllReportsHandler getAllReportsHandler = new GetAllReportsHandler(this, "getAllReportsForUser");
        getAllReportsHandler.getAllReportsData();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_reports, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.d(TAG, "opetionsitemselected");
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,SettingsActivity.class);
            if ( null != userInfo ) {
                intent.putExtra("user_info", userInfo);
            }
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_view_profile) {
            Intent intent = new Intent(this,ViewProfileActivity.class);
            if ( null != userInfo)
                intent.putExtra("user_info", userInfo);
            startActivity(intent);
        } else if (id == R.id.nav_notifcations) {
            Intent intent = new Intent(this, ViewNotificationsActivity.class);
            if ( null != userInfo)
                intent.putExtra("user_info", userInfo);
            startActivity(intent);
        } else if (id == R.id.nav_newreport) {
            Intent intent = new Intent(this, CreateReportActivity.class);
            if ( null != userInfo)
                intent.putExtra("user_info", userInfo);
            startActivity(intent);
        } else if (id == R.id.nav_allreports) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPostProcessCompletion(Object responseObj, String identifier, boolean isSuccess) {
        // sample code.
        Log.d(TAG, "In onPostProcessCompletion");
        if (responseObj instanceof UserInfo) {
            Log.d(TAG, "got userinfo!!!!");
            userInfo = (UserInfo) responseObj;
            Log.d(TAG, userInfo.toString());
            Log.d(TAG, userInfo.getSettings().toString());
        } else if (responseObj instanceof List) {
            Log.d(TAG, "Got a bunch of reports!");
            for (ReportData report : (ArrayList <ReportData>) responseObj) {
                Log.d(TAG, report.toString());
            }
        }

    }
}
