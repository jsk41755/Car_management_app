package com.example.car_management_app;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

//REST API의 메소드 GET POST DELETE PUT 중에 GET 메소드 사용하기
public interface RetrofitService {
    @GET("/v2/local/search/keyword.json")
    Call<GetResultClass> getDocuments(@Header("Authorization") String KakaoAK,
                                      @Query("query") String query);
}
