package com.crowdo.p2pconnect.data;

import com.crowdo.p2pconnect.data.request_model.LoginRequest;
import com.crowdo.p2pconnect.data.request_model.RegisterRequest;
import com.crowdo.p2pconnect.data.response_model.OAuthResponse;
import com.crowdo.p2pconnect.model.LoanDetail;
import com.crowdo.p2pconnect.model.LoanListItem;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by cwdsg05 on 9/11/16.
 */

public interface APIServices {

    public static final String API_OLD_BASE_URL_DOWN = "https://p2pid.crowdo.com/";
    public static final String OLD_STAGE_DOWN = "internal_api/";

    public static final String API_LIVE_BASE_URL = "https://api.crowdo.com/";
    public static final String LIVE_STAGE = "api/v1/";

    public static final String FACTSHEET_URL = "download_factsheet/";
    public static final String FACTSHEET_LANGUAGE_PARAM = "lang=";

    public static final String API_OLD_BASE_URL = "http://192.168.1.3:3000/";
    public static final String OLD_STAGE = "internal_api/";

    public static final String PRASANTH_API_URL = "http://192.168.1.3:3004/";
    public static final String PRASANTH_API_STAGE = "api/v1/";


    @GET("loans/loan_listing")
    Observable<Response<List<LoanListItem>>> getLoansList(@Query("device_id") String deviceId);

    @GET("loans/loan_details/{id}")
    Observable<Response<LoanDetail>> getLoanDetail(@Path("id") int id, @Query("device_id") String deviceId);

    @POST("oauth/login")
    @Headers({"Content-type: application/json"})
    Observable<Response<OAuthResponse>> postLoginUser(@Body LoginRequest data);

    @POST("oauth/register")
    @Headers({"Content-type: application/json"})
    Observable<Response<OAuthResponse>> postRegisterUser(@Body RegisterRequest data);
}
