package com.courier.sunatlanticsrider.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.courier.sunatlanticsrider.R;
import com.courier.sunatlanticsrider.lilly.SignUpActivity;
import com.courier.sunatlanticsrider.utils.PermissionUtils;
import com.courier.sunatlanticsrider.utils.PreferenceUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import static com.courier.sunatlanticsrider.utils.AppConstant.LOCATION_PERMISSION_REQUEST_CODE;

public class SplashScreen extends AppCompatActivity {

    int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        user_id = PreferenceUtil.getValueInt(this, PreferenceUtil.USER_ID);

        PreferenceUtil.remove(SplashScreen.this,PreferenceUtil.USER_LAT);
        PreferenceUtil.remove(SplashScreen.this,PreferenceUtil.USER_LONG);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {

                String newToken = instanceIdResult.getToken();

                if(newToken!=null){
                    //saveFirebaseNotificationTokenInServer();
                    PreferenceUtil.setValueString(SplashScreen.this, PreferenceUtil.NOTIFICATION, newToken);
                    String token=PreferenceUtil.getValueString(SplashScreen.this,PreferenceUtil.NOTIFICATION);
                    System.out.println("TOKENGEN"+token);
                }else {
                    System.out.println("NOTOKENGENERATED");
                }

            }
        });


        new SplashDownCountDown(3000, 1000).start();

    }


    class SplashDownCountDown extends CountDownTimer {

        SplashDownCountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);


        }

        @Override
        public void onTick(long milliSecond) {

        }

        @Override
        public void onFinish() {

            Intent intent;

            if (user_id == -1) {
                intent = new Intent(SplashScreen.this, LoginActivity.class);

            } else {
                intent = new Intent(SplashScreen.this, DrawerActivity.class);

            }

            startActivity(intent);
            finish();

        }
    }

}