package com.courier.sunatlanticsrider.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.courier.sunatlanticsrider.R;
import com.courier.sunatlanticsrider.lilly.SignUpActivity;
import com.courier.sunatlanticsrider.utils.GpsUtils;
import com.courier.sunatlanticsrider.utils.PermissionUtils;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import static com.courier.sunatlanticsrider.utils.AppConstant.GPS_PROVIDER_CODE;

public class RiderLocationFetchActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private View mapView;

    private Location mLastKnownLocation;
    private LocationCallback locationCallback;

    private final float DEFAULT_ZOOM = 13;

    private LocationManager locationManager;
    Double myLocationLat, myLocationLon;

    public boolean isGPS;

    MarkerOptions myCurrentLocation, deliveryLocation;
    Marker myCurrentLocationMarker, deliveryLocationMarker;
    Polyline currentPolyLine;

    Button clickMe, getMyLocation;

    Double deliveryLocationLat, deliveryLocationLongi;
    String trackNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_location_fetch);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.riderMap);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(RiderLocationFetchActivity.this);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        getMyLocation = (Button) findViewById(R.id.getMyLocation);

        getMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //alert dialog


                AlertDialog.Builder builder = new AlertDialog.Builder(RiderLocationFetchActivity.this);

                builder.setMessage("Are you sure ");
                builder.setTitle("Rider Location Confirmation");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        PreferenceUtil.setValueString(RiderLocationFetchActivity.this,PreferenceUtil.USER_LAT,String.valueOf(myLocationLat));
                        PreferenceUtil.setValueString(RiderLocationFetchActivity.this,PreferenceUtil.USER_LONG,String.valueOf(myLocationLon));

                        onBackPressed();

                        /*Intent intent = new Intent(RiderLocationFetchActivity.this, SignUpActivity.class);

                        intent.putExtra("RIDER_LAT", myLocationLat);
                        intent.putExtra("RIDER_LONG", myLocationLon);
                        startActivity(intent);*/


                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();


                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });


            enableGPS();


    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        //mGoogleMap.getUiSettings().setCompassEnabled(true);

        //mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        //mGoogleMap.setPadding(0,0,0,100);

        //Assigning Position of location button at right bottom
        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {

            //Toolbar has the ID 4. ID 2 is for location icon.
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 0, 50);

        }
        //Assigning Position of location button at right bottom

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

                                //addMarker(myLocationLat, myLocationLon);


                                System.out.println("MyLocationLatLong" + myLocationLat + " " + myLocationLon);
                                moveCamera(myLocationLat, myLocationLon);
                                getMyLocation.setEnabled(true);

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
                                            getMyLocation.setEnabled(true);


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

        //Dialog dialog=LoaderUtil.showProgressBar(MapActivity.this);

        // getAddressFromLatiandLongi(myLocationLat, myLocationLon);

        // LoaderUtil.dismisProgressBar(MapActivity.this,dialog);


    }

    private void gpsCheck() {
        new GpsUtils(RiderLocationFetchActivity.this).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS*

                isGPS = isGPSEnable;

                System.out.println("ISGPS" + isGPS);

            }
        });

    }

    @Override
    public void
    onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GPS_PROVIDER_CODE) {
                // flag maintain before get location

                isGPS = true;


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getDeviceLocation();
                    }
                }, 1000);


                Toast.makeText(RiderLocationFetchActivity.this, "GetDeviceLocationnn", Toast.LENGTH_LONG).show();


            }

        } else {
            // Toast.makeText(MapActivity.this, "Open Map", Toast.LENGTH_LONG).show();
        }
    }


}