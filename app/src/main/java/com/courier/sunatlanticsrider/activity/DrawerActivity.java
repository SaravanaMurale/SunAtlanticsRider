package com.courier.sunatlanticsrider.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.courier.sunatlanticsrider.R;
import com.courier.sunatlanticsrider.fragment.HomeFragment;
import com.courier.sunatlanticsrider.fragment.MyPastOrdersFragment;
import com.courier.sunatlanticsrider.fragment.MyProfileFragment;
import com.courier.sunatlanticsrider.model.BaseResponse;
import com.courier.sunatlanticsrider.model.GetToeknResponse;
import com.courier.sunatlanticsrider.model.SavePushNotification;
import com.courier.sunatlanticsrider.retrofit.ApiClient;
import com.courier.sunatlanticsrider.retrofit.ApiInterface;
import com.courier.sunatlanticsrider.utils.LoaderUtil;
import com.courier.sunatlanticsrider.utils.PreferenceUtil;
import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String previousTokenFromServer = "";
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //get Push Notification Token From Server

        getPushNotificationFromServer();
        String currentNotificationToken = PreferenceUtil.getValueString(DrawerActivity.this, PreferenceUtil.NOTIFICATION);
        System.out.println("CURRENTTOKEN" + currentNotificationToken);
        System.out.println("PREVIOUSTOKEN" + previousTokenFromServer);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 1000);

        if (previousTokenFromServer.isEmpty()) {
            previousTokenFromServer = "null";
        }


        if (previousTokenFromServer.equals(currentNotificationToken)) {

        } else if (!previousTokenFromServer.equals(currentNotificationToken)) {
            saveFirebaseNotificationTokenInServer();
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                triggerPushNotification();
            }
        }, 500);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Fragment quoteFragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.screenArea, quoteFragment);
        fragmentTransaction.commit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void triggerPushNotification() {
        dialog = LoaderUtil.showProgressBar(this);
        ApiInterface apiInterface = ApiClient.getAPIClient().create(ApiInterface.class);

        int userid = PreferenceUtil.getValueInt(DrawerActivity.this, PreferenceUtil.USER_ID);
        Call<BaseResponse> call = apiInterface.triggerNotificationFromServer(PreferenceUtil.getValueString(DrawerActivity.this, PreferenceUtil.BEARER) + " " + PreferenceUtil.getValueString(DrawerActivity.this, PreferenceUtil.AUTH_TOKEN), userid);

        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                System.out.println("NotificationTriggered");

                LoaderUtil.dismisProgressBar(DrawerActivity.this, dialog);
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                System.out.println("NotificationIsNotTriggered");
                LoaderUtil.dismisProgressBar(DrawerActivity.this, dialog);
            }
        });


    }

    private void saveFirebaseNotificationTokenInServer() {

        dialog = LoaderUtil.showProgressBar(this);
        ApiInterface apiInterface = ApiClient.getAPIClient().create(ApiInterface.class);

        /*int userid=PreferenceUtil.getValueInt(LoginActivity.this,PreferenceUtil.USER_ID);
       String pToken=PreferenceUtil.getValueString(LoginActivity.this,PreferenceUtil.NOTIFICATION);*/

        SavePushNotification loginResponse = new SavePushNotification(PreferenceUtil.getValueInt(DrawerActivity.this, PreferenceUtil.USER_ID), PreferenceUtil.getValueString(DrawerActivity.this, PreferenceUtil.NOTIFICATION));

        Call<BaseResponse> call = apiInterface.saveNotificationTokenInServer(PreferenceUtil.getValueString(DrawerActivity.this, PreferenceUtil.BEARER) + " " + PreferenceUtil.getValueString(DrawerActivity.this, PreferenceUtil.AUTH_TOKEN), loginResponse);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                BaseResponse baseResponse = response.body();
                LoaderUtil.dismisProgressBar(DrawerActivity.this, dialog);
                if (baseResponse != null) {
                    if (baseResponse.getSuccess()) {
                        System.out.println("TokenInsertedSuccessfully");
                    } else {
                        System.out.println("TokenIsNotInsertedSuccessfully");
                    }
                } else {
                    //return;
                }


            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                LoaderUtil.dismisProgressBar(DrawerActivity.this, dialog);
            }
        });


    }


    private void getPushNotificationFromServer() {

        dialog = LoaderUtil.showProgressBar(DrawerActivity.this);
        ApiInterface apiInterface = ApiClient.getAPIClient().create(ApiInterface.class);

        String token = PreferenceUtil.getValueString(DrawerActivity.this, PreferenceUtil.BEARER) + " " + PreferenceUtil.getValueString(DrawerActivity.this, PreferenceUtil.AUTH_TOKEN);
        int userId = PreferenceUtil.getValueInt(DrawerActivity.this, PreferenceUtil.USER_ID);

        Call<GetToeknResponse> call = apiInterface.getPushNotificationToken(token, userId);
        call.enqueue(new Callback<GetToeknResponse>() {
            @Override
            public void onResponse(Call<GetToeknResponse> call, Response<GetToeknResponse> response) {

                GetToeknResponse getToeknResponse = response.body();
                LoaderUtil.dismisProgressBar(DrawerActivity.this, dialog);
                if (getToeknResponse != null) {
                    previousTokenFromServer = getToeknResponse.getToken();
                } else {

                }


            }

            @Override
            public void onFailure(Call<GetToeknResponse> call, Throwable t) {
                LoaderUtil.dismisProgressBar(DrawerActivity.this, dialog);
            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        Fragment fragment = null;

        int id = item.getItemId();

        if (id == R.id.orderShipment) {

            fragment = new HomeFragment();

            /*Intent intent=new Intent(DrawerActivity.this,LoginActivity.class);
            startActivity(intent);*/

        } else if (id == R.id.myProfile) {

            fragment = new MyProfileFragment();

        } else if (id == R.id.myOrders) {

            fragment = new MyPastOrdersFragment();

        } else if (id == R.id.logOut) {

            callLoginActivity();

        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.screenArea, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void callLoginActivity() {

        PreferenceUtil.clear(DrawerActivity.this);

        Intent intent = new Intent(DrawerActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }
}
