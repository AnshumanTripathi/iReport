package com.ireport.controller.utils;

import com.ireport.controller.IReportException;
import com.ireport.model.UserInfo;

/**
 * Created by Somya on 11/27/2016.
 */

public class UserSettingsUtils {

    // userId would be the email id of the user
    public UserInfo getUserInfo(String userId) throws IReportException {
        UserInfo ret = new UserInfo();
        // get the user info from server. throw exception if its an invalid user or there is some
        // communication error
        return ret;
    }

    public boolean updateUserInfo(UserInfo info) throws IReportException {
        return true;
    }
}
