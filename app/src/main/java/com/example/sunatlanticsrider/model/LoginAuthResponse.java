package com.example.sunatlanticsrider.model;

import com.google.gson.annotations.SerializedName;

public class LoginAuthResponse extends ErrorResponse{

    @SerializedName("access_token")
    String authToken;

    @SerializedName("token_type")
    String tokenType;

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
