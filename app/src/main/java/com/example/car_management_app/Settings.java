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

        String id = "my_channel_01";    //외부 알림설정 ID값

        LinearLayout homeBack = (LinearLayout) findViewById(R.id.homeBack);
        switchAlarm = findViewById(R.id.switchAlarm);
        logoutButton = findViewById(R.id.logoutButton);

        String kakaoID = getIntent().getStringExtra("KakaoID");

        Calendar cal = Calendar.getInstance();      //현재 시간 출력을 위한 라이브러리
        cal.setTime(new Date());
        DateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        Calendar cal2 = Calendar.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {       //Android Oreo버젼에서는 이전과 다르게 Notification 기능을 다르게 해주어야 한다.
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(id, "My Channel", importance);
            channel.setDescription("wafafa");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(Settings.this, id)      //Notification 알림 객체 생성
                .setSmallIcon(R.drawable.carimg)
                .setContentTitle("카계부 알림")
                .setContentText("엔진오일 교체 1주일 전 입니다");

        notification = builder.build();
        notificationManagerCompat = NotificationManagerCompat.from(Settings.this);  //Notification 빌드.

        homeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {        //로그아웃 기능(LoginActivity에서 Setting 액티비티로 기능 옮김.)
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
    }   //알림 Swith를 On 했을때, 알림 기능 Notification을 실행 해줌.

}
