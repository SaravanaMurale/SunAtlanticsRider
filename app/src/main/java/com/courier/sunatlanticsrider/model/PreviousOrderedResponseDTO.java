package com.courier.sunatlanticsrider.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PreviousOrderedResponseDTO {

    @SerializedName("data")
    private List<MyPastOrderResponse> previousOrderedResponseDTOList;

    public PreviousOrderedResponseDTO(List<MyPastOrderResponse> previousOrderedResponseDTOList) {
        this.previousOrderedResponseDTOList = previousOrderedResponseDTOList;
    }

    public List<MyPastOrderResponse> getPreviousOrderedResponseDTOList() {
        return previousOrderedResponseDTOList;
    }
}
