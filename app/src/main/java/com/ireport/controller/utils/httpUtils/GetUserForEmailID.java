package com.ireport.controller.utils.httpUtils;

import com.ireport.controller.IReportException;

import java.util.Map;

/**
 * Created by Somya on 12/3/2016.
 */

public class GetUserForEmailID extends HttpBaseCommunicator {
    protected String getRequestURL() throws IReportException {
        throw new IReportException("getRequestURL method should be implemented");
    }

    protected void handleResponse(String response) throws IReportException {
        throw new IReportException("handleResponse method should be implemented");
    }

    protected Map<String, String> getParams() throws IReportException {
        throw new IReportException("getParams method should be implemented");
    }
}
