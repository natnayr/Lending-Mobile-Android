package com.crowdo.p2pconnect.data;

import com.crowdo.p2pconnect.data.request_model.LoginRequest;
import com.crowdo.p2pconnect.data.request_model.RegisterRequest;
import com.crowdo.p2pconnect.data.response_model.OAuthResponse;
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

/**
 * Created by cwdsg05 on 9/11/16.
 */

public interface APIServices {

    public static final String API_OLD_BASE_URL = "https://p2pid.crowdo.com/";
    public static final String OLD_STAGE = "internal_api/";

    public static final String API_LIVE_BASE_URL = "https://api.crowdo.com/";
    public static final String LIVE_STAGE = "api/v1/";

    public static final String FACTSHEET_URL = "download_factsheet/";
    public static final String FACTSHEET_LANGUAGE_PARAM = "lang=";

    @GET("loan_listing")
    Observable<List<LoanListItem>> getLoansList();

    @GET("loan_details/{id}")
    Observable<LoanDetail> getLoanDetail(@Path("id") int id);

    @POST("oauth/login")
    @Headers({"Content-type: application/json"})
    Observable<Response<OAuthResponse>> postLoginUser(@Body LoginRequest data);

    @POST("oauth/register")
    @Headers({"Content-type: application/json"})
    Observable<Response<OAuthResponse>> postRegisterUser(@Body RegisterRequest data);
}
