package com.example.sunatlanticsrider.model;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("mobile_no")
    String mobileNumber;
    @SerializedName("password")
    String password;

    public LoginRequest(String mobileNumber, String password) {
        this.mobileNumber = mobileNumber;
        this.password = password;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getPassword() {
        return password;
    }
}
