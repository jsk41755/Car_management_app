package com.example.car_management_app;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// 카카오맵 REST API를 이용해 사이트에서 가져온 값 JAVA로 사용가능하게 변환시켜 주기
public class RetrofitNet {
    private static RetrofitNet INSTANCE = new RetrofitNet();

    public static RetrofitNet getRetrofit() {
        return INSTANCE;
    }

    private RetrofitNet() {
    }

    Retrofit kakaoRetrofit = new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://dapi.kakao.com/")
            .build();

    RetrofitService service = kakaoRetrofit.create(RetrofitService.class);

    public RetrofitService getService(){
        return service;
    }
}
