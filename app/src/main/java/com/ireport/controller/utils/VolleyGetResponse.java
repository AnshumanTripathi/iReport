package com.ireport.controller.utils;

import android.util.Log;

import com.android.volley.Response;

import org.json.JSONObject;

/**
 * Created by Somya on 12/2/2016.
 */

public class VolleyGetResponse implements Response.Listener <String>{
    public VolleyGetResponse() {

    }

    @Override
    public void onResponse(String response) {
        try {
            // Display the first 500 characters of the response string.
            JSONObject json = new JSONObject(response.toString());
            Log.d("Json object",json.toString());

            String jsonData =  json.getString("data");
            //UserInfo uiObj = parseUserInfoFromJson(jsonData);
            Log.d("USER",jsonData.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
