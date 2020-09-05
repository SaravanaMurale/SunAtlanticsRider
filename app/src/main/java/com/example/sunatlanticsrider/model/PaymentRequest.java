package com.example.sunatlanticsrider.model;

import com.google.gson.annotations.SerializedName;

public class PaymentRequest {

    @SerializedName("detail")
    private String paymentDetail;
    @SerializedName("amount")
    private int payAmt;
    @SerializedName("order_id")
    private String payOrderId;
    @SerializedName("name")
    private String payUserName;
    @SerializedName("email")
    private String payEmail;
    @SerializedName("phone")
    private String payMobileNum;

    public PaymentRequest(String paymentDetail, int payAmt, String payOrderId, String payUserName, String payEmail, String payMobileNum) {
        this.paymentDetail = paymentDetail;
        this.payAmt = payAmt;
        this.payOrderId = payOrderId;
        this.payUserName = payUserName;
        this.payEmail = payEmail;
        this.payMobileNum = payMobileNum;
    }

    public String getPaymentDetail() {
        return paymentDetail;
    }

    public int getPayAmt() {
        return payAmt;
    }

    public String getPayOrderId() {
        return payOrderId;
    }

    public String getPayUserName() {
        return payUserName;
    }

    public String getPayEmail() {
        return payEmail;
    }

    public String getPayMobileNum() {
        return payMobileNum;
    }
}
