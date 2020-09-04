package com.example.sunatlanticsrider.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sunatlanticsrider.R;
import com.example.sunatlanticsrider.model.LoginAuthResponse;
import com.example.sunatlanticsrider.model.LoginRequest;
import com.example.sunatlanticsrider.model.LoginResponse;
import com.example.sunatlanticsrider.retrofit.ApiClient;
import com.example.sunatlanticsrider.retrofit.ApiInterface;
import com.example.sunatlanticsrider.utils.LoaderUtil;
import com.example.sunatlanticsrider.utils.MathUtil;
import com.example.sunatlanticsrider.utils.PreferenceUtil;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sunatlanticsrider.utils.MathUtil.validateMobile;
import static com.example.sunatlanticsrider.utils.MathUtil.validatePassword;

public class LoginActivity extends AppCompatActivity {

    private EditText loginMobile, loginPassword;
    private TextInputLayout inputLoginMobile, inputLoginPassword;
    private Button btnSignIn;

    Typeface typeface;
    TextView othersignin;

    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        typeface = MathUtil.getOctinPrisonFont(LoginActivity.this);


        loginMobile = (EditText) findViewById(R.id.login_mobile);
        loginPassword = (EditText) findViewById(R.id.login_password);

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

    private void loginUser() {
        dialog = LoaderUtil.showProgressBar(this);

        ApiInterface apiInterface = ApiClient.getAPIClient().create(ApiInterface.class);
        LoginRequest loginRequest = new LoginRequest(loginMobile.getText().toString(), loginPassword.getText().toString());
        Call<LoginAuthResponse> call = apiInterface.doCheckLogin(loginRequest);
        call.enqueue(new Callback<LoginAuthResponse>() {
            @Override
            public void onResponse(Call<LoginAuthResponse> call, Response<LoginAuthResponse> response) {
                LoginAuthResponse loginAuthResponse = response.body();
                if (loginAuthResponse.getAuthToken() != null && loginAuthResponse.getTokenType() != null) {

                    PreferenceUtil.setValueString(LoginActivity.this, PreferenceUtil.AUTH_TOKEN, loginAuthResponse.getAuthToken());
                    PreferenceUtil.setValueString(LoginActivity.this, PreferenceUtil.BEARER, loginAuthResponse.getTokenType());


                    LoaderUtil.dismisProgressBar(LoginActivity.this, dialog);

                    getUserDetails();

                }
            }

            @Override
            public void onFailure(Call<LoginAuthResponse> call, Throwable t) {

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
                    btnSignIn.setTextColor(R.color.black);
                }
            }

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}