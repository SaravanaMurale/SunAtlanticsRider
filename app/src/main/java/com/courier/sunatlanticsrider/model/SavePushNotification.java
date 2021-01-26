package com.courier.sunatlanticsrider.model;

import com.google.gson.annotations.SerializedName;

public class SavePushNotification {

    @SerializedName("userId")
    public int userId;
    @SerializedName("token")
    public String token;

    public SavePushNotification(int userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
