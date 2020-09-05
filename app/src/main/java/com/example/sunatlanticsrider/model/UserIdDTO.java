package com.example.sunatlanticsrider.model;

import com.google.gson.annotations.SerializedName;

public class UserIdDTO {

    @SerializedName("userId")
    int userId;

    public UserIdDTO(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }
}
