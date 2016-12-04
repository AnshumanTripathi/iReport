package com.ireport.controller.utils.httpUtils.APIHandlers;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

/**
 * Created by Somya on 12/3/2016.
 */

public class HttpCommunicatorStringRequest extends StringRequest {
    private Map<String, String> params_;
    public HttpCommunicatorStringRequest(
            int method, String url,
            Response.Listener<String> listener,
            Response.ErrorListener errorListener,
            Map<String, String> params
    ) {
        super(method, url, listener, errorListener);
        this.params_ = params;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return this.params_;
    }
}
