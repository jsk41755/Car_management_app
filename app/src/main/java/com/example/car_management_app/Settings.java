package com.example.car_management_app;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.common.util.Utility;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

public class Settings extends AppCompatActivity {

    private LinearLayout homeBack;
    Switch switchAlarm;
    LinearLayout logoutButton;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();

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

        String kakaoID = getIntent().getStringExtra("KakaoID");

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        DateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        Calendar cal2 = Calendar.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(id, "My Channel", importance);
            channel.setDescription("wafafa");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);


            /*NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            NotificationChannel channel = new NotificationChannel("myCh", "My Channel", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);*/
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(Settings.this, id)
                .setSmallIcon(R.drawable.carimg)
                .setContentTitle("카계부 알림")
                .setContentText("엔진오일 교체 1주일 전 입니다");/*
                .setOngoing(true);*/

        notification = builder.build();
        notificationManagerCompat = NotificationManagerCompat.from(Settings.this);

        /*switchAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("Car_Management").child(kakaoID).child("1").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.child("Supplies").getChildren()) {
                            for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                                for (DataSnapshot dataSnapshot3 : dataSnapshot2.getChildren()) {
                                    double day = 0;
                                    if (dataSnapshot3.getKey().equals("EngineOil")) {
                                        cal2.set(2022, Integer.parseInt(dataSnapshot.getKey()) - 1, Integer.parseInt(dataSnapshot2.getKey()));
                                        day = (cal.getTimeInMillis() - cal2.getTimeInMillis()) / (24 * 60 * 60 * 1000);
                                        if ((int) (365 - day) == 365) {
                                            Log.d("regre","gerge");

                                        }
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });*/

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
                Boolean logoutstatus = true;
                //homeActivity.finish();
                SharedPrefs.saveSharedSetting(Settings.this, "CaptainCode", "true");
                SharedPrefs2.saveSharedSetting(Settings.this, "CaptainCode", "true");
                intent.putExtra("로그아웃",logoutstatus);
                startActivity(intent);
                finish();
            }
        });
    }

    public void push(View view){
        notificationManagerCompat.notify(1, notification);
    }

}
