package com.ireport.controller.utils.httpUtils;

import android.util.Log;

import com.ireport.activites.ICallbackActivity;
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

    public void getUserDataForEmail() {
        this.sendHttpPostRequest();
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

            String first_name = udata.getString("first_name");
            uiObj.setFirstName(first_name);

            String email = udata.getString("email");
            uiObj.setEmail(email);

            String last_name = udata.getString("last_name");
            uiObj.setLastName(last_name);

            String home_address = udata.getString("home_address");
            uiObj.setHomeAddress(home_address);

            String screen_name = udata.getString("screen_name");
            uiObj.setScreenName(screen_name);

            String settings = udata.getString("settings");
            JSONObject settingJson = new JSONObject(settings);
            Log.d("SETTINGS",settings);

            String email_confirm = settingJson.getString("email_confirm");
            settingObj.setAllowEmailConfirmation(Boolean.parseBoolean(email_confirm));

            String email_notify = settingJson.getString("email_notify");
            settingObj.setAllowEmailNotification(Boolean.parseBoolean(email_notify));

            String anonymous = settingJson.getString("anonymous");
            settingObj.setAnonymous(Boolean.parseBoolean(anonymous));

            uiObj.setSettings(settingObj);

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return uiObj;
    }

}
