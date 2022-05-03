package com.example.car_management_app;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

    String kakaoID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_select);

        updateKakaoLoginUi();

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

    private void updateKakaoLoginUi() {
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                if(user != null){

                    Log.i(TAG, "invoke: id3=" + user.getKakaoAccount().getProfile().getNickname());
                    Log.i(TAG, "invoke: email=" + user.getKakaoAccount().getEmail());
                    Log.i(TAG, "invoke: nickname=" + user.getKakaoAccount().getProfile().getNickname());
                    Log.i(TAG, "invoke: gender=" + user.getKakaoAccount().getGender());
                    Log.i(TAG, "invoke: age=" + user.getKakaoAccount().getAgeRange());

                    kakaoID = user.getKakaoAccount().getProfile().getNickname();
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

        Intent introIntent = new Intent(Car_Select_Activity.this, MainActivity.class);
        introIntent.putExtra("CaptainCode", Check);

        //The Value if you click on Login Activity and Set the value is FALSE and whe false the activity will be visible
        if (Check) {
            startActivity(introIntent);
            finish();
        } //If no the Main Activity not Do Anything
    }

    public void CarSelect(boolean isSelect, String Oil){
        Car_Select_Helper car_select = new Car_Select_Helper(isSelect, Oil);

        databaseReference.child("Car_Management").child(kakaoID).setValue(Oil);
        Intent intent2 = new Intent(Car_Select_Activity.this, HomeActivity.class);
        intent2.putExtra("Name",kakaoID);
        startActivity(intent2);
    }
}