package com.ireport.controller.utils.httpUtils.APIHandlers;

import android.content.Context;

import com.ireport.activities.ICallbackActivity;
import com.ireport.controller.IReportException;
import com.ireport.controller.utils.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Somya on 12/3/2016.
 */
public class GetAllUsersHandler extends HttpBaseCommunicator {

    private ICallbackActivity activity_;
    private String callbackIdentifier_;
    public GetAllUsersHandler(ICallbackActivity activity, String identifier) {
        this.activity_ = activity;
        this.callbackIdentifier_ = identifier;
    }

    @Override
    protected String getRequestURL() throws IReportException {
        return Constants.SERVER_URL + ":" + Constants.SERVER_PORT + "/getAllUsers";
    }

    @Override
    protected void handleResponse(String response) throws IReportException {
        // TODO: parse json and populate the user data
        //TODO: do this if needed in the project.

        // create the final object here. test code for now
        this.activity_.onPostProcessCompletion(
                response,
                this.callbackIdentifier_,
                true    /* is error */);
    }

    @Override
    protected Map<String, String> getPostParams() {
        return new HashMap<>();
    }

    public void getAllUsersData(Context ctx) {
        this.sendHttpGetRequest(ctx);
        System.out.println("returning from UI thread immediately");
    }
}
