package com.courier.sunatlanticsrider.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.courier.sunatlanticsrider.R;
import com.courier.sunatlanticsrider.utils.PreferenceUtil;

public class SplashScreen extends AppCompatActivity {

    int user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        user_id = PreferenceUtil.getValueInt(this, PreferenceUtil.USER_ID);

        new SplashDownCountDown(3000, 1000).start();

    }


    private class SplashDownCountDown extends CountDownTimer {

        SplashDownCountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);


        }

        @Override
        public void onTick(long milliSecond) {

        }

        @Override
        public void onFinish() {

            Intent intent;

            if (user_id==-1) {
                intent = new Intent(SplashScreen.this, LoginActivity.class);

            } else {
                intent = new Intent(SplashScreen.this, DrawerActivity.class);

            }

            startActivity(intent);
            finish();

        }
    }

}