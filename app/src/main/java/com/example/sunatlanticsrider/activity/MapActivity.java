package com.example.sunatlanticsrider.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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

import com.example.sunatlanticsrider.R;
import com.example.sunatlanticsrider.utils.GpsUtils;
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
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import static com.example.sunatlanticsrider.utils.AppConstant.GPS_PROVIDER_CODE;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {

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

    Button clickMe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapActivity.this);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


        enableGPS();


    }

    private void callDirectionsAPI() {

        if (myCurrentLocation.getPosition() == null && deliveryLocation.getPosition() == null) {

            // Toast.makeText(MapActivity.this, "Position value Is Null", Toast.LENGTH_LONG).show();


        } else {
            System.out.println("MyPositionURL" + myCurrentLocation.getPosition() + " " + deliveryLocation.getPosition());
            String url = getUrl(myCurrentLocation.getPosition(), deliveryLocation.getPosition(), "driving");
            new FetchURL(MapActivity.this).execute(url, "driving");


        }


    }

    private void addMarker(Double lat, Double longi) {
        myCurrentLocation = new MarkerOptions().position(new LatLng(myLocationLat, myLocationLon)).title("Murali");
        myCurrentLocationMarker = mGoogleMap.addMarker(myCurrentLocation);
        myCurrentLocationMarker.showInfoWindow();

        deliveryLocation = new MarkerOptions().position(new LatLng(13.0087, 80.2130)).title("Guindy");
        deliveryLocationMarker = mGoogleMap.addMarker(deliveryLocation);
        deliveryLocationMarker.showInfoWindow();

    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + "AIzaSyCnG_pJ7ZVHK3CyT1Y8OG_ortNhgvJbxBQ";
        return url;
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

                                addMarker(myLocationLat, myLocationLon);

                                /*new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        addMarker(myLocationLat, myLocationLon);
                                    }
                                }, 2000);*/

                                callDirectionsAPI();

                                /*new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        callDirectionsAPI();
                                    }
                                }, 2000);*/


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

                                            addMarker(myLocationLat, myLocationLon);
                                            callDirectionsAPI();

                                           /* new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    addMarker(myLocationLat, myLocationLon);
                                                }
                                            }, 2000);

                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    callDirectionsAPI();
                                                }
                                            }, 2000);*/


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

        //Dialog dialog=LoaderUtil.showProgressBar(MapActivity.this);

        // getAddressFromLatiandLongi(myLocationLat, myLocationLon);

        // LoaderUtil.dismisProgressBar(MapActivity.this,dialog);


    }

    private void gpsCheck() {
        new GpsUtils(MapActivity.this).turnGPSOn(new GpsUtils.onGpsListener() {
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


                Toast.makeText(MapActivity.this, "GetDeviceLocationnn", Toast.LENGTH_LONG).show();


            }

        } else {
            // Toast.makeText(MapActivity.this, "Open Map", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onTaskDone(Object... values) {

        Toast.makeText(MapActivity.this, "PolyLine Interface Is called", Toast.LENGTH_LONG).show();
        if (currentPolyLine != null) {
            currentPolyLine.remove();
            currentPolyLine = mGoogleMap.addPolyline((PolylineOptions) values[0]);
        } else {
            currentPolyLine = mGoogleMap.addPolyline((PolylineOptions) values[0]);
        }

    }
}