package com.ireport.controller.utils;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
}

