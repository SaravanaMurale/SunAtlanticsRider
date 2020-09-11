package com.example.sunatlanticsrider.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sunatlanticsrider.R;
import com.example.sunatlanticsrider.adapter.PastOrderAdapter;
import com.example.sunatlanticsrider.model.BaseResponse;
import com.example.sunatlanticsrider.model.MyPastOrderResponse;
import com.example.sunatlanticsrider.model.PreviousOrderedResponseDTO;
import com.example.sunatlanticsrider.retrofit.ApiClient;
import com.example.sunatlanticsrider.retrofit.ApiInterface;
import com.example.sunatlanticsrider.utils.LoaderUtil;
import com.example.sunatlanticsrider.utils.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyPastOrdersFragment extends Fragment {

    RecyclerView pastOrderRecyclerView;
    PastOrderAdapter pastOrderAdapter;
    List<MyPastOrderResponse> myPastOrderResponseList;

    Dialog dialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_pastorder_fragment, null);

        pastOrderRecyclerView = (RecyclerView) view.findViewById(R.id.pastOrderRecyclerView);
        pastOrderRecyclerView.setHasFixedSize(true);
        pastOrderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        myPastOrderResponseList = new ArrayList<>();

        pastOrderAdapter = new PastOrderAdapter(getActivity(), myPastOrderResponseList);
        pastOrderRecyclerView.setAdapter(pastOrderAdapter);

        getMyPastOrderList();

        return view;
    }

    private void getMyPastOrderList() {

        dialog = LoaderUtil.showProgressBar(getActivity());

        ApiInterface apiInterface = ApiClient.getAPIClient().create(ApiInterface.class);

        String token = PreferenceUtil.getValueString(getActivity(), PreferenceUtil.BEARER) + " " + PreferenceUtil.getValueString(getActivity(), PreferenceUtil.AUTH_TOKEN);

        int userId = PreferenceUtil.getValueInt(getActivity(), PreferenceUtil.USER_ID);

        Call<PreviousOrderedResponseDTO> call = apiInterface.getmyPreviousOrders(token, userId);
        call.enqueue(new Callback<PreviousOrderedResponseDTO>() {
            @Override
            public void onResponse(Call<PreviousOrderedResponseDTO> call, Response<PreviousOrderedResponseDTO> response) {
                PreviousOrderedResponseDTO previousOrderedResponseDTO = response.body();

                if (previousOrderedResponseDTO != null) {
                    List<MyPastOrderResponse> myPastOrderResponses = previousOrderedResponseDTO.getPreviousOrderedResponseDTOList();
                    pastOrderAdapter.setData(myPastOrderResponses);

                    LoaderUtil.dismisProgressBar(getActivity(), dialog);
                } else {
                    LoaderUtil.dismisProgressBar(getActivity(), dialog);
                    Toast.makeText(getContext(), "You dont have any previous order", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<PreviousOrderedResponseDTO> call, Throwable t) {

            }
        });


    }
}
