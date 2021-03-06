package com.ireport.controller.utils.httpUtils.APIHandlers;

import android.content.Context;
import android.util.Log;

import com.ireport.activities.ICallbackActivity;
import com.ireport.controller.IReportException;
import com.ireport.controller.utils.Constants;
import com.ireport.model.LocationDetails;
import com.ireport.model.ReportData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Somya on 12/7/2016.
 */

public class UpdateReportByIdHandler extends HttpBaseCommunicator {
    private ICallbackActivity activity_;
    private String callbackIdentifier_;
    private String reportId;
    private String status;

    public UpdateReportByIdHandler(
            ICallbackActivity activity,
            String identifier,
            String reportId,
            String status
    ) {
        this.activity_ = activity;
        this.callbackIdentifier_ = identifier;
        this.reportId = reportId;
        this.status = status;
    }

    @Override
    protected String getRequestURL() throws IReportException {
        return Constants.SERVER_URL + ":" + Constants.SERVER_PORT + "/updateReportStatus";
    }

    @Override
    protected void handleResponse(String response) throws IReportException {
        try {
            JSONObject json = new JSONObject(response);
            Log.d("Response: ",json.toString());
            String statusCode = json.getString("statusCode");

            Log.d("UpdateReportByIdHandler", statusCode);

            // create the final object here. test code for now
            this.activity_.onPostProcessCompletion(
                    statusCode,
                    this.callbackIdentifier_,
                    true    /* is error */);

        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private ReportData parseResponseForReports(String response) {
        ReportData rd = new ReportData();

        try {

            JSONObject json = new JSONObject(response);
            String responseCode = json.getString("statusCode");
            Log.d("GetReportByIDHandler", responseCode);

            if(responseCode.equals("200")) {
                String data = json.getString("data");

                JSONObject tempJson = new JSONObject(data);

                String id = tempJson.getString("_id");
                rd.setReportId(id);
                Log.d("GetReportID received: ",id);

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

                String reportId = tempJson.getString("_id");
                rd.setReportId(reportId);

                String streetAdd = tempJson.getString("street_address");
                rd.setStreetAddress(streetAdd);

                String timeStamp = tempJson.getString("timestamp");
                rd.setTimestamp(timeStamp);

                if (tempJson.has("location")) {
                    String location = tempJson.getString("location");
                    JSONObject locJson = new JSONObject(location);

                    String lat = locJson.getString("lat");
                    String lng = locJson.getString("lng");

                    rd.setLocation(new LocationDetails(Double.valueOf(lat), Double.valueOf(lng)));
                }
            }

        } catch (JSONException je) {
            je.printStackTrace();
        }
        return rd;
    }

    @Override
    protected Map<String, String> getPostParams() {
        HashMap<String,String> params = new HashMap<>();
        params.put("id",reportId);
        params.put("status",status);
        Log.d("PARAMS",params.toString());
        return params;
    }

    public void updateReportForReportId(Context ctx) {
        this.sendHttpPostRequest(ctx);
        System.out.println("returning from UI thread immediately");
    }
}
