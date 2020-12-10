package com.courier.sunatlanticsrider.model;

import com.google.gson.annotations.SerializedName;

public class PreviousOrderResponse {

    @SerializedName("Username")
    private String orderedUserName;
    @SerializedName("ServiceName")
    private String serviceName;
    @SerializedName("latitude")
    private String deliveredLatl;
    @SerializedName("longitude")
    private String deliveredLongi;
    @SerializedName("trackno")
    private String orderedTrackNo;
    @SerializedName("price")
    private int price;
    @SerializedName("status")
    private String deliveredStatus;

    public String getOrderedUserName() {
        return orderedUserName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getDeliveredLatl() {
        return deliveredLatl;
    }

    public String getDeliveredLongi() {
        return deliveredLongi;
    }

    public String getOrderedTrackNo() {
        return orderedTrackNo;
    }

    public int getPrice() {
        return price;
    }

    public String getDeliveredStatus() {
        return deliveredStatus;
    }
}
