package com.ireport.controller.utils.httpUtils.APIHandlers;

import com.ireport.activites.ICallbackActivity;
import com.ireport.controller.IReportException;
import com.ireport.controller.utils.Constants;
import com.ireport.model.ReportData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Somya on 12/4/2016.
 */

public class AddUserHandler extends HttpBaseCommunicator {

    private ICallbackActivity activity_;
    private String callbackIdentifier_;
    private String email;
    private boolean isOfficial;

    public AddUserHandler(
            ICallbackActivity activity,
            String identifier,
            String email,
            Boolean isOfficial
    ) {
        this.activity_ = activity;
        this.callbackIdentifier_ = identifier;
        this.email = email;
        this.isOfficial = isOfficial;
    }

    protected String getRequestURL() throws IReportException {
        return Constants.SERVER_URL + ":" + Constants.SERVER_PORT + "/addUser";
    }

    protected void handleResponse(String response) throws IReportException {
        try {
            JSONObject json = new JSONObject(response);
            String statusCode = json.getString("statusCode");

            // create the final object here.
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
        params.put("email",email);
        params.put("isOfficial",String.valueOf(isOfficial));
        return params;
    }

    public void addNewUser() {
        this.sendHttpPostRequest();
        System.out.println("returning from UI thread immediately");
    }
}
