package com.example.sunatlanticsrider.adapter;

import android.content.Context;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sunatlanticsrider.R;
import com.example.sunatlanticsrider.model.OrdersResponse;
import com.example.sunatlanticsrider.utils.GpsUtils;
import com.example.sunatlanticsrider.utils.MathUtil;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder> {

    private Context mCtx;
    List<OrdersResponse> ordersResponseList;
    OnOrderClickListener onOrderClickListener;

    public OrdersAdapter(Context mCtx, List<OrdersResponse> ordersResponseList, OnOrderClickListener onOrderClickListener) {
        this.mCtx = mCtx;
        this.ordersResponseList = ordersResponseList;
        this.onOrderClickListener = onOrderClickListener;
    }

    public void setData(List<OrdersResponse> ordersResponseList) {
        this.ordersResponseList = ordersResponseList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(mCtx);
        View view = layoutInflater.inflate(R.layout.layout_order, parent, false);

        return new OrdersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {

        holder.trackingNum.setText("" + ordersResponseList.get(position).getTrackingNum());
        holder.avgCost.setText("" + ordersResponseList.get(position).getPrice());

        String strL = ordersResponseList.get(position).getDeliveryLat();
        String strLong = ordersResponseList.get(position).getDeliveryLongi();
        System.out.println("YESSSS" + strL + " " + strLong);

        /*Double myLocationLat=new Double(strL);
        Double myLocationLon=new Double(strLong);
*/
        double myLocationLat = MathUtil.stringToDouble(strL);
        double myLocationLon = MathUtil.stringToDouble(strLong);


        List<Address> geoAddresses = GpsUtils.getAddressFromMap(mCtx, myLocationLat, myLocationLon);

        if (geoAddresses.size() != 0) {

            String address = geoAddresses.get(0).getAddressLine(0);
            String area = geoAddresses.get(0).getLocality();
            String city = geoAddresses.get(0).getAdminArea();
            String country = geoAddresses.get(0).getCountryName();
            String postalCode = geoAddresses.get(0).getPostalCode();
            String subAdminArea = geoAddresses.get(0).getSubAdminArea();

            ordersResponseList.get(position).setDeliveryAddress(address + " " + area + " " + city + " " + subAdminArea + " " + postalCode);
        }


        holder.deliveryAddr.setText(ordersResponseList.get(position).getDeliveryAddress());


    }

    @Override
    public int getItemCount() {
        return ordersResponseList == null ? 0 : ordersResponseList.size();
    }


    class OrdersViewHolder extends RecyclerView.ViewHolder {

        TextView trackingNum, deliveryAddr, avgCost;

        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            trackingNum = (TextView) itemView.findViewById(R.id.trackNumber);
            deliveryAddr = (TextView) itemView.findViewById(R.id.address);
            avgCost = (TextView) itemView.findViewById(R.id.avgPrice);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OrdersResponse ordersResponse = ordersResponseList.get(getAdapterPosition());
                    onOrderClickListener.onOrderClick(ordersResponse);

                }
            });

        }
    }

    public interface OnOrderClickListener {

        public void onOrderClick(OrdersResponse ordersResponse);

    }


}
