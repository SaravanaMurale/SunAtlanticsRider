package com.courier.sunatlanticsrider.lilly;

import androidx.appcompat.app.AppCompatActivity;

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
import com.courier.sunatlanticsrider.retrofit.ApiClient;
import com.courier.sunatlanticsrider.retrofit.ApiInterface;
import com.courier.sunatlanticsrider.utils.GpsUtils;
import com.courier.sunatlanticsrider.utils.LoaderUtil;

import java.util.List;

import static com.courier.sunatlanticsrider.utils.MathUtil.validateMobile;
import static com.courier.sunatlanticsrider.utils.MathUtil.validatePassword;

public class SignUpActivity extends AppCompatActivity {


    Button textView1;
    Button btnRiderSignUp;

    TextView riderLocationn;

    RelativeLayout riderLocationBlock;

    Dialog dialog;

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

        signupName.addTextChangedListener(new MyTextWatcher(signupName));
        signupMobile.addTextChangedListener(new MyTextWatcher(signupMobile));
        signupPassword.addTextChangedListener(new MyTextWatcher(signupPassword));

        btnRiderSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerRider();
            }
        });

        riderLocationBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignUpActivity.this, RiderLocationFetchActivity.class);
                startActivity(intent);

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


            btnRiderSignUp.setEnabled(validateMobile(name) && validateMobile(mobile) && validatePassword(password));

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

        // RegisterRiderRequest registerRiderRequest=new RegisterRiderRequest()

    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        Double riderLat = intent.getDoubleExtra("RIDER_LAT", 0);
        Double riderLongi = intent.getDoubleExtra("RIDER_LONG", 0);

        if (riderLat != null && riderLat != 0 && riderLongi != null && riderLongi != 0) {
            List<Address> geoAddresses = GpsUtils.getAddressFromMap(SignUpActivity.this, riderLat, riderLongi);
            String riderDeliveryAddress = GpsUtils.getFullAddress(geoAddresses);
            riderLocationn.setText(riderDeliveryAddress);


        }


    }
}