package com.courier.sunatlanticsrider.model;

import com.google.gson.annotations.SerializedName;

public class UserNameUpdateRequest extends UserIdDTO {

    @SerializedName("name")
    private String updateNameUpdate;


    public UserNameUpdateRequest(int userId, String updateNameUpdate) {
        super(userId);
        this.updateNameUpdate = updateNameUpdate;
    }

    public String getUpdateNameUpdate() {
        return updateNameUpdate;
    }
}
