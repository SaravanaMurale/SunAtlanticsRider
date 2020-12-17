package com.courier.sunatlanticsrider.model;

import com.google.gson.annotations.SerializedName;

public class BaseResponse {

    @SerializedName("success")
    private boolean success;
    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public boolean isSuccess() {
        return success;
    }

    public boolean getStatus() {
        return status;
    }

    public BaseResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
