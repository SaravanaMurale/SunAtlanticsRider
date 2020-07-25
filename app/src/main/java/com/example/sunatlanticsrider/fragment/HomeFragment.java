package com.example.sunatlanticsrider.fragment;

import android.Manifest;
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
import com.example.sunatlanticsrider.model.OrdersResponse;
import com.example.sunatlanticsrider.utils.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

import static com.example.sunatlanticsrider.utils.AppConstant.LOCATION_PERMISSION_REQUEST_CODE;

public class HomeFragment extends Fragment implements OrdersAdapter.OnOrderClickListener {


    private RecyclerView myCurrentOrderRecyclerView;
    private OrdersAdapter ordersAdapter;
    private List<OrdersResponse> ordersResponseList;


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
        ordersResponseList.add(new OrdersResponse(111, "address", 500));
        ordersAdapter.setData(ordersResponseList);

    }

    @Override
    public void onOrderClick(OrdersResponse ordersResponse) {

        checkLocationPermission();


    }

    private void checkLocationPermission() {

        if (!PermissionUtils.hasPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                && !PermissionUtils.hasPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {

            PermissionUtils.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);


        } else {

            if (PermissionUtils.hasPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    && PermissionUtils.hasPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {


                Intent intent = new Intent(getActivity(), MapActivity.class);
                startActivity(intent);


            }

        }

    }
}
