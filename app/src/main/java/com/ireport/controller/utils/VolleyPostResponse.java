package com.ireport.controller.utils;

import android.util.Log;

import com.android.volley.Response;

import org.json.JSONObject;

/**
 * Created by Somya on 12/2/2016.
 */

public class VolleyPostResponse implements Response.Listener <String>{
    public VolleyPostResponse() {

    }

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
}
