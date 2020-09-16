package com.exno.mygetwayrestaurant.rest;


import com.exno.mygetwayrestaurant.model.Testingdata;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface ApiInterface {
    // @GET("movie/top_rated")


    /*@GET("movie/{id}")
    Call<LoginModel> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);*/


    /*@POST("api/getPaymentDetails")
    @FormUrlEncoded
    Call<Testingdata> savePost(@Query("api_key") String api_key,
                               @Query("booking_id") String booking_id);*/


    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("restaurant_login")
    Call<Testingdata> savePost(@Body RequestBody locationPost);



}
