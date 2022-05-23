package com.example.car_management_app;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface RetrofitService {
    @GET("/v2/local/search/keyword.json")
    Call<GetResultClass> getDocuments(@Header("Authorization") String KakaoAK,
                                      @Query("query") String query);
}
