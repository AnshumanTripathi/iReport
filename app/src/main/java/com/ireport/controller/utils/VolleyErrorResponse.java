package com.ireport.controller.utils;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by Somya on 12/2/2016.
 */

public class VolleyErrorResponse implements Response.ErrorListener {

    public VolleyErrorResponse() {

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("ERROR","Error while communicating with the server");
        error.printStackTrace();
    }
}
