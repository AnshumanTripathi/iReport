package com.ireport.controller.utils.httpUtils.APIHandlers;

import android.util.Log;

import com.ireport.activities.ICallbackActivity;
import com.ireport.controller.IReportException;
import com.ireport.controller.utils.Constants;
import com.ireport.model.Settings;
import com.ireport.model.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Somya on 12/3/2016.
 */

public class UpdateUserInfoHandler extends HttpBaseCommunicator {
    private ICallbackActivity activity_;
    private String callbackIdentifier_;
    private UserInfo uiObj;


    public UpdateUserInfoHandler(
            ICallbackActivity activity,
            String identifier,
            UserInfo uiObj
    ) {
        this.activity_ = activity;
        this.callbackIdentifier_ = identifier;
        this.uiObj  = uiObj;
    }

    protected String getRequestURL() throws IReportException {
        return Constants.SERVER_URL + ":" + Constants.SERVER_PORT + "/updateUserInfo";
    }

    protected void handleResponse(String response) throws IReportException {
        Log.d("response","-------------------");
        try {
            JSONObject json = new JSONObject(response);
            String statusCode = json.getString("statusCode");
            if(statusCode != "200") {
                // create the final object here. test code for now
                this.activity_.onPostProcessCompletion(
                        null,
                        this.callbackIdentifier_,
                        true    /* is error */);
            } else {
                // create the final object here. test code for now
                this.activity_.onPostProcessCompletion(
                        uiObj,
                        this.callbackIdentifier_,
                        true    /* is error */);
            }
        } catch(JSONException je) {
            je.printStackTrace();
        }
    }

    @Override
    protected Map<String, String> getPostParams() {
        Map<String,String> params = new HashMap<String, String>();
        params.put("email",uiObj.getEmail());
        params.put("first_name",uiObj.getFirstName());
        params.put("last_name",uiObj.getLastName());
        params.put("screen_name",uiObj.getScreenName());
        params.put("home_address",uiObj.getHomeAddress());
        Log.d("PARAMS------->>>",params.toString());
        return params;
    }

    public void updateUserInfo() {
        this.sendHttpPostRequest();
        System.out.println("returning from UI thread immediately");
    }
}
