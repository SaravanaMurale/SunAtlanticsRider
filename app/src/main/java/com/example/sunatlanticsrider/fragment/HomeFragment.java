package com.example.sunatlanticsrider.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sunatlanticsrider.R;
import com.example.sunatlanticsrider.activity.MapActivity;
import com.example.sunatlanticsrider.adapter.OrdersAdapter;
import com.example.sunatlanticsrider.model.BaseResponse;
import com.example.sunatlanticsrider.model.OrderRequest;
import com.example.sunatlanticsrider.model.OrderResponseDTO;
import com.example.sunatlanticsrider.model.OrdersResponse;
import com.example.sunatlanticsrider.retrofit.ApiClient;
import com.example.sunatlanticsrider.retrofit.ApiInterface;
import com.example.sunatlanticsrider.utils.LoaderUtil;
import com.example.sunatlanticsrider.utils.PermissionUtils;
import com.example.sunatlanticsrider.utils.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sunatlanticsrider.utils.AppConstant.LOCATION_PERMISSION_REQUEST_CODE;

public class HomeFragment extends Fragment implements OrdersAdapter.OnOrderClickListener {


    private RecyclerView myCurrentOrderRecyclerView;
    private OrdersAdapter ordersAdapter;
    private List<OrdersResponse> ordersResponseList;

    Dialog dialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_home_fragment, null);

        myCurrentOrderRecyclerView = (RecyclerView) view.findViewById(R.id.myCurrentOrderRecyclerView);
        myCurrentOrderRecyclerView.setHasFixedSize(true);
        myCurrentOrderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        ordersResponseList = new ArrayList<>();

        ordersAdapter = new OrdersAdapter(getActivity(), ordersResponseList, HomeFragment.this);

        myCurrentOrderRecyclerView.setAdapter(ordersAdapter);


        getMyCurrentOrderDetails();

        return view;

    }

    private void getMyCurrentOrderDetails() {

        ApiInterface apiInterface = ApiClient.getAPIClient().create(ApiInterface.class);

        String token = PreferenceUtil.getValueString(getActivity(), PreferenceUtil.BEARER) + " " + PreferenceUtil.getValueString(getActivity(), PreferenceUtil.AUTH_TOKEN);

        int userId = PreferenceUtil.getValueInt(getActivity(), PreferenceUtil.USER_ID);

        System.out.println("USERID "+userId+" "+token);

        Call<OrderResponseDTO> call = apiInterface.getMyCurrentOrders(token, userId);

        System.out.println("Iamhere");

        call.enqueue(new Callback<OrderResponseDTO>() {
            @Override
            public void onResponse(Call<OrderResponseDTO> call, Response<OrderResponseDTO> response) {

                System.out.println("OrderResponse" + response.body());

                OrderResponseDTO orderResponseDTO = response.body();
                List<OrdersResponse> ordersResponses = orderResponseDTO.getOrdersResponseList();

                ordersAdapter.setData(ordersResponses);


            }

            @Override
            public void onFailure(Call<OrderResponseDTO> call, Throwable t) {
                System.out.println("Error" + t.getMessage().toString());
            }
        });


    }


    @Override
    public void onOrderClick(OrdersResponse ordersResponse) {


        checkLocationPermission(ordersResponse);


    }

    private void checkLocationPermission(OrdersResponse ordersResponse) {

        if (!PermissionUtils.hasPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                && !PermissionUtils.hasPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {

            PermissionUtils.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);


        } else {

            if (PermissionUtils.hasPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    && PermissionUtils.hasPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {

                String trackNum = PreferenceUtil.getValueString(getActivity(), PreferenceUtil.TRACKING_NUM);

                if (!trackNum.equals(ordersResponse.getTrackingNum())) {
                    updateStatusInProgressToOnDelivery(ordersResponse);
                }


                Intent intent = new Intent(getActivity(), MapActivity.class);
                intent.putExtra("LAT", ordersResponse.getDeliveryLat());
                intent.putExtra("LON", ordersResponse.getDeliveryLongi());
                intent.putExtra("TRACKNUM", ordersResponse.getTrackingNum());
                startActivity(intent);


            }

        }

    }

    private void updateStatusInProgressToOnDelivery(final OrdersResponse ordersResponse) {

        dialog = LoaderUtil.showProgressBar(getActivity());

        ApiInterface apiInterface = ApiClient.getAPIClient().create(ApiInterface.class);

        OrderRequest orderRequest = new OrderRequest(PreferenceUtil.getValueInt(getActivity(), PreferenceUtil.USER_ID), ordersResponse.getTrackingNum());
        String token = PreferenceUtil.getValueString(getActivity(), PreferenceUtil.BEARER) + " " + PreferenceUtil.getValueString(getActivity(), PreferenceUtil.AUTH_TOKEN);
        Call<BaseResponse> call = apiInterface.updateDeliveryProgressStatus(token,orderRequest);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                BaseResponse baseResponse = response.body();


                if (baseResponse.getSuccess()) {
                    System.out.println("InProgressToOnDelivery");

                    PreferenceUtil.setValueString(getActivity(), PreferenceUtil.TRACKING_NUM, ordersResponse.getTrackingNum());

                }

                LoaderUtil.dismisProgressBar(getActivity(), dialog);

            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

            }
        });


    }
}
