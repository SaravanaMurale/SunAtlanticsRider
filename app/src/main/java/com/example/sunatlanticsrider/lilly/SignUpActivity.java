package com.example.sunatlanticsrider.lilly;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sunatlanticsrider.R;
import com.example.sunatlanticsrider.activity.RiderLocationFetchActivity;
import com.example.sunatlanticsrider.model.RegisterRiderRequest;
import com.example.sunatlanticsrider.retrofit.ApiClient;
import com.example.sunatlanticsrider.retrofit.ApiInterface;
import com.example.sunatlanticsrider.utils.GpsUtils;
import com.example.sunatlanticsrider.utils.LoaderUtil;

import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    RelativeLayout layout1;
    RelativeLayout layout2;
    Button textView1;
    Button textView2,btnRiderSignUp;

    TextView loginText, signUpText, riderLocationn;

    RelativeLayout riderLocationBlock;

    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        layout1 = findViewById(R.id.rel_Layout1);
        layout2 = findViewById(R.id.rel_Layout2);
        textView1 = findViewById(R.id.login);
        textView2 = findViewById(R.id.signUp);

        riderLocationBlock = (RelativeLayout) findViewById(R.id.riderLocationBlock);
        riderLocationn = (TextView) findViewById(R.id.riderLocationn);
        btnRiderSignUp=(Button)findViewById(R.id.btnRiderSignUp);

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

        loginText = (TextView) findViewById(R.id.loginText);
        signUpText = (TextView) findViewById(R.id.signUpText);

        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SignUpActivity.this, "View1", Toast.LENGTH_SHORT).show();
                layout2.setVisibility(View.GONE);
                layout1.setVisibility(View.VISIBLE);
            }
        });

        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout2.setVisibility(View.VISIBLE);
                layout1.setVisibility(View.GONE);
                Toast.makeText(SignUpActivity.this, "View2", Toast.LENGTH_SHORT).show();

            }
        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SignUpActivity.this, "View1", Toast.LENGTH_SHORT).show();
                layout2.setVisibility(View.GONE);
                layout1.setVisibility(View.VISIBLE);
            }
        });

        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout2.setVisibility(View.VISIBLE);
                layout1.setVisibility(View.GONE);
                Toast.makeText(SignUpActivity.this, "View2", Toast.LENGTH_SHORT).show();

            }
        });


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