package com.courier.sunatlanticsrider.model;

import com.google.gson.annotations.SerializedName;

public class LoginAuthResponse extends ErrorResponse{


    @SerializedName("success")
    boolean status;

    @SerializedName("access_token")
    String authToken;

    @SerializedName("token_type")
    String tokenType;

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
