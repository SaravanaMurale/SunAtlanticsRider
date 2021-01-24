package com.courier.sunatlanticsrider.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.courier.sunatlanticsrider.R;
import com.courier.sunatlanticsrider.lilly.SignUpActivity;
import com.courier.sunatlanticsrider.model.BaseResponse;
import com.courier.sunatlanticsrider.model.LoginAuthResponse;
import com.courier.sunatlanticsrider.model.LoginRequest;
import com.courier.sunatlanticsrider.model.LoginResponse;
import com.courier.sunatlanticsrider.retrofit.ApiClient;
import com.courier.sunatlanticsrider.retrofit.ApiInterface;
import com.courier.sunatlanticsrider.utils.LoaderUtil;
import com.courier.sunatlanticsrider.utils.MathUtil;
import com.courier.sunatlanticsrider.utils.PreferenceUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.courier.sunatlanticsrider.utils.MathUtil.validateMobile;
import static com.courier.sunatlanticsrider.utils.MathUtil.validatePassword;

public class LoginActivity extends AppCompatActivity {

    private EditText loginMobile, loginPassword;
    private TextInputLayout inputLoginMobile, inputLoginPassword;
    private Button btnSignIn;
    private TextView loginSignup;

    Typeface typeface;
    TextView othersignin;

    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();


                if(newToken!=null){
                    saveFirebaseNotificationTokenInServer();
                    PreferenceUtil.setValueString(LoginActivity.this, PreferenceUtil.NOTIFICATION, newToken);
                }else {
                    System.out.println("NOTOKENGENERATED");
                }


               
            }
        });

        typeface = MathUtil.getOctinPrisonFont(LoginActivity.this);


        loginMobile = (EditText) findViewById(R.id.login_mobile);
        loginPassword = (EditText) findViewById(R.id.login_password);
        loginSignup = (TextView) findViewById(R.id.loginSignup);

        loginSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        btnSignIn = (Button) findViewById(R.id.btn_login);

        loginMobile.addTextChangedListener(new MyTextWatcher(loginMobile));
        loginPassword.addTextChangedListener(new MyTextWatcher(loginPassword));

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginUser();

            }
        });

    }

    private void saveFirebaseNotificationTokenInServer() {

        dialog = LoaderUtil.showProgressBar(this);
        ApiInterface apiInterface = ApiClient.getAPIClient().create(ApiInterface.class);

        /*int userid=PreferenceUtil.getValueInt(LoginActivity.this,PreferenceUtil.USER_ID);
       String pToken=PreferenceUtil.getValueString(LoginActivity.this,PreferenceUtil.NOTIFICATION);*/

        LoginResponse loginResponse=new LoginResponse(PreferenceUtil.getValueInt(LoginActivity.this,PreferenceUtil.USER_ID),PreferenceUtil.getValueString(LoginActivity.this,PreferenceUtil.NOTIFICATION));

        Call<BaseResponse> call=apiInterface.saveNotificationTokenInServer(PreferenceUtil.getValueString(LoginActivity.this, PreferenceUtil.BEARER) + " " + PreferenceUtil.getValueString(LoginActivity.this, PreferenceUtil.AUTH_TOKEN),loginResponse);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                BaseResponse baseResponse=response.body();
                LoaderUtil.dismisProgressBar(LoginActivity.this, dialog);
                if(baseResponse!=null){
                    if(baseResponse.getSuccess()){
                        System.out.println("TokenInsertedSuccessfully");
                    }else {
                        System.out.println("TokenIsNotInsertedSuccessfully");
                    }
                }else {
                    return;
                }



            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                LoaderUtil.dismisProgressBar(LoginActivity.this, dialog);
            }
        });


    }

    private void loginUser() {
        dialog = LoaderUtil.showProgressBar(this);

        ApiInterface apiInterface = ApiClient.getAPIClient().create(ApiInterface.class);
        LoginRequest loginRequest = new LoginRequest(loginMobile.getText().toString(), loginPassword.getText().toString());
        Call<LoginAuthResponse> call = apiInterface.doCheckLogin(loginRequest);
        call.enqueue(new Callback<LoginAuthResponse>() {
            @Override
            public void onResponse(Call<LoginAuthResponse> call, Response<LoginAuthResponse> response) {
                LoginAuthResponse loginAuthResponse = response.body();

                if(loginAuthResponse.getStatus()){

                    if (loginAuthResponse.getAuthToken() != null && loginAuthResponse.getTokenType() != null) {

                        PreferenceUtil.setValueString(LoginActivity.this, PreferenceUtil.AUTH_TOKEN, loginAuthResponse.getAuthToken());
                        PreferenceUtil.setValueString(LoginActivity.this, PreferenceUtil.BEARER, loginAuthResponse.getTokenType());


                        LoaderUtil.dismisProgressBar(LoginActivity.this, dialog);

                        getUserDetails();

                    }
                }else {

                    LoaderUtil.dismisProgressBar(LoginActivity.this, dialog);
                    Toast.makeText(LoginActivity.this,"You have endtered wrong username or password",Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<LoginAuthResponse> call, Throwable t) {
                LoaderUtil.dismisProgressBar(LoginActivity.this, dialog);
            }
        });
    }

    private void getUserDetails() {
        dialog = LoaderUtil.showProgressBar(this);

        ApiInterface apiInterface = ApiClient.getAPIClient().create(ApiInterface.class);
        Call<LoginResponse> call = apiInterface.doGetUserDetails(PreferenceUtil.getValueString(LoginActivity.this, PreferenceUtil.BEARER) + " " + PreferenceUtil.getValueString(LoginActivity.this, PreferenceUtil.AUTH_TOKEN));

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();

                PreferenceUtil.setValueSInt(LoginActivity.this, PreferenceUtil.USER_ID, loginResponse.getUserId());

                LoaderUtil.dismisProgressBar(LoginActivity.this, dialog);

                launchHomeActivity();


            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                LoaderUtil.dismisProgressBar(LoginActivity.this, dialog);
            }
        });
    }

    private void launchHomeActivity() {
        Intent intent = new Intent(LoginActivity.this, DrawerActivity.class);
        startActivity(intent);
        finish();
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


            String mobile = loginMobile.getText().toString().trim();
            String password = loginPassword.getText().toString().trim();

            btnSignIn.setEnabled(validateMobile(mobile) && validatePassword(password));

            if (btnSignIn.isEnabled()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btnSignIn.setBackground(getDrawable(R.drawable.rectangle_shpae));
                    btnSignIn.setTextColor(getApplication().getResources().getColor(R.color.white));
                }
            } else if (!btnSignIn.isEnabled()) {
                btnSignIn.setEnabled(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btnSignIn.setBackground(getDrawable(R.color.btn_disable));
                    btnSignIn.setTextColor(getApplication().getResources().getColor(R.color.black));
                }
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}