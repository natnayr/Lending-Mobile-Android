package com.crowdo.p2pconnect.data;

import com.crowdo.p2pconnect.data.request_model.LoginRequest;
import com.crowdo.p2pconnect.data.request_model.RegisterRequest;
import com.crowdo.p2pconnect.data.response_model.AuthResponse;
import com.crowdo.p2pconnect.model.LoanDetail;
import com.crowdo.p2pconnect.model.LoanListItem;
import com.crowdo.p2pconnect.model.RegisteredMemberCheck;

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

    public static final String API_BASE_URL = "https://p2pid.crowdo.com/";
    public static final String STAGE = "internal_api/";

    public static final String API_TEST_URL = "https://crowdo-api.herokuapp.com/";
    public static final String TEST_STAGE = "api/v1/";

    public static final String FACTSHEET_URL = "download_factsheet/";
    public static final String FACTSHEET_LANGUAGE_PARAM = "lang=";

    @GET("loan_listing")
    Observable<List<LoanListItem>> getLoansList();

    @GET("loan_details/{id}")
    Observable<LoanDetail> getLoanDetail(@Path("id") int id);

    @GET("member_by_email?")
    Observable<RegisteredMemberCheck> getUserCheck(@Query("email") String email);

    @Headers({"Content-type: application/json"})
    @POST("login")
    Observable<Response<AuthResponse>> postLoginUser(@Body LoginRequest data);

    @Headers({"Content-type: application/json"})
    @POST("register")
    Observable<Response<AuthResponse>> postRegisterUser(@Body RegisterRequest data);
}
