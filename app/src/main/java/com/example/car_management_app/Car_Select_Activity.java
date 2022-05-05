package com.example.car_management_app;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class Car_Select_Activity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

    Button car1;
    Button car2;

    public static String kakaoID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_car_select);

        updateKakaoLoginUi();

        //isSelect();

        Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
            @Override
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                if(oAuthToken != null){
                    //TBD

                }
                if(throwable != null){
                    //TBD
                }
                updateKakaoLoginUi();
                return null;
            }
        };


        car1 = findViewById(R.id.Car1);
        car2 = findViewById(R.id.Car2);

        car1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CarSelect(true, "Gasoline");
            }
        });
        CekSession();
    }

    private void isSelect() {

        databaseReference.child("Car_Management").child(kakaoID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                Log.d("qadq", value); // 에러문 출력

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Log.e("MainActivity", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });
    }

    private void updateKakaoLoginUi() {
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                if(user != null){

                    Log.i(TAG, "invoke: id=" + user.getKakaoAccount().getProfile().getNickname());
                    Log.i(TAG, "invoke: email=" + user.getKakaoAccount().getEmail());
                    Log.i(TAG, "invoke: nickname=" + user.getKakaoAccount().getProfile().getNickname());
                    Log.i(TAG, "invoke: gender=" + user.getKakaoAccount().getGender());
                    Log.i(TAG, "invoke: age=" + user.getKakaoAccount().getAgeRange());

                    kakaoID = user.getKakaoAccount().getProfile().getNickname();

                    if(databaseReference.child("Car_Management").child(kakaoID).child("1").getKey() != null) {
                        SharedPrefs2.saveSharedSetting(Car_Select_Activity.this, "CaptainCode", "false");
                    }
                }
                else{

                }
                if (throwable != null) {
                    Log.w(TAG, "invoke: " + throwable.getLocalizedMessage());
                }
                return null;
            }
        });
    }

    private void CekSession() {
        Boolean Check = Boolean.valueOf(SharedPrefs.readSharedSetting(Car_Select_Activity.this, "CaptainCode", "true"));

        Intent introIntent = new Intent(Car_Select_Activity.this, LoginActivity.class);
        introIntent.putExtra("CaptainCode", Check);

        //The Value if you click on Login Activity and Set the value is FALSE and whe false the activity will be visible
        if (Check) {
            Log.d("Check2", String.valueOf(Check));
            startActivity(introIntent);
            finish();
        } //If no the Main Activity not Do Anything
    }

    public void CarSelect(boolean isSelect, String Oil){
        Car_Select_Helper car_select = new Car_Select_Helper(isSelect, Oil);

        databaseReference.child("Car_Management").child(kakaoID).child("1").child("Oil").setValue(Oil);
        Intent intent2 = new Intent(Car_Select_Activity.this, Car_Number_Reigst.class);
        intent2.putExtra("Name",kakaoID);
        startActivity(intent2);
    }
}