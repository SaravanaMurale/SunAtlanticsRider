package com.courier.sunatlanticsrider.model;

import com.google.gson.annotations.SerializedName;

public class GetToeknResponse {

    @SerializedName("token")
    private String token;


    public GetToeknResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}


