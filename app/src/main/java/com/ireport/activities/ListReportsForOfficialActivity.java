package com.ireport.activities;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.ireport.R;
import com.ireport.controller.utils.httpUtils.APIHandlers.GetAllReportsHandler;
import com.ireport.controller.utils.httpUtils.APIHandlers.GetReportForEmailId;
import com.ireport.controller.utils.httpUtils.APIHandlers.GetReportForStatus;
import com.ireport.model.AppContext;
import com.ireport.model.ReportData;
import com.ireport.model.UserInfo;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListReportsForOfficialActivity extends AppCompatActivity
        implements ICallbackActivity, AdapterView.OnItemClickListener {

    private UserInfo userInfo = AppContext.getInstance().getCurrentLoggedInUser();
    private static final String TAG = "ListReportOfficial";
    GetAllReportsHandler getAllReportsHandler = null;
    GetReportForStatus getAllReportByStatusHandler = null;
    GetReportForEmailId getAllReportByEmailIdHandler = null;

    List<ReportData> reportDataList;
    ListView listView;
    List<ListActivityRowClass> rowItems;
    private MenuItem searchMenuItem;

    TextView noReportsMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "In list reports for offical activity");
        setContentView(R.layout.activity_list_reports_for_official);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("List Reports: Official");
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // load profile details from the server
        UserInfo currUser = AppContext.getInstance().getCurrentLoggedInUser();
        Log.d(TAG, "in official workflow");
        getAllReportsHandler = new GetAllReportsHandler(this, "get_all_reports");
        getAllReportsHandler.getAllReportsData(getApplicationContext());

        // Get the intent, verify the action and get the query
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        Log.d(TAG, "In handle intent");
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d(TAG,"Trying to search for " + query);
            doMySearch(query);
            //use the query to search your data somehow
        }
    }

    private void doMySearch(String emailId) {
        Log.d(TAG,"Email id to be searched for" + emailId);
        getAllReportByEmailIdHandler = new GetReportForEmailId(this,"getAllReportsByEmail",emailId);
        getAllReportByEmailIdHandler.getReportForEmailId(getApplicationContext());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        searchMenuItem = menu.findItem(R.id.search_by_email);
        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // Do something when collapsed
                Log.d("SEARCH", "in collapse");
                Log.d("SEARCH", Integer.toString(AppContext.getInstance().getCurrentUserReportsToShow().size()));
                populateListViewElements(AppContext.getInstance().getCurrentUserReportsToShow());
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Log.d("SEARCH", "in expand");
                return true;
            }
        });

        /*
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_by_email).getActionView();
        //SearchView signOut = (SearchView) menu.findItem(R.id.action_signout).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        */
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_by_email:
                Log.d(TAG,"Selected search_by_email");
                SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
                SearchView searchView = (SearchView) item.getActionView();
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
                searchView.setIconifiedByDefault(false);
                handleIntent(getIntent());
                return true;
            case R.id.action_signout :
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                AppContext.getInstance().reset();
                Intent intent = new Intent(ListReportsForOfficialActivity.this, MainActivity.class);
                startActivity(intent);
            case R.id.search_by_status :
                Log.d(TAG,"trying to search by status");
                return true;
            default:
                Log.d(TAG, "some crap");
                return false;
        }
    }
    @Override
    public void onPostProcessCompletion(Object responseObj, String identifier, boolean isSuccess) {

        if (responseObj instanceof UserInfo) {
            userInfo = (UserInfo) responseObj;
            Log.d(TAG, "got userinfo - for official: " + userInfo.toString());
        } else if (responseObj instanceof List) {
            if(((List) responseObj).size() == 0) {
                System.out.println("No reports to show for the official.");

                //Show him the msg
                noReportsMsg = (TextView)findViewById(R.id.noReportsMsg);
                noReportsMsg.setVisibility(View.VISIBLE);

                //Hide the fab button too
                findViewById(R.id.fab_button).setVisibility(View.GONE);
            } else {
                //Reports received from the server is more than 1
                Log.d(TAG,"Multiple reports received for this user from the server");
                Log.d(TAG, "Identifier: " + identifier);
                if (identifier.equals("get_all_reports")) {
                    AppContext.getInstance().setCurrentUserReportsToShow((ArrayList<ReportData>) responseObj);
                }
                populateListViewElements((ArrayList<ReportData>) responseObj);
            }
        }
        Log.d(TAG, "got some result back");
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
        Log.d(TAG, "Size of the list " + Integer.toString(rowItems.size()));

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Toast toast = Toast.makeText(getApplicationContext(),
                "Item " + (position) + ": " + rowItems.get(position),
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();

        /*
        Intent intent = new Intent(this,ViewReportActivity.class);
        intent.putExtra("report_id_in_mongo", rowItems.get(position).getId());
        Log.v(TAG,"Item on item click = " + rowItems.get(position).getId());
        startActivity(intent);
        */

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "In resume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "In pause");
    }
}