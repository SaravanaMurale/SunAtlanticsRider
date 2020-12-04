package com.example.sunatlanticsrider.model;

import com.google.gson.annotations.SerializedName;

public class RegisterRiderRequest {

    @SerializedName("name")
    String riderUserName;
    @SerializedName("mobile_no")
    String riderMobileNumber;
    @SerializedName("password")
    String riderPassword;
    @SerializedName("latitude")
    Double riderLat;
    @SerializedName("longitude")
    Double riderLongi;
    @SerializedName("delivery_area")
    String riderEmail;


    public RegisterRiderRequest(String riderUserName, String riderMobileNumber, String riderPassword, Double riderLat, Double riderLongi, String riderEmail) {
        this.riderUserName = riderUserName;
        this.riderMobileNumber = riderMobileNumber;
        this.riderPassword = riderPassword;
        this.riderLat = riderLat;
        this.riderLongi = riderLongi;
        this.riderEmail = riderEmail;
    }

    public String getRiderUserName() {
        return riderUserName;
    }

    public void setRiderUserName(String riderUserName) {
        this.riderUserName = riderUserName;
    }

    public String getRiderMobileNumber() {
        return riderMobileNumber;
    }

    public void setRiderMobileNumber(String riderMobileNumber) {
        this.riderMobileNumber = riderMobileNumber;
    }

    public String getRiderPassword() {
        return riderPassword;
    }

    public void setRiderPassword(String riderPassword) {
        this.riderPassword = riderPassword;
    }

    public Double getRiderLat() {
        return riderLat;
    }

    public void setRiderLat(Double riderLat) {
        this.riderLat = riderLat;
    }

    public Double getRiderLongi() {
        return riderLongi;
    }

    public void setRiderLongi(Double riderLongi) {
        this.riderLongi = riderLongi;
    }

    public String getRiderEmail() {
        return riderEmail;
    }

    public void setRiderEmail(String riderEmail) {
        this.riderEmail = riderEmail;
    }
}
