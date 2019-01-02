package com.cykulapps.lcservices.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

@SerializedName("departmentID")
@Expose
private String departmentID;
@SerializedName("category")
@Expose
private String category;
@SerializedName("departmentName")
@Expose
private String departmentName;
@SerializedName("alertMessage")
@Expose
private String alertMessage;
@SerializedName("image")
@Expose
private String image;
@SerializedName("qrCode")
@Expose
private String qrCode;
@SerializedName("eventID")
@Expose
private String eventID;
@SerializedName("price")
@Expose
private String price;

public String getDepartmentID() {
return departmentID;
}
public void setDepartmentID(String departmentID) {
this.departmentID = departmentID;}
public String getDepartmentName() {
        return departmentName;
    }
public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
public String getAlertMessage(){return alertMessage;}
public void setAlertMessage(String alertMessage){this.alertMessage = alertMessage;}
public String getCategory() {
        return category;
}
public void setCategory(String category){
    this.category = category;
}

public String getImage() {
return image;
}

public void setImage(String image) {
this.image = image;
}

public String getQrCode() {
return qrCode;
}

public void setQrCode(String qrCode) {
this.qrCode = qrCode;
}

public String getEventID() {
        return eventID;
    }
public void setEventID(String eventID) {
        this.eventID = eventID;
    }

 public String getPrice(){
    return price;
    }
public void setPrice(String price){
    this.price = price;
    }
}