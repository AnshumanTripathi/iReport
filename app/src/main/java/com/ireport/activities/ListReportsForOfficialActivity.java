package com.ireport.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ireport.R;
import com.ireport.controller.utils.httpUtils.APIHandlers.GetAllReportsHandler;
import com.ireport.model.AppContext;
import com.ireport.model.ReportData;
import com.ireport.model.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class ListReportsForOfficialActivity extends AppCompatActivity
        implements ICallbackActivity, AdapterView.OnItemClickListener {

    private UserInfo userInfo = AppContext.getInstance().getCurrentLoggedInUser();
    private static final String TAG = "ListReportOfficial";
    GetAllReportsHandler getAllReportsHandler = null;

    List<ReportData> reportDataList;
    ListView listView;
    List<ListActivityRowClass> rowItems;

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
    public void onPostProcessCompletion(Object responseObj, String identifier, boolean isSuccess) {

        if (responseObj instanceof UserInfo) {
            userInfo = (UserInfo) responseObj;
            Log.d(TAG, "got userinfo - for official: " + userInfo.toString());
        } else if (responseObj instanceof List) {
            if(((List) responseObj).size() == 0) {
                Log.d(TAG, "No reports to show for this official.");
                Toast.makeText(
                        getApplicationContext(),
                        "Your city is litter-free :-)",
                        Toast.LENGTH_SHORT).show();
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


}
