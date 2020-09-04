package com.example.sunatlanticsrider.model;

import com.google.gson.annotations.SerializedName;

public class OrderRequest {

    @SerializedName("userId")
    private int userId;

    @SerializedName("trackno")
    private String trackNo;

    public OrderRequest(int userId, String trackNo) {
        this.userId = userId;
        this.trackNo = trackNo;
    }

    public int getUserId() {
        return userId;
    }

    public String getTrackNo() {
        return trackNo;
    }
}
