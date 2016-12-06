package com.ireport.controller.utils.httpUtils.APIHandlers;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ireport.controller.IReportException;

import java.util.HashMap;
import java.util.Map;

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

    protected Map<String, String> getPostParams(){
        return new HashMap<>();
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

    /**************************************************************/
    /*This is a HTTP GET API to get all the users in the database*/
    public String sendHttpGetRequest(Context ctx) {
        Log.d("GET","Sending HTTP get Request through Volley");

        try {
            String retResponse = null;
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(ctx);
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

    /*********************************************************************/


    /*********************************************************************/
    //POST: getUser
    public void sendHttpPostRequest(Context ctx) {

        try {
            String retResponse = null;
            RequestQueue queue = Volley.newRequestQueue(ctx);
            String url = this.getRequestURL();
            Log.d("URL",url);
            HttpCommunicatorStringRequest stringRequest = new HttpCommunicatorStringRequest(
                    Request.Method.POST,
                    url,
                    this,
                    this,
                    this.getPostParams()
            );
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    5000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
            );

            queue.add(stringRequest);
        } catch (IReportException ex) {
            ex.printStackTrace();
        }
    }

    /********************************************************************/

    /*

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
            protected Map<String, String> getPostParams() throws AuthFailureError {
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
            protected Map<String, String> getPostParams() throws AuthFailureError {
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
            protected Map<String, String> getPostParams() throws AuthFailureError {
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

