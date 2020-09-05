package com.example.sunatlanticsrider.model;

import com.google.gson.annotations.SerializedName;

public class OrdersResponse {

    @SerializedName("Username")
    private String userName;
    @SerializedName("ServiceName")
    private String serviceName;
    @SerializedName("latitude")
    private String deliveryLat;
    @SerializedName("longitude")
    private String deliveryLongi;
    @SerializedName("trackno")
    private String trackingNum;
    @SerializedName("price")
    private Double price;

    private String deliveryAddress;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDeliveryLat() {
        return deliveryLat;
    }

    public void setDeliveryLat(String deliveryLat) {
        this.deliveryLat = deliveryLat;
    }

    public String getDeliveryLongi() {
        return deliveryLongi;
    }

    public void setDeliveryLongi(String deliveryLongi) {
        this.deliveryLongi = deliveryLongi;
    }

    public String getTrackingNum() {
        return trackingNum;
    }

    public void setTrackingNum(String trackingNum) {
        this.trackingNum = trackingNum;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
}
