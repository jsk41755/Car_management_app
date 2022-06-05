package com.example.car_management_app;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.common.util.Utility;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

public class Settings extends AppCompatActivity {

    private LinearLayout homeBack;
    Switch switchAlarm;
    LinearLayout logoutButton;

    HomeActivity homeActivity = (HomeActivity)HomeActivity.homeActivity;
    Car_Select_Activity car_select_activity = (Car_Select_Activity)Car_Select_Activity.carselectActivity;
    Car_Number_Reigst car_number_reigst = (Car_Number_Reigst)Car_Number_Reigst.carreigstActivity;
    LoginActivity loginActivity = (LoginActivity)LoginActivity.loginActivity;

    NotificationManagerCompat notificationManagerCompat;
    Notification notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        String id = "my_channel_01";

        LinearLayout homeBack = (LinearLayout) findViewById(R.id.homeBack);
        switchAlarm = findViewById(R.id.switchAlarm);
        logoutButton = findViewById(R.id.logoutButton);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("myCh", "My Channel", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "my Ch")
                .setSmallIcon(android.R.drawable.stat_notify_sync)
                .setContentTitle("First Notification")
                .setContentText("This is the Body of message")
                .setOngoing(true);

        notification = builder.build();
        notificationManagerCompat = NotificationManagerCompat.from(this);

        homeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.this, HomeActivity.class);
                intent.putExtra("로그아웃","로그아웃완료");
                homeActivity.finish();
                SharedPrefs.saveSharedSetting(Settings.this, "CaptainCode", "true");
                SharedPrefs2.saveSharedSetting(Settings.this, "CaptainCode", "true");
                startActivity(intent);
                finish();
            }
        });
    }

    public void push(View view){
        notificationManagerCompat.notify(0, notification);
    }

}
