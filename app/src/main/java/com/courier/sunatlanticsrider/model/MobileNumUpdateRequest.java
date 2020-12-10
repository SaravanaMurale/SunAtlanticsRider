package com.courier.sunatlanticsrider.model;

import com.google.gson.annotations.SerializedName;

public class MobileNumUpdateRequest extends UserIdDTO {

    @SerializedName("mobile_no")
    String mobileNumUpdate;

    public MobileNumUpdateRequest(int userId, String mobileNumUpdate) {
        super(userId);
        this.mobileNumUpdate = mobileNumUpdate;
    }

    public String getMobileNumUpdate() {
        return mobileNumUpdate;
    }
}
