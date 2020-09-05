package com.example.sunatlanticsrider.model;

import com.google.gson.annotations.SerializedName;

public class EmailUpdateRequest extends UserIdDTO {

    @SerializedName("email")
    private String updateUserEmail;


    public EmailUpdateRequest(int userId, String updateUserEmail) {
        super(userId);
        this.updateUserEmail = updateUserEmail;
    }

    public String getUpdateUserEmail() {
        return updateUserEmail;
    }
}
