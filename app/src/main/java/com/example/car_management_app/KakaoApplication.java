package com.example.car_management_app;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class KakaoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        KakaoSdk.init(this, "88af6b217bb2b94784fd9106a2587250");
        //정승규 기준 kakao appKey
    }
}
