package com.example.sunatlanticsrider.model;

public class MyPastOrderResponse {

    private String trackNo;

    private String fromLat;
    private String fromLongi;

    private String deliveryLat;
    private String deliveryLongi;

    private String fromFullAddress;
    private String toFullAddress;


    public String getTrackNo() {
        return trackNo;
    }

    public void setTrackNo(String trackNo) {
        this.trackNo = trackNo;
    }

    public String getFromLat() {
        return fromLat;
    }

    public void setFromLat(String fromLat) {
        this.fromLat = fromLat;
    }

    public String getFromLongi() {
        return fromLongi;
    }

    public void setFromLongi(String fromLongi) {
        this.fromLongi = fromLongi;
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

    public String getFromFullAddress() {
        return fromFullAddress;
    }

    public void setFromFullAddress(String fromFullAddress) {
        this.fromFullAddress = fromFullAddress;
    }

    public String getToFullAddress() {
        return toFullAddress;
    }

    public void setToFullAddress(String toFullAddress) {
        this.toFullAddress = toFullAddress;
    }
}
