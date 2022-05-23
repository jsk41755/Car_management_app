package com.example.car_management_app;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
