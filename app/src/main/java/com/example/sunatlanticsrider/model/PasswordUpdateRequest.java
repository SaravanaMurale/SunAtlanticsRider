package com.example.sunatlanticsrider.model;

import com.google.gson.annotations.SerializedName;

public class PasswordUpdateRequest  extends UserIdDTO{

    @SerializedName("password")
    private String passwordUpdate;

    public PasswordUpdateRequest(int userId, String passwordUpdate) {
        super(userId);
        this.passwordUpdate = passwordUpdate;
    }

    public String getPasswordUpdate() {
        return passwordUpdate;
    }
}
