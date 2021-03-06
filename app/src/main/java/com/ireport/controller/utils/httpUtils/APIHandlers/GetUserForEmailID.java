package com.ireport.controller.utils.httpUtils.APIHandlers;

import android.content.Context;
import android.util.Log;

import com.ireport.activities.ICallbackActivity;
import com.ireport.controller.IReportException;
import com.ireport.controller.utils.Constants;
import com.ireport.model.Settings;
import com.ireport.model.UserInfo;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Somya on 12/3/2016.
 */

public class GetUserForEmailID extends HttpBaseCommunicator {

    private ICallbackActivity activity_;
    private String callbackIdentifier_;
    private String email_;

    public GetUserForEmailID(ICallbackActivity activity, String identifier, String email) {
        this.activity_ = activity;
        this.callbackIdentifier_ = identifier;
        this.email_ = email;
    }

    protected String getRequestURL() throws IReportException {
        return Constants.SERVER_URL + ":" + Constants.SERVER_PORT + "/getUser";
    }

    protected void handleResponse(String response) throws IReportException {
        UserInfo uiObj = parseUserInfoFromJson(response);

        // create the final object here. test code for now
        this.activity_.onPostProcessCompletion(
                uiObj,
                this.callbackIdentifier_,
                true    /* is error */);
    }

    @Override
    protected Map<String, String> getPostParams() {
        Map<String,String> params = new HashMap<String, String>();
        params.put("email",email_);
        Log.d("PARAMS", params.toString());
        return params;
    }

    public void getUserDataForEmail(Context ctx) {
        this.sendHttpPostRequest(ctx);
        System.out.println("returning from UI thread immediately");
    }

    //parse the json and return info for one user
    public static UserInfo parseUserInfoFromJson(String userData) {
        UserInfo uiObj = null;
        try {

            uiObj = new UserInfo();
            Settings settingObj = new Settings();

            JSONObject json = new JSONObject(userData);
            String data = json.getString("data");

            JSONObject udata = new JSONObject(data);

            if(udata.has("first_name")) {
                String first_name = udata.getString("first_name");
                uiObj.setFirstName(first_name);
            }else{
                uiObj.setFirstName("");
            }


            String email = udata.getString("email");
            uiObj.setEmail(email);

            if(udata.has("last_name")) {
                String last_name = udata.getString("last_name");
                uiObj.setLastName(last_name);
            }else{
                uiObj.setLastName("");
            }

            if(udata.has("home_address")) {
                String home_address = udata.getString("home_address");
                uiObj.setHomeAddress(home_address);
            }else{
                uiObj.setHomeAddress("");
            }

            String screen_name = udata.getString("screen_name");
            uiObj.setScreenName(screen_name);

            String settings = udata.getString("settings");
            JSONObject settingJson = new JSONObject(settings);
            Log.d("SETTINGS",settings);

            String email_confirm = settingJson.getString("email_confirm");
            if (!email_confirm.isEmpty()) {
                settingObj.setAllowEmailConfirmation(Boolean.parseBoolean(email_confirm));
            }

            String email_notify = settingJson.getString("email_notify");
            if (!email_notify.isEmpty()) {
                settingObj.setAllowEmailNotification(Boolean.parseBoolean(email_notify));
            }

            String anonymous = settingJson.getString("anonymous");
            if (!anonymous.isEmpty()) {
                settingObj.setAnonymous(Boolean.parseBoolean(anonymous));
            }

            uiObj.setSettings(settingObj);

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return uiObj;
    }

}
