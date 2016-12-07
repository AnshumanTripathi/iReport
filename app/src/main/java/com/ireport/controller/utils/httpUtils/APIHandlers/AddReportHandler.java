package com.ireport.controller.utils.httpUtils.APIHandlers;

import android.content.Context;

import com.ireport.activities.ICallbackActivity;
import com.ireport.controller.IReportException;
import com.ireport.controller.utils.Constants;
import com.ireport.model.ReportData;
import com.ireport.model.Settings;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Somya on 12/3/2016.
 */

public class AddReportHandler extends HttpBaseCommunicator {
    private ICallbackActivity activity_;
    private String callbackIdentifier_;
    private ReportData repObj;

    public AddReportHandler(
            ICallbackActivity activity,
            String identifier,
            ReportData rObj
    ) {
        this.activity_ = activity;
        this.callbackIdentifier_ = identifier;
        this.repObj = rObj;
    }

    protected String getRequestURL() throws IReportException {
        return Constants.SERVER_URL + ":" + Constants.SERVER_PORT + "/addReport";
    }

    protected void handleResponse(String response) throws IReportException {
        try {
            JSONObject json = new JSONObject(response);
            String statusCode = json.getString("statusCode");

            // create the final object here. test code for now
            this.activity_.onPostProcessCompletion(
                    statusCode,
                    this.callbackIdentifier_,
                    true    /* is error */);
        } catch (JSONException je) {
            je.printStackTrace();
        }
    }

    @Override
    protected Map<String, String> getPostParams() {
        Map<String,String> params = new HashMap<String, String>();
        params.put("user_email",repObj.getReporteeID());
        params.put("pictures",repObj.getImages());
        params.put("description", repObj.getDescription());
        params.put("size",repObj.getSize());
        params.put("severity_level",repObj.getSeverityLevel());
        params.put("location", repObj.getLocation().toString());
        return params;
    }

    public void addNewReport(Context ctx) {
        this.sendHttpPostRequest(ctx);
        System.out.println("returning from UI thread immediately");
    }
}
