package com.courier.sunatlanticsrider.adapter;

import android.content.Context;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.courier.sunatlanticsrider.R;
import com.courier.sunatlanticsrider.model.MyPastOrderResponse;
import com.courier.sunatlanticsrider.utils.GpsUtils;
import com.courier.sunatlanticsrider.utils.MathUtil;

import java.util.List;

public class PastOrderAdapter extends RecyclerView.Adapter<PastOrderAdapter.PastOrderViewHolder> {

    private Context mCtx;
    private List<MyPastOrderResponse> myPastOrderResponseList;

    public PastOrderAdapter(Context mCtx, List<MyPastOrderResponse> myPastOrderResponseList) {
        this.mCtx = mCtx;
        this.myPastOrderResponseList = myPastOrderResponseList;
    }

    public void setData(List<MyPastOrderResponse> myPastOrderResponseList) {
        this.myPastOrderResponseList = myPastOrderResponseList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PastOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mCtx);
        View view = layoutInflater.inflate(R.layout.layout_my_orders_fragment, parent, false);

        return new PastOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PastOrderViewHolder holder, int position) {

        holder.deliveredTrackNum.setText(myPastOrderResponseList.get(position).getTrackNo());

        /*double fromLat = MathUtil.stringToDouble(myPastOrderResponseList.get(position).getFromLat());
        double fromLongi = MathUtil.stringToDouble(myPastOrderResponseList.get(position).getFromLongi());

        List<Address> fromGeoAddresses = GpsUtils.getAddressFromMap(mCtx, fromLat, fromLongi);
        String fullAddress = GpsUtils.getFullAddress(fromGeoAddresses);

        myPastOrderResponseList.get(position).setFromFullAddress(fullAddress);

        holder.fromAddr.setText(myPastOrderResponseList.get(position).getFromFullAddress()); */


        double toLat = MathUtil.stringToDouble(myPastOrderResponseList.get(position).getDeliveryLat());
        double toLongi = MathUtil.stringToDouble(myPastOrderResponseList.get(position).getDeliveryLongi());

        List<Address> toGeoAddresses = GpsUtils.getAddressFromMap(mCtx, toLat, toLongi);
        String fullToAddress = GpsUtils.getFullAddress(toGeoAddresses);

        myPastOrderResponseList.get(position).setToFullAddress(fullToAddress);

        holder.toAddr.setText(myPastOrderResponseList.get(position).getToFullAddress());


    }

    @Override
    public int getItemCount() {
        return myPastOrderResponseList == null ? 0 : myPastOrderResponseList.size();
    }

    class PastOrderViewHolder extends RecyclerView.ViewHolder {

        TextView deliveredTrackNum, fromAddr, toAddr;

        public PastOrderViewHolder(@NonNull View itemView) {
            super(itemView);

            deliveredTrackNum = (TextView) itemView.findViewById(R.id.deliveredTrackNo);
            fromAddr = (TextView) itemView.findViewById(R.id.fromAddress);
            toAddr = (TextView) itemView.findViewById(R.id.toAddress);

        }
    }
}
