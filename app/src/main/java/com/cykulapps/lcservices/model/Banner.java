package com.cykulapps.lcservices.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Banner {

@SerializedName("departmentID")
@Expose
private String departmentID;
@SerializedName("image")
@Expose
private String image;
@SerializedName("qrCode")
@Expose
private String qrCode;

public String getDepartmentID() {
return departmentID;
}

public void setDepartmentID(String departmentID) {
this.departmentID = departmentID;
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

}