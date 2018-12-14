package com.cykulapps.lcservices.model;

public class EventModel
{
   private String eventID;
   private String eventUrl;
   private String status;
   private String departID;
   private String category;
   private String deptName;

   public EventModel(){

   }

    public EventModel(String eventID, String eventUrl) {
        this.eventID = eventID;
        this.eventUrl = eventUrl;
    }

    public EventModel(String eventID,String departID, String eventUrl, String status) {
        this.eventID = eventID;
        this.eventUrl = eventUrl;
        this.status = status;
        this.departID = departID;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getEventUrl() {
        return eventUrl;
    }

    public void setEventUrl(String eventUrl) {
        this.eventUrl = eventUrl;
    }

    public String getDepartID() {
        return departID;
    }

    public void setDepartID(String departID) {
        this.departID = departID;
    }
}
