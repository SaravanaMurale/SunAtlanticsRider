package com.courier.sunatlanticsrider.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.courier.sunatlanticsrider.R;
import com.courier.sunatlanticsrider.model.BaseResponse;
import com.courier.sunatlanticsrider.model.OrderRequest;
import com.courier.sunatlanticsrider.model.OrdersResponse;
import com.courier.sunatlanticsrider.retrofit.ApiClient;
import com.courier.sunatlanticsrider.retrofit.ApiInterface;
import com.courier.sunatlanticsrider.utils.GpsUtils;
import com.courier.sunatlanticsrider.utils.LoaderUtil;
import com.courier.sunatlanticsrider.utils.MathUtil;
import com.courier.sunatlanticsrider.utils.PreferenceUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        String fullAddress = GpsUtils.getFullAddress(geoAddresses);

        ordersResponseList.get(position).setDeliveryAddress(fullAddress);


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

                    String trackNum = PreferenceUtil.getValueString(mCtx, PreferenceUtil.TRACKING_NUM1);
                    //int status1=PreferenceUtil.getValueInt(mCtx,PreferenceUtil.STATUS_ACCEPT1);

                    OrdersResponse ordersResponse = ordersResponseList.get(getAdapterPosition());
                    if (!trackNum .equals( ordersResponse.getTrackingNum())) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);

                        builder.setMessage("Are you sure you want to");
                        builder.setTitle("Order Taking");

                        builder.setPositiveButton("TAKE ORDER", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                //OrdersResponse ordersResponse = ordersResponseList.get(getAdapterPosition());

                                Toast.makeText(mCtx, "Thanks For Accepting", Toast.LENGTH_LONG).show();
                                OrdersResponse ordersResponse = ordersResponseList.get(getAdapterPosition());

                                PreferenceUtil.setValueString(mCtx, PreferenceUtil.TRACKING_NUM1, ordersResponse.getTrackingNum());
                                //PreferenceUtil.setValueSInt(mCtx,PreferenceUtil.STATUS_ACCEPT1,1);

                                updateOnDeliveryToDelivered(ordersResponse);

                            /*OrdersResponse ordersResponse = ordersResponseList.get(getAdapterPosition());
                            onOrderClickListener.onOrderClick(ordersResponse);*/

                            }
                        }).setNegativeButton("CANCEL ORDER", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.dismiss();


                            }
                        });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();


                    } else if (trackNum .equals( ordersResponse.getTrackingNum())) {

                        onOrderClickListener.onOrderClick(ordersResponse);
                    }


                }
            });

        }

        private void updateOnDeliveryToDelivered(OrdersResponse ordersResponse) {

            final Dialog dialog = LoaderUtil.showProgressBar(mCtx);

            ApiInterface apiInterface = ApiClient.getAPIClient().create(ApiInterface.class);

            OrderRequest orderRequest = new OrderRequest(PreferenceUtil.getValueInt(mCtx, PreferenceUtil.USER_ID), ordersResponse.getTrackingNum());
            String token = PreferenceUtil.getValueString(mCtx, PreferenceUtil.BEARER) + " " + PreferenceUtil.getValueString(mCtx, PreferenceUtil.AUTH_TOKEN);
            Call<BaseResponse> call = apiInterface.updateDeliveryProgressStatus(token, orderRequest);

            call.enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                    LoaderUtil.dismisProgressBar(mCtx, dialog);
                    if (response.isSuccessful()) {


                        OrdersResponse ordersResponse = ordersResponseList.get(getAdapterPosition());
                        onOrderClickListener.onOrderClick(ordersResponse);


                    }

                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {

                }
            });

        }
    }

    public interface OnOrderClickListener {

        public void onOrderClick(OrdersResponse ordersResponse);

    }


}
