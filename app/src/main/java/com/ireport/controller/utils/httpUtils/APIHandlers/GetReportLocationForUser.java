package com.ireport.controller.utils.httpUtils.APIHandlers;

import android.util.Log;

import com.ireport.activities.ICallbackActivity;
import com.ireport.controller.IReportException;
import com.ireport.controller.utils.Constants;
import com.ireport.model.LocationDetails;
import com.ireport.model.ReportData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Somya on 12/4/2016.
 */

public class GetReportLocationForUser extends HttpBaseCommunicator {
    private ICallbackActivity activity_;
    private String callbackIdentifier_;
    private String email;

    public GetReportLocationForUser(ICallbackActivity activity, String identifier, String email) {
        this.activity_ = activity;
        this.callbackIdentifier_ = identifier;
        this.email = email;
    }

    @Override
    protected String getRequestURL() throws IReportException {
        return Constants.SERVER_URL + ":" + Constants.SERVER_PORT + "/getUserReportLocation";
    }

    @Override
    protected void handleResponse(String response) throws IReportException {
        Log.d("RESPONSE:", response.toString());
        ArrayList<LocationDetails> locaList = parseResponseForReportLocations(response);

        // create the final object here. test code for now
        this.activity_.onPostProcessCompletion(
                locaList,
                this.callbackIdentifier_,
                true    /* is error */);
    }

    @Override
    protected Map<String, String> getPostParams() {
        HashMap<String,String> params = new HashMap<>();
        params.put("email",email);
        Log.d("PARAMS",params.toString());
        return params;
    }

    public void getAllReportsLocation() {
        this.sendHttpPostRequest();
        System.out.println("returning from UI thread immediately");
    }

    private ArrayList<LocationDetails> parseResponseForReportLocations(String response) {
        ArrayList<LocationDetails> llist = new ArrayList<>();

        try {
            JSONObject json = new JSONObject(response);
            if(json.has("data")) {
                JSONArray arrJson = json.getJSONArray("data");

                String[] arr = new String[arrJson.length()];

                for (int i = 0; i < arrJson.length(); i++) {
                    arr[i] = arrJson.getString(i);

                    JSONObject tempJson = new JSONObject(arr[i]);
                    LocationDetails locObj = new LocationDetails();

                    if (tempJson.has("lat") &&
                            tempJson.has("lng")) {
                        String lat = tempJson.getString("lat");
                        locObj.setLatitude(Double.valueOf(lat));

                        String lng = tempJson.getString("lng");
                        locObj.setLongitude(Double.valueOf(lng));

                        llist.add(locObj);
                    }
                }
            }
        } catch(JSONException je) {
            je.printStackTrace();
        }

        return llist;
    }



}
