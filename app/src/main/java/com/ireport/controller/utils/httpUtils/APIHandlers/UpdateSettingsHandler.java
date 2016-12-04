package com.ireport.controller.utils.httpUtils.APIHandlers;

package com.ireport.controller.utils.httpUtils;

import com.ireport.activities.ICallbackActivity;
import com.ireport.controller.IReportException;
import com.ireport.controller.utils.Constants;
import com.ireport.model.Settings;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Somya on 12/3/2016.
 */

public class UpdateSettingsHandler extends HttpBaseCommunicator {

    private ICallbackActivity activity_;
    private String callbackIdentifier_;
    private String email;
    private Settings settingsObj;

    public UpdateSettingsHandler(
            ICallbackActivity activity,
            String identifier,
            String email,
            Settings settingsObj
    ) {
        this.activity_ = activity;
        this.callbackIdentifier_ = identifier;
        this.email = email;
        this.settingsObj = settingsObj;
    }

    protected String getRequestURL() throws IReportException {
        return Constants.SERVER_URL + ":" + Constants.SERVER_PORT + "/updateSettings";
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
        } catch(JSONException je) {
            je.printStackTrace();
        }
    }

    @Override
    protected Map<String, String> getPostParams() {
        Map<String,String> params = new HashMap<String, String>();
        params.put("email",email);
        params.put("email_confirm",String.valueOf(settingsObj.isAllowEmailConfirmation()));
        params.put("email_notify",String.valueOf(settingsObj.isAllowEmailNotification()));
        params.put("Anonymous",String.valueOf(settingsObj.isAnonymous()));
        return params;
    }

    public void updateSettingForUser() {
        this.sendHttpPostRequest();
        System.out.println("returning from UI thread immediately");
    }


}
