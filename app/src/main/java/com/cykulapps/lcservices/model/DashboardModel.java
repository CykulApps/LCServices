package com.cykulapps.lcservices.model;

public class DashboardModel
{
    String header;
    String headerValue;
    String departID;

    public DashboardModel(String departID, String header, String headerValue) {
        this.header = header;
        this.headerValue = headerValue;
        this.departID = departID;
    }

    public String getDepartID() {
        return departID;
    }

    public void setDepartID(String departID) {
        this.departID = departID;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getHeaderValue() {
        return headerValue;
    }

    public void setHeaderValue(String headerValue) {
        this.headerValue = headerValue;
    }
}
