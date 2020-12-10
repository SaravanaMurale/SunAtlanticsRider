package com.courier.sunatlanticsrider.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("id")
    int userId;

    @SerializedName("name")
    private String userName;
    @SerializedName("email")
    private String userEmail;
    @SerializedName("mobile_no")
    private String userMobileNumber;


    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserMobileNumber() {
        return userMobileNumber;
    }
}
