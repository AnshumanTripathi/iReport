package com.ireport.controller.utils.httpUtils;

import android.location.Location;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.ExecutorDelivery;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ireport.R;
import com.ireport.controller.IReportException;
import com.ireport.controller.utils.Constants;
import com.ireport.controller.utils.VolleyErrorResponse;
import com.ireport.controller.utils.VolleyGetResponse;
import com.ireport.controller.utils.VolleyPostResponse;
import com.ireport.model.LocationDetails;
import com.ireport.model.ReportData;
import com.ireport.model.Settings;
import com.ireport.model.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

/*
    Generic class to interact with server for a particular method
 */
public class HttpBaseCommunicator implements Response.Listener <String>, Response.ErrorListener {
    private static String TAG = "HttpUtils";

    protected String getRequestURL() throws IReportException{
        throw new IReportException("getRequestURL method should be implemented");
    }

    protected void handleResponse(String response) throws IReportException {
        throw new IReportException("handleResponse method should be implemented");
    }

    protected Map<String, String> getParams() throws IReportException {
        throw new IReportException("getParams method should be implemented");
    }


    protected void handleErrorResponse(VolleyError error) {
        // subclasses should override this function if they want to handle error in some specific
        // manner
    }

    @Override
    public void onResponse(String response) {
        try {
            this.handleResponse(response);
        } catch (IReportException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
        this.handleErrorResponse(error);
    }

    public String sendHttpGetRequest() {
        Log.d("GET","Sending HTTP get Request through Volley");

        try {
            String retResponse = null;
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            String url = this.getRequestURL();
            Log.d("URL", url);

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(
                    Request.Method.GET,
                    url,
                    this,
                    this
            );

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            );

            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }catch (IReportException e) {
            e.printStackTrace();
        }

        return "";
    }

    //parse the json and return info for one user
    public static UserInfo parseUserInfoFromJson(String userData) {
        UserInfo uiObj = null;
        try {

            uiObj = new UserInfo();
            Settings settingObj = new Settings();

            JSONObject json = new JSONObject(userData);
            String first_name = json.getString("first_name");
            String email = json.getString("email");
            String last_name = json.getString("last_name");
            String home_address = json.getString("home_address");
            String screen_name = json.getString("screen_name");

            uiObj.setEmail(email);
            uiObj.setFirstName(first_name);
            uiObj.setLastName(last_name);
            uiObj.setHomeAddress(home_address);
            uiObj.setScreenName(screen_name);
            //uiObj.setOfficial();

            String settings = json.getString("settings");
            JSONObject settingJson = new JSONObject(settings);
            String email_confirm = json.getString("email_confirm");
            String email_notify = json.getString("email_notify");
            String anonymous = json.getString("anonymous");

            settingObj.setAllowEmailConfirmation(Boolean.parseBoolean(email_confirm));
            settingObj.setAllowEmailNotification(Boolean.parseBoolean(email_notify));
            settingObj.setAnonymous(Boolean.parseBoolean(anonymous));
            uiObj.setSettings(settingObj);

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        return uiObj;
    }

    ////////////////////////////////////////////////////////////////////////////////

    //POST: getUser
    public void testHTTPPOST_Volley() {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = Constants.SERVER_URL + ":" + Constants.SERVER_PORT + "/getUser";

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new VolleyPostResponse(),
                new VolleyErrorResponse()
        )

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return this.getParams();
//                return getUserByEmail("sanjay_dutt@email.com");
            }
        };

        queue.add(stringRequest);
    }


    public static Map<String, String> getUserByEmail(String email) {
        Map<String,String> params = new HashMap<String, String>();
        params.put("email",email);
        return params;
    }

//////////////////////////////////////////////////////////////////////////////////////////////////


    //POST: updateSettings
/*    public static void volley_updateSettings(final Settings settings, final String email) {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = Constants.SERVER_URL + ":" + Constants.SERVER_PORT + "/updateSettings";

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new VolleyPostResponse(),
                new VolleyErrorResponse()
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return updateSettings(settings, email);
            }
        };
        Log.d(TAG, "Ready to post on " + url );
        queue.add(stringRequest);
    }

    public static Map<String, String> updateSettings(Settings settingObj, String email){
            Map<String,String> params = new HashMap<String, String>();
            params.put("email",email);
            params.put("email_confirm",String.valueOf(settingObj.isAllowEmailConfirmation()));
            params.put("email_notify",String.valueOf(settingObj.isAllowEmailNotification()));
            params.put("Anonymous",String.valueOf(settingObj.isAnonymous()));
            return params;
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////

    //POST: updateUserInfo
    public static void volley_updateUserInfo() {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        Log.d("UPDATE", "Updating user inforamtion");
        String url = Constants.SERVER_URL + ":" + Constants.SERVER_PORT + "/updateUserInfo";

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new VolleyPostResponse(),
                new VolleyErrorResponse()
        )

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return updatePersonalInfo(new UserInfo("123","sanjay_dutt@email.com","fn","ln","ha"));
            }
        };

        queue.add(stringRequest);
    }

    public static Map<String, String> updatePersonalInfo(UserInfo uiObj) {
        Map<String,String> params = new HashMap<String, String>();
        params.put("email",uiObj.getEmail());
        params.put("first_name", uiObj.getFirstName());
        params.put("last_name" , uiObj.getLastName());
        params.put("home_address", uiObj.getHomeAddress());
        params.put("screen_name", uiObj.getScreenName());
        Log.d("UPDATE_PARAMS",params.toString());
        return params;
    }



////////////////////////////////////////////////////////////////////////////////////////////

    //POST: addUser
    public static void volley_addUser() {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = Constants.SERVER_URL + ":" + Constants.SERVER_PORT + "/addUser";

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new VolleyPostResponse(),
                new VolleyErrorResponse()
        )

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return addUser("sanjay_dutt@email.com",false);
            }
        };

        queue.add(stringRequest);
    }

    public static Map<String, String> addUser(String email, boolean isOfficial) {
        Map<String,String> params = new HashMap<>();
        params.put("email",email);
        params.put("isOfficial", String.valueOf(isOfficial));
        return params;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////

    //Report API : POST
    public static void addReport() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = Constants.SERVER_URL + ":" + Constants.SERVER_PORT + "/addReport";

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new VolleyPostResponse(),
                new VolleyErrorResponse()
        )

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return addReport(new ReportData(
                        "sanjay_dutt@email.com",
                        "pics",
                        "hello",
                        "size",
                        "severe",
                        new LocationDetails(
                                12.12,
                                12.122
                        )
                )
                );
            }
        };

        queue.add(stringRequest);
    }

    public static Map<String, String> addReport(ReportData reportObj) {
        Map<String,String> params = new HashMap<>();
        params.put("user_email",reportObj.getReporteeID());
        params.put("pictures", reportObj.getImages());
        params.put("description",reportObj.getDescription());
        params.put("size",reportObj.getSize());
        params.put("severity_level",reportObj.getSeverityLevel());
        params.put("location", " { \"lat\": " + reportObj.getLocation().getLatitude() +", \"lng\": "+ reportObj.getLocation().getLongitude() + "}" );

        Log.d("REPORT", params.toString());

        return params;
    }*/
}

