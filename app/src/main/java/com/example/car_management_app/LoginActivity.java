package com.example.car_management_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.common.util.Utility;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private View loginButton, logoutButton;
    private TextView nickName;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public static Activity loginActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginActivity = LoginActivity.this;

        Intent logoutintent = getIntent();
        Boolean logoutstatus = logoutintent.getBooleanExtra("로그아웃", false);
        if(logoutstatus != null)
        {
            Log.d("로그아웃6",logoutstatus.toString());
            if(logoutstatus){
                UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
                    @Override
                    public Unit invoke(Throwable throwable) {
                        nickName.setText(null);
                        loginButton.setVisibility(View.VISIBLE);
                        logoutButton.setVisibility(View.GONE);
                        return null;
                    }
                });
            }
        }
        else{
            updateKakaoLoginUi();
        }

        Log.d("카카오", "시간체크");

        loginButton = findViewById(R.id.login);
        logoutButton = findViewById(R.id.logout);
        nickName = findViewById(R.id.nickname);


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

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(LoginActivity.this)){
                    UserApiClient.getInstance().loginWithKakaoTalk(LoginActivity.this, callback);
                }
                else{
                    UserApiClient.getInstance().loginWithKakaoAccount(LoginActivity.this, callback);
                    String keyHash = Utility.INSTANCE.getKeyHash(LoginActivity.this);
                    Log.i(TAG, "onCreate: keyHash:" + keyHash);
                }
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
                    @Override
                    public Unit invoke(Throwable throwable) {
                        updateKakaoLoginUi();
                        return null;
                    }
                });
            }
        });


    }

    private void updateKakaoLoginUi() {
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                if(user != null){
                    SharedPrefs.saveSharedSetting(LoginActivity.this, "CaptainCode", "false");
                    /*
                    Log.i(TAG, "invoke: id=" + user.getId());
                    Log.i(TAG, "invoke: email=" + user.getKakaoAccount().getEmail());
                    Log.i(TAG, "invoke: nickname=" + user.getKakaoAccount().getProfile().getNickname());
                    Log.i(TAG, "invoke: gender=" + user.getKakaoAccount().getGender());
                    Log.i(TAG, "invoke: age=" + user.getKakaoAccount().getAgeRange());
                    */
                    nickName.setText(user.getKakaoAccount().getProfile().getNickname());


                    logoutButton.setVisibility(View.VISIBLE);

                    Intent intent = new Intent(LoginActivity.this, Car_Select_Activity.class);
                    String sName = nickName.getText().toString().trim();
                    Log.d("rname", sName);
                    pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
                    editor = pref.edit();
                    editor.putString("KakaoName", sName);
                    editor.apply();
                    intent.putExtra("Name",sName);

                    startActivity(intent);
                    finish();

                }
                else{
                    nickName.setText(null);
                    loginButton.setVisibility(View.VISIBLE);
                    logoutButton.setVisibility(View.GONE);

                }
                if (throwable != null) {
                    Log.w(TAG, "invoke: " + throwable.getLocalizedMessage());
                }
                return null;
            }
        });
    }
}