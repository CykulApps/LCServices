package com.cykulapps.lcservices.model;

public class SubActivityModel
{
    private String deptName;
    private String depturl;
    private String category;

    public SubActivityModel() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public SubActivityModel(String deptName, String depturl) {
        this.deptName = deptName;
        this.depturl = depturl;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDepturl() {
        return depturl;
    }

    public void setDepturl(String depturl) {
        this.depturl = depturl;
    }
}
