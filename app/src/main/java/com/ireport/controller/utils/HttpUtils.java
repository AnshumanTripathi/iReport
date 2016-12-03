package com.ireport.controller.utils;

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

/**
 * Created by Somya on 11/30/2016.
 */
public class HttpUtils {

    //Get : Getallusers
    public static String sendHttpGetRequest() {
        Log.d("GET","Sending HTTP get Request through Volley");

        String retResponse = null;
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.19.1:3000/getAllUsers";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                url,
                new VolleyGetResponse(),
                new VolleyErrorResponse()
        );

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        );

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

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
    public static void testHTTPPOST_Volley() {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.19.1:3000/getUser";

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new VolleyPostResponse(),
                new VolleyErrorResponse()
        )

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                /*String userinfo = "";
                Map<String,String> params = new HashMap<String, String>();
                params.put("email","abc");
                params.put("first_name", "wer");
                params.put("last_name" , "jfij");
                params.put("home_address", "");
                params.put("screen_name", "");
                JSONObject jo = new JSONObject(params);

                userinfo = jo.toString();

                userinfo += ",  \"settings\" : {\n" +
                        "    \"email_confirm\" : true,\n" +
                        "    \"email_notify\" : true,\n" +
                        "    \"anonymous\" : false\n" +
                        "  }\n";

                Map<String,String> retParams = new HashMap<>();
                retParams.put("user",userinfo);


                Log.d("PARAMS", retParams.toString());

                return retParams;*/

                return getUserByEmail("sanjay_dutt@email.com");
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
    public static void volley_updateSettings() {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.19.1:3000/updateSettings";

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new VolleyPostResponse(),
                new VolleyErrorResponse()
        )

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return updateSettings(new Settings(false,false,false), "sanjay_dutt@email.com");
            }
        };

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
        String url = "http://192.168.19.1:3000/updateUserInfo";

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
        String url = "http://192.168.19.1:3000/addUser";

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
}

