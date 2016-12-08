package com.ireport.activities;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.ireport.R;
import com.ireport.controller.utils.httpUtils.APIHandlers.GetReportForEmailId;
import com.ireport.controller.utils.httpUtils.APIHandlers.GetUserForEmailID;
import com.ireport.model.AppContext;
import com.ireport.model.ReportData;
import com.ireport.model.UserInfo;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class ListReportsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ICallbackActivity,
        AdapterView.OnItemClickListener
{

    private static String TAG = "ListReportsActivity";
    private UserInfo userInfo = AppContext.getInstance().getCurrentLoggedInUser();

    List<ReportData> reportDataList;

    GetReportForEmailId getCurrUserReports = null;
    GetUserForEmailID getUserForEmailID = null;

    /*********************************List Report Activity Code: Somya*****************************/
    ListView listView;
    List<ListActivityRowClass> rowItems;
    /**********************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "First thing EVER!!");
        setContentView(R.layout.activity_list_reports);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // load profile details from the server
        String currUserEmail = AppContext.getInstance().getCurrentLoggedInUser().getEmail();
        Log.d(TAG,currUserEmail);
        //getUserForEmailID = new GetUserForEmailID(this, "getUser", currUserEmail);
        //getUserForEmailID.getUserDataForEmail(getApplicationContext());
        
        //Somya - Actual server call
        getCurrUserReports = new GetReportForEmailId(this,
                "getAllReportsForUser",
                currUserEmail
        );
        getCurrUserReports.getReportForEmailId(getApplicationContext());

//    findViewById(R.id.goToMaps).setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Intent intent = new Intent(ListReportsActivity.this, MapsActivity.class);
//            startActivity(intent);
//        }
//    });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
 /*       Toast toast = Toast.makeText(getApplicationContext(),
                "Item " + (position) + ": " + rowItems.get(position),
                Toast.LENGTH_SHORT);

        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
*/
        Intent intent = new Intent(this,ViewReportActivity.class);
        intent.putExtra("report_id_in_mongo", rowItems.get(position).getId());
        Log.v(TAG,"Item on item click = " + rowItems.get(position).getId());
        // TEST CODE, SANDHYA
        intent.putExtra("images", reportDataList.get(position).getImages());
        intent.putExtra("street_address", reportDataList.get(position).getStreetAddress());
        // END TEST CODE
        startActivity(intent);

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
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,SettingsActivity.class);
            if ( null != userInfo ) {
//                intent.putExtra("user_info", userInfo);
                startActivity(intent);
            } else {
                Toast.makeText(getBaseContext(), "Could not load user settings!", Toast.LENGTH_SHORT).show();
            }

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
            Intent intent = new Intent(this, ViewProfileActivity.class);
            if (null != userInfo){
//                intent.putExtra("user_info", userInfo);
                startActivity(intent);
            }else {
                Toast.makeText(getBaseContext(), "Could not load user information!", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.nav_notifcations) {
            Intent intent = new Intent(this, ViewNotificationsActivity.class);
            if ( null != userInfo)
//                intent.putExtra("user_info", userInfo);
            startActivity(intent);
        } else if (id == R.id.nav_newreport) {
            Intent intent = new Intent(this, CreateReportActivity.class);
            if ( null != userInfo)
//                intent.putExtra("user_info", userInfo);
            startActivity(intent);
        } else if (id == R.id.nav_allreports) {

        } else if (id == R.id.mSignOut){
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            AppContext.getInstance().reset();
            Intent intent = new Intent(ListReportsActivity.this, MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPostProcessCompletion(Object responseObj, String identifier, boolean isSuccess) {
        // sample code.
        if (responseObj instanceof UserInfo) {
            Log.d(TAG, "got userinfo");
            userInfo = (UserInfo) responseObj;
            Log.d(TAG, userInfo.toString());
            Log.d(TAG, userInfo.getSettings().toString());
        } else if (responseObj instanceof List) {
            if(((List) responseObj).size() == 0) {
                System.out.println("No reports to show for the user.");
                //Point him to a different activity or a view etc.
                //TODO: Pending.
                
                Toast.makeText(
                        getApplicationContext(),
                        "No reports to be displayed!",
                        Toast.LENGTH_SHORT).show();
            } else {
                //Reports received from the server is more than 1
                Log.d(TAG,"Multiple reports received for this user from the server");
                populateListViewElements((ArrayList<ReportData>) responseObj);
            }
        }
    }

    // this method will populate the reports data in the list view on the activity
    private void populateListViewElements(ArrayList<ReportData> reportList) {
        reportDataList = reportList;
        rowItems = new ArrayList<ListActivityRowClass>();


        for (int i = 0; i < reportList.size(); i++) {
            ListActivityRowClass item = new ListActivityRowClass(
                    getImage(reportList.get(i).getImages()),
                    reportList.get(i).getDescription(),
                    reportList.get(i).getTimestamp(),
                    reportList.get(i).getStatus(),
                    reportList.get(i).getReportId()
            );
            Log.v(TAG,"Item description = " + item.getDescription());
            Log.v(TAG,"Item id = " + item.getId());
            rowItems.add(item);
        }

        listView = (ListView) findViewById(R.id.list);
        CustomListViewAdapter adapter = new CustomListViewAdapter(this,
                R.layout.list_item, rowItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    private Bitmap getImage(String imageString) {
        String ResponseImageArrayString[] = imageString.split(",");
        byte[] decodedString = Base64.decode(ResponseImageArrayString[0].getBytes(), Base64.DEFAULT);
        Bitmap Responseimage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return Responseimage;
    }



}
