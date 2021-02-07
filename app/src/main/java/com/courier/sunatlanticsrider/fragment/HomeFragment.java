package com.courier.sunatlanticsrider.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.courier.sunatlanticsrider.R;
import com.courier.sunatlanticsrider.activity.DrawerActivity;
import com.courier.sunatlanticsrider.activity.MapActivity;
import com.courier.sunatlanticsrider.adapter.OrdersAdapter;
import com.courier.sunatlanticsrider.model.BaseResponse;
import com.courier.sunatlanticsrider.model.OrderRequest;
import com.courier.sunatlanticsrider.model.OrderResponseDTO;
import com.courier.sunatlanticsrider.model.OrdersResponse;
import com.courier.sunatlanticsrider.retrofit.ApiClient;
import com.courier.sunatlanticsrider.retrofit.ApiInterface;
import com.courier.sunatlanticsrider.utils.LoaderUtil;
import com.courier.sunatlanticsrider.utils.PermissionUtils;
import com.courier.sunatlanticsrider.utils.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.courier.sunatlanticsrider.utils.AppConstant.LOCATION_PERMISSION_REQUEST_CODE;

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


        return view;

    }

    @Override
    public void onResume() {
        super.onResume();

        getMyCurrentOrderDetails();

    }

    private void getMyCurrentOrderDetails() {

        dialog = LoaderUtil.showProgressBar(getActivity());

        ApiInterface apiInterface = ApiClient.getAPIClient().create(ApiInterface.class);

        String token = PreferenceUtil.getValueString(getActivity(), PreferenceUtil.BEARER) + " " + PreferenceUtil.getValueString(getActivity(), PreferenceUtil.AUTH_TOKEN);

        int userId = PreferenceUtil.getValueInt(getActivity(), PreferenceUtil.USER_ID);

        //System.out.println("USERID "+userId+" "+token);

        Call<OrderResponseDTO> call = apiInterface.getMyCurrentOrders(token, userId);


        call.enqueue(new Callback<OrderResponseDTO>() {
            @Override
            public void onResponse(Call<OrderResponseDTO> call, Response<OrderResponseDTO> response) {

                //System.out.println("OrderResponse" + response.body());
                LoaderUtil.dismisProgressBar(getActivity(), dialog);

                OrderResponseDTO orderResponseDTO = response.body();

                if (orderResponseDTO != null) {

                    List<OrdersResponse> ordersResponses = orderResponseDTO.getOrdersResponseList();

                    if (ordersResponses.size() == 0) {
                        LoaderUtil.dismisProgressBar(getActivity(), dialog);
                        Toast.makeText(getActivity(), "Currently you dont have any order to deliver", Toast.LENGTH_LONG).show();
                    } else if (ordersResponses.size() > 0) {
                        ordersAdapter.setData(ordersResponses);
                        LoaderUtil.dismisProgressBar(getActivity(), dialog);
                    }


                } else {
                    LoaderUtil.dismisProgressBar(getActivity(), dialog);
                    Toast.makeText(getActivity(), "Currently you dont have any order to deliver", Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<OrderResponseDTO> call, Throwable t) {
                LoaderUtil.dismisProgressBar(getActivity(), dialog);
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

                String trackNum = PreferenceUtil.getValueString(getActivity(), PreferenceUtil.TRACKING_NUM2);
                //int status2=PreferenceUtil.getValueInt(getActivity(),PreferenceUtil.STATUS_ACCEPT2);
                if (!trackNum.equals(ordersResponse.getTrackingNum())) {
                    //Pending To Process
                    updateStatusInProgressToOnDelivery(ordersResponse);
                }


                if (!trackNum.equals(ordersResponse.getTrackingNum())) {
                    //Process To OnDelivery
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
        Call<BaseResponse> call = apiInterface.updateDeliveryProgressStatus(token, orderRequest);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                BaseResponse baseResponse = response.body();
                LoaderUtil.dismisProgressBar(getActivity(), dialog);


                if (baseResponse.getSuccess()) {
                    System.out.println("InProgressToOnDelivery");

                    PreferenceUtil.setValueString(getActivity(), PreferenceUtil.TRACKING_NUM2, ordersResponse.getTrackingNum());
                    //PreferenceUtil.setValueSInt(getActivity(),PreferenceUtil.STATUS_ACCEPT2,2);

                }


            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                LoaderUtil.dismisProgressBar(getActivity(), dialog);
            }
        });


    }
}
