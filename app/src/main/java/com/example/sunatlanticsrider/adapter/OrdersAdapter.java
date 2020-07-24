package com.example.sunatlanticsrider.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sunatlanticsrider.R;
import com.example.sunatlanticsrider.model.OrdersResponse;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder> {

    private Context mCtx;
    List<OrdersResponse> ordersResponseList;

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(mCtx);
        View view = layoutInflater.inflate(R.layout.layout_order, parent, false);

        return new OrdersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return ordersResponseList == null ? 0 : ordersResponseList.size();
    }


    class OrdersViewHolder extends RecyclerView.ViewHolder {

        TextView trackingNum,deliveryAddr,avgCost;

        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            trackingNum=(TextView)itemView.findViewById(R.id.trackNumber);
            deliveryAddr=(TextView)itemView.findViewById(R.id.address);
            avgCost=(TextView)itemView.findViewById(R.id.avgPrice);
        }
    }

}
