package com.courier.sunatlanticsrider.utils;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.courier.sunatlanticsrider.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseInstanceService extends FirebaseMessagingService {

    public MyFirebaseInstanceService() {
//        Toast.makeText(MyFirebaseInstanceService.this, "Push Notification Called", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        Toast.makeText(MyFirebaseInstanceService.this, "Token Generated"+s, Toast.LENGTH_LONG).show();
        if (s == null) {
            System.out.println("FCMToken" + s);
        } else {
            System.out.println("FCMToken" + s);
        }

    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("message");
        String clickAction = remoteMessage.getData().get("click_action");

        Toast.makeText(MyFirebaseInstanceService.this,message+" "+title, Toast.LENGTH_LONG).show();

        pushNotificationBuilder(title, message, clickAction);

    }

    private void pushNotificationBuilder(String title, String message, String clickAction) {

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "com.sunatlantics.notification";

        // Here pass your activity where you want to redirect.
        Intent intent = new Intent(clickAction);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this);
        nBuilder.setContentTitle(title)
                .setContentText(message)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setPriority(1)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_logout);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.setDescription("FG Channel");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            nBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            notificationManager.createNotificationChannel(notificationChannel);

        }


        notificationManager.notify(new Random().nextInt(), nBuilder.build());


    }




}
