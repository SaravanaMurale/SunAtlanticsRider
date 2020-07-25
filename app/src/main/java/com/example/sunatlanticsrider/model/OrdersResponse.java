package com.example.sunatlanticsrider.model;

public class OrdersResponse {

    private int trackingNum;
    private String deliveryAddr;
    private int avgCost;

    public OrdersResponse(int trackingNum, String deliveryAddr, int avgCost) {
        this.trackingNum = trackingNum;
        this.deliveryAddr = deliveryAddr;
        this.avgCost = avgCost;
    }

    public int getTrackingNum() {
        return trackingNum;
    }

    public void setTrackingNum(int trackingNum) {
        this.trackingNum = trackingNum;
    }

    public String getDeliveryAddr() {
        return deliveryAddr;
    }

    public void setDeliveryAddr(String deliveryAddr) {
        this.deliveryAddr = deliveryAddr;
    }

    public int getAvgCost() {
        return avgCost;
    }

    public void setAvgCost(int avgCost) {
        this.avgCost = avgCost;
    }
}
