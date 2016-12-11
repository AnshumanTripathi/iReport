package com.ireport.activities;

import com.ireport.R;
import com.ireport.controller.utils.httpUtils.APIHandlers.GetAllReportsHandler;
import com.ireport.model.AppContext;
import com.ireport.model.ReportData;
import com.ireport.model.UserInfo;

import java.util.ArrayList;
import java.util.List;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
        implements ICallbackActivity, AdapterView.OnItemClickListener,SearchView.OnQueryTextListener {

    private UserInfo userInfo = AppContext.getInstance().getCurrentLoggedInUser();
    private static final String TAG = "ListReportOfficial";
    GetAllReportsHandler getAllReportsHandler = null;

    List<ReportData> reportDataList;
    ListView listView;
    List<ListActivityRowClass> rowItems;
    private SearchView searchView;
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

        // load profile details from the server
        UserInfo currUser = AppContext.getInstance().getCurrentLoggedInUser();
        Log.d(TAG, "in official workflow");
        getAllReportsHandler = new GetAllReportsHandler(this, "getAllReports");
        getAllReportsHandler.getAllReportsData(getApplicationContext());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        /*
        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.search_by_email);
        Log.d("CRAP", Integer.toString(R.id.search_by_email));
        Log.d("CRAP", searchMenuItem.toString());
        searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener((SearchView.OnQueryTextListener) this);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG,"Trying to search");
            }
        });
        */
        return true;
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
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }


}
