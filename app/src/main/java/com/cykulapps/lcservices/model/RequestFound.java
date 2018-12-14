package com.cykulapps.lcservices.model;

/**
 * Created by LSM on 28-Feb-18.
 */

public class RequestFound {
    String firstName;
    String lastName;
    String reqID;

    public RequestFound(String reqID, String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.reqID = reqID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setReqID(String reqID) {
        this.reqID = reqID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getReqID() {
        return reqID;
    }
}
