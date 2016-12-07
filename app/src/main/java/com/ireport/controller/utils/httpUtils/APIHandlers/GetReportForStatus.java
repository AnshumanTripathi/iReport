package com.ireport.controller.utils.httpUtils.APIHandlers;

import android.content.Context;

import com.ireport.activities.ICallbackActivity;
import com.ireport.controller.IReportException;
import com.ireport.controller.utils.Constants;
import com.ireport.model.LocationDetails;
import com.ireport.model.ReportData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Somya on 12/4/2016.
 */

public class GetReportForStatus extends HttpBaseCommunicator {
    private ICallbackActivity activity_;
    private String callbackIdentifier_;
    private String status;

    public GetReportForStatus(ICallbackActivity activity, String identifier, String status) {
        this.activity_ = activity;
        this.callbackIdentifier_ = identifier;
        this.status = status;
    }

    @Override
    protected String getRequestURL() throws IReportException {
        return Constants.SERVER_URL + ":" + Constants.SERVER_PORT + "/getReports";
    }

    @Override
    protected void handleResponse(String response) throws IReportException {
        ArrayList<ReportData> reportList = parseResponseForReports(response);

        // create the final object here. test code for now
        this.activity_.onPostProcessCompletion(
                reportList,
                this.callbackIdentifier_,
                true    /* is error */);
    }

    private ArrayList<ReportData> parseResponseForReports (String response) {
        ArrayList<ReportData> riData = new ArrayList<>();

        try {

            JSONObject json = new JSONObject(response);

            JSONArray arrJson= json.getJSONArray("data");
            String[] arr = new String[arrJson.length()];

            for(int i = 0; i < arrJson.length(); i++) {
                arr[i] = arrJson.getString(i);

                JSONObject tempJson = new JSONObject(arr[i]);
                ReportData rd = new ReportData();

                String email = tempJson.getString("user_email");
                rd.setReporteeID(email);

                String description = tempJson.getString("description");
                rd.setDescription(description);

                String size = tempJson.getString("size");
                rd.setSize(size);

                String severity = tempJson.getString("severity_level");
                rd.setSeverityLevel(severity);

                String pic = tempJson.getString("pictures");
                rd.setImages(pic);

                String status = tempJson.getString("status");
                rd.setStatus(status);

                if(tempJson.has("location")) {
                    String location = tempJson.getString("location");
                    JSONObject locJson = new JSONObject(location);

                    String lat = locJson.getString("lat");
                    String lng = locJson.getString("lng");

                    rd.setLocation(new LocationDetails(Double.valueOf(lat), Double.valueOf(lng)));
                }
                riData.add(rd);
            }

        } catch (JSONException je) {
            je.printStackTrace();
        }
        return riData;
    }

    @Override
    protected Map<String, String> getPostParams() {
        HashMap<String,String> params = new HashMap<>();
        params.put("status",status);
        return params;
    }

    public void getReportForStatus(Context ctx) {
        this.sendHttpPostRequest(ctx);
        System.out.println("returning from UI thread immediately");
    }
}
