package com.ireport.activities;

/**
 * Created by Somya on 12/3/2016.
 */

public interface ICallbackActivity {
    public void onPostProcessCompletion(Object responseObj, String identifier, boolean isSuccess);

}
