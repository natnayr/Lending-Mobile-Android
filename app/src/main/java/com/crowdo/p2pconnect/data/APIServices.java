package com.crowdo.p2pconnect.data;

import com.crowdo.p2pconnect.data.request_model.LoginRequest;
import com.crowdo.p2pconnect.data.request_model.RegisterRequest;
import com.crowdo.p2pconnect.data.response_model.AuthResponse;
import com.crowdo.p2pconnect.model.LoanDetail;
import com.crowdo.p2pconnect.model.LoanListItem;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by cwdsg05 on 9/11/16.
 */

public interface APIServices {

    public static final String FACTSHEET_STAGE = "download_factsheet/";
    public static final String FACTSHEET_LANGUAGE_PARAM = "lang=";

    public static final String P2P_BASE_URL = "https://crowdo.co.id/";

    public static final String API_LIVE_BASE_URL = "https://api.crowdo.com/";
    public static final String LIVE_STAGE = "api/v1/";


    @GET("loans/loan_listing")
    @Headers({"Content-type: application/json"})
    Observable<Response<List<LoanListItem>>> getLoansList(@Query("device_id") String deviceId);

    @GET("loans/loan_details/{id}")
    @Headers({"Content-type: application/json"})
    Observable<Response<LoanDetail>> getLoanDetail(@Path("id") int id, @Query("device_id") String deviceId);

    @POST("oauth/login")
    @Headers({"Content-type: application/json"})
    Observable<Response<AuthResponse>> postLoginUser(@Body LoginRequest data);

    @POST("oauth/register")
    @Headers({"Content-type: application/json"})
    Observable<Response<AuthResponse>> postRegisterUser(@Body RegisterRequest data);
}
