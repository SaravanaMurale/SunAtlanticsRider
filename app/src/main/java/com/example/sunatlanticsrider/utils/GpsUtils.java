package com.example.sunatlanticsrider.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class GpsUtils {

    private Context context;
    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationManager locationManager;
    private LocationRequest locationRequest;

    public GpsUtils(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        mSettingsClient = LocationServices.getSettingsClient(context);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000);
        locationRequest.setFastestInterval(2 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        mLocationSettingsRequest = builder.build();
//**************************
        builder.setAlwaysShow(true); //this is the key ingredient
        //**************************
    }

    // method for turn on GPS
    public void turnGPSOn(final onGpsListener onGpsListener) {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (onGpsListener != null) {
                onGpsListener.gpsStatus(true);
            }
        } else if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            mSettingsClient
                    .checkLocationSettings(mLocationSettingsRequest)
                    .addOnSuccessListener((Activity) context, new OnSuccessListener<LocationSettingsResponse>() {
                        @SuppressLint("MissingPermission")
                        @Override
                        public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
//  GPS is already enable, callback GPS status through listener
                            if (onGpsListener != null) {
                                onGpsListener.gpsStatus(true);
                            }


                        }
                    })
                    .addOnFailureListener((Activity) context, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            int statusCode = ((ApiException) e).getStatusCode();
                            switch (statusCode) {
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    try {
                                        // Show the dialog by calling startResolutionForResult(), and check the
                                        // result in onActivityResult().
                                        ResolvableApiException rae = (ResolvableApiException) e;
                                        rae.startResolutionForResult((Activity) context, AppConstant.GPS_PROVIDER_CODE);
                                    } catch (IntentSender.SendIntentException sie) {
                                        Log.i(TAG, "PendingIntent unable to execute request.");
                                        System.out.println("PendingIntent");
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    String errorMessage = "Location settings are inadequate, and cannot be " +
                                            "fixed here. Fix in Settings.";
                                    Log.e(TAG, errorMessage);
                                    System.out.println("ErrorMessage" + errorMessage);
                                    Toast.makeText((Activity) context, errorMessage, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }



    public interface onGpsListener {
        void gpsStatus(boolean isGPSEnable);
    }


    //Converts coordinates into human readable address
    public static List<Address> getAddressFromMap(Context context, Double nextlatitude, Double nextlongitude) {

        List<Address> geoAddresses = new ArrayList<>();

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        try {
            geoAddresses = geocoder.getFromLocation(nextlatitude, nextlongitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return geoAddresses;


    }

    /*Double lat1=12.9929;
        Double lon1=80.2219;

        Double lat2=12.9803;
        Double lon2=80.1772;

       Double km= distance(lat1,lon1,lat2,lon2);*/


    private double distance(double storeLatitute, double storeLongitute, double deliveryLat, double deliveryLon) {
        double theta = storeLongitute - deliveryLon;
        double dist = Math.sin(deg2rad(storeLatitute))
                * Math.sin(deg2rad(deliveryLat))
                + Math.cos(deg2rad(storeLatitute))
                * Math.cos(deg2rad(deliveryLat))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }



}
