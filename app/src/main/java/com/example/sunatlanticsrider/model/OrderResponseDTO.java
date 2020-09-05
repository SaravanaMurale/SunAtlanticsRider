package com.example.sunatlanticsrider.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderResponseDTO {

    @SerializedName("data")
    List<OrdersResponse> ordersResponseList;

    public List<OrdersResponse> getOrdersResponseList() {
        return ordersResponseList;
    }

    public void setOrdersResponseList(List<OrdersResponse> ordersResponseList) {
        this.ordersResponseList = ordersResponseList;
    }
}
