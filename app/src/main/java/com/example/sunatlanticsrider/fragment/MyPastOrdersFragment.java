package com.example.sunatlanticsrider.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sunatlanticsrider.R;
import com.example.sunatlanticsrider.adapter.PastOrderAdapter;
import com.example.sunatlanticsrider.model.MyPastOrderResponse;

import java.util.ArrayList;
import java.util.List;

public class MyPastOrdersFragment extends Fragment {

    RecyclerView pastOrderRecyclerView;
    PastOrderAdapter pastOrderAdapter;
    List<MyPastOrderResponse> myPastOrderResponseList;


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

        return view;
    }
}
