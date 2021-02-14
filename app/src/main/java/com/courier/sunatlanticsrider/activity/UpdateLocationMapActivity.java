package com.courier.sunatlanticsrider.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.courier.sunatlanticsrider.R;
import com.courier.sunatlanticsrider.utils.GpsUtils;
import com.courier.sunatlanticsrider.utils.LoaderUtil;
import com.courier.sunatlanticsrider.utils.PreferenceUtil;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class UpdateLocationMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private View mapView;
    private LocationCallback locationCallback;

    private LocationManager locationManager;

    Double myLocationLat, myLocationLon;
    private Location mLastKnownLocation;
    public boolean isGPS;
    private final float DEFAULT_ZOOM = 13;

    TextView updateConfirmLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_update_location_map);

        updateConfirmLoc=(TextView)findViewById(R.id.updateConfirmLoc);



        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(UpdateLocationMapActivity.this);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        enableGPS();

        updateConfirmLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PreferenceUtil.setValueString(UpdateLocationMapActivity.this,PreferenceUtil.UPDATE_LAT,String.valueOf(myLocationLat));
                PreferenceUtil.setValueString(UpdateLocationMapActivity.this,PreferenceUtil.UPDATE_LONG,String.valueOf(myLocationLon));


                onBackPressed();
            }
        });

    }

    private void enableGPS() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Toast.makeText(MapActivity.this, "Get Device Location", Toast.LENGTH_LONG).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    getDeviceLocation();

                }
            }, 3000);


        } else {

            gpsCheck();

        }
    }

    private void gpsCheck() {

        new GpsUtils(UpdateLocationMapActivity.this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS*

                isGPS = isGPSEnable;

                System.out.println("ISGPS" + isGPS);

            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {

        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {


                        if (task.isSuccessful()) {
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {

                                System.out.println("MyLastKnownLocation " + mLastKnownLocation);

                                myLocationLat = mLastKnownLocation.getLatitude();
                                myLocationLon = mLastKnownLocation.getLongitude();

                                System.out.println("MyLocationLatLong" + myLocationLat + " " + myLocationLon);
                                moveCamera(myLocationLat, myLocationLon);

                            } else if (mLastKnownLocation == null) {

                                final LocationRequest locationRequest = LocationRequest.create();
                                locationRequest.setInterval(10000);
                                locationRequest.setFastestInterval(5000);
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

                                locationCallback = new LocationCallback() {

                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult(locationResult);

                                        mFusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

                                        if (locationResult == null) {
                                            return;
                                        } else if (locationResult != null) {

                                            mLastKnownLocation = locationResult.getLastLocation();

                                            myLocationLat = mLastKnownLocation.getLatitude();
                                            myLocationLon = mLastKnownLocation.getLongitude();

                                            System.out.println("MyLocationLatLong" + myLocationLat + " " + myLocationLon);

                                            moveCamera(myLocationLat, myLocationLon);


                                            mFusedLocationProviderClient.removeLocationUpdates(locationCallback);

                                        }


                                    }
                                };


                            }


                        }


                    }
                });


    }

    private void moveCamera(Double myLocationLat, Double myLocationLon) {

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLocationLat, myLocationLon), DEFAULT_ZOOM));

//        Dialog dialog=LoaderUtil.showProgressBar(MapActivity.this);

        //getAddressFromLatiandLongi(myLocationLat, myLocationLon);

        // LoaderUtil.dismisProgressBar(MapActivity.this,dialog);


    }
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(true);

        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {

            //Toolbar has the ID 4. ID 2 is for location icon.
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 0, 50);

        }

        mGoogleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                Double nextlatitude = mGoogleMap.getProjection().getVisibleRegion().latLngBounds.getCenter().latitude;
                Double nextlongitude = mGoogleMap.getProjection().getVisibleRegion().latLngBounds.getCenter().longitude;

                System.out.println("OnCameraIdleListenerLatLong" + nextlatitude + " " + nextlongitude);

                myLocationLat=nextlatitude;
                myLocationLon=nextlongitude;

                Dialog dialog= LoaderUtil.showProgressBar(UpdateLocationMapActivity.this);
               // getAddressFromLatiandLongi(nextlatitude, nextlongitude);
                LoaderUtil.dismisProgressBar(UpdateLocationMapActivity.this,dialog);
            }
        });

    }
}