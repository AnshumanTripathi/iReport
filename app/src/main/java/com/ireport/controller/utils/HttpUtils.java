package com.ireport.controller.utils;

import android.util.Log;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Somya on 11/30/2016.
 */
public class HttpUtils {

    public static String sendHttpGetRequest(String url) {
        String retResponse = null;
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        //String url = "http://ec2-35-165-22-113.us-west-2.compute.amazonaws.com:3000/getAllUsers";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Display the first 500 characters of the response string.
                            JSONObject json = new JSONObject(response.toString());
                            Log.d("Json object",json.toString());

                            String jsonData =  json.getString("data");
                            UserInfo uiObj = parseUserInfoFromJson(jsonData);


                    } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //did not work
                Log.d("ERROR","Response did not work!!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

        return "";
    }

    public static void testHTTPPOST_Volley() {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://ec2-35-165-22-113.us-west-2.compute.amazonaws.com:3000/getUser";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response: ", response.toString());
                try {
                    JSONObject json = new JSONObject(response.toString());
                    Log.d("Json object",json.toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error","");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("email","anshuman.tripathi305@gmail.com");
                return params;
            }
        };

        queue.add(stringRequest);
    }

    


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


}

