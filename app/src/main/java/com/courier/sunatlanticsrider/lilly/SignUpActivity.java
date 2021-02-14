package com.courier.sunatlanticsrider.lilly;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.courier.sunatlanticsrider.R;
import com.courier.sunatlanticsrider.activity.LoginActivity;
import com.courier.sunatlanticsrider.activity.RiderLocationFetchActivity;
import com.courier.sunatlanticsrider.activity.SplashScreen;
import com.courier.sunatlanticsrider.model.BaseResponse;
import com.courier.sunatlanticsrider.model.RegisterRiderRequest;
import com.courier.sunatlanticsrider.retrofit.ApiClient;
import com.courier.sunatlanticsrider.retrofit.ApiInterface;
import com.courier.sunatlanticsrider.utils.GpsUtils;
import com.courier.sunatlanticsrider.utils.LoaderUtil;
import com.courier.sunatlanticsrider.utils.PermissionUtils;
import com.courier.sunatlanticsrider.utils.PreferenceUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.courier.sunatlanticsrider.utils.AppConstant.LOCATION_PERMISSION_REQUEST_CODE;
import static com.courier.sunatlanticsrider.utils.MathUtil.validateMobile;
import static com.courier.sunatlanticsrider.utils.MathUtil.validateName;
import static com.courier.sunatlanticsrider.utils.MathUtil.validatePassword;

public class SignUpActivity extends AppCompatActivity {


    Button textView1;
    Button btnRiderSignUp;

    TextView riderLocationn;

    RelativeLayout riderLocationBlock;

    Dialog dialog;

    Double riderLat, riderLongi;

    private EditText signupName, signupMobile, signupPassword, signupLat, signupLongi, signupDeliveryArea;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        signupName = (EditText) findViewById(R.id.signUpUserName);
        signupMobile = (EditText) findViewById(R.id.signUpMobile);
        signupPassword = (EditText) findViewById(R.id.signUpPassword);


        riderLocationBlock = (RelativeLayout) findViewById(R.id.riderLocationBlock);
        riderLocationn = (TextView) findViewById(R.id.riderLocationn);
        btnRiderSignUp = (Button) findViewById(R.id.btnRiderSignUp);

        /*signupName.addTextChangedListener(new MyTextWatcher(signupName));
        signupMobile.addTextChangedListener(new MyTextWatcher(signupMobile));
        signupPassword.addTextChangedListener(new MyTextWatcher(signupPassword));*/

        btnRiderSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = signupName.getText().toString().trim();
                String userMobile = signupMobile.getText().toString().trim();
                String userPassword = signupPassword.getText().toString().trim();

                if (userName.isEmpty() || userName.equals("") || userName.equals(null)) {
                    Toast.makeText(SignUpActivity.this, "Please Enter Name", Toast.LENGTH_LONG).show();
                    return;

                }
                if (userMobile.isEmpty() || userMobile.equals("") || userMobile.equals(null)) {
                    Toast.makeText(SignUpActivity.this, "Please Enter Mobile Number", Toast.LENGTH_LONG).show();
                    return;

                }

                if (userPassword.isEmpty() || userPassword.equals("") || userPassword.equals(null)) {
                    Toast.makeText(SignUpActivity.this, "Please Enter Password", Toast.LENGTH_LONG).show();
                    return;

                }


                if (validateName(userName) && validateMobile(userMobile) && validatePassword(userPassword) && riderLat != null && riderLongi != null) {
                    registerRider();
                }


            }
        });

        riderLocationBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!PermissionUtils.hasPermission(SignUpActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        && !PermissionUtils.hasPermission(SignUpActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)) {

                    PermissionUtils.requestPermissions(SignUpActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);


                } else {

                    if (PermissionUtils.hasPermission(SignUpActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            && PermissionUtils.hasPermission(SignUpActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)) {


                        Intent intent = new Intent(SignUpActivity.this, RiderLocationFetchActivity.class);
                        startActivity(intent);
                        //getActivity().finish();


                    }


                }


            }
        });


    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {


        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String name = signupName.getText().toString().trim();
            String mobile = signupMobile.getText().toString().trim();
            String password = signupPassword.getText().toString().trim();


            btnRiderSignUp.setEnabled(validateMobile(name) && validateMobile(mobile) && validatePassword(password) && riderLat != null && riderLongi != null);

            if (btnRiderSignUp.isEnabled()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btnRiderSignUp.setBackground(getDrawable(R.drawable.rectangle_shpae));
                    btnRiderSignUp.setTextColor(getApplication().getResources().getColor(R.color.white));
                }
            } else if (!btnRiderSignUp.isEnabled()) {
                btnRiderSignUp.setEnabled(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btnRiderSignUp.setBackground(getDrawable(R.color.btn_disable));
                    btnRiderSignUp.setTextColor(getApplication().getResources().getColor(R.color.black));
                }
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private void registerRider() {

        dialog = LoaderUtil.showProgressBar(this);

        ApiInterface apiInterface = ApiClient.getAPIClient().create(ApiInterface.class);

        RegisterRiderRequest registerRiderRequest = new RegisterRiderRequest(signupName.getText().toString().trim(), signupMobile.getText().toString().trim(), signupPassword.getText().toString().trim(), riderLat, riderLongi, "Area");

        Call<BaseResponse> call = apiInterface.registerRider(registerRiderRequest);

        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                LoaderUtil.dismisProgressBar(SignUpActivity.this, dialog);

                if (response.isSuccessful()) {
                    BaseResponse baseResponse = response.body();

                    if (baseResponse.getStatus()) {

                        clearSignUpLocation();

                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();

                    } else {

                        clearSignUpLocation();

                        Toast.makeText(SignUpActivity.this, "Your Already Registerd", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }

            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

            }
        });


    }

    private void clearSignUpLocation() {

        PreferenceUtil.remove(SignUpActivity.this,PreferenceUtil.USER_LAT);
        PreferenceUtil.remove(SignUpActivity.this,PreferenceUtil.USER_LONG);
    }

    @Override
    protected void onResume() {
        super.onResume();


        String lat = PreferenceUtil.getValueString(SignUpActivity.this, PreferenceUtil.USER_LAT);
        String longi = PreferenceUtil.getValueString(SignUpActivity.this, PreferenceUtil.USER_LONG);

        //System.out.println("value" + lat + " " + longi);

        if (lat.equals("null") && longi.equals("null")) {


        } else {
            riderLat = Double.valueOf(lat);
            riderLongi = Double.valueOf(longi);


            List<Address> geoAddresses = GpsUtils.getAddressFromMap(SignUpActivity.this, riderLat, riderLongi);
            String riderDeliveryAddress = GpsUtils.getFullAddress(geoAddresses);
            riderLocationn.setText(riderDeliveryAddress);

        }


    }
}