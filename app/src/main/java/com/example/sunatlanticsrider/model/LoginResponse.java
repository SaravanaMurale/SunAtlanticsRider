package com.example.sunatlanticsrider.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("id")
    int userId;

    public int getUserId() {
        return userId;
    }
}
