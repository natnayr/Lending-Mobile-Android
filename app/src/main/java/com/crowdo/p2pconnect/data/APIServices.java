package com.crowdo.p2pconnect.data;

import android.os.Build;

import com.crowdo.p2pconnect.data.client.LoginClient;
import com.crowdo.p2pconnect.data.client.RegisterClient;
import com.crowdo.p2pconnect.data.response.AuthResponse;
import com.crowdo.p2pconnect.model.LoanDetail;
import com.crowdo.p2pconnect.model.LoanListItem;
import com.crowdo.p2pconnect.model.Member;
import com.crowdo.p2pconnect.model.RegisteredMemberCheck;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import rx.Observable;

/**
 * Created by cwdsg05 on 9/11/16.
 */

public interface APIServices {

    public static final String API_BASE_URL = "https://p2pid.crowdo.com/";
    public static final String STAGE = "internal_api/";

    public static final String API_TEST_URL = "https://crowdo-api.herokuapp.com/";
    public static final String TEST_STAGE = "api/v1/";


    @GET("loan_listing")
    Observable<List<LoanListItem>> getLoansList();

    @GET("loan_details/{id}")
    Observable<LoanDetail> getLoanDetail(@Path("id") int id);

    @Streaming
    @GET("download_factsheet/{id}")
    Observable<Response<ResponseBody>> getLoanFactSheet(@Path("id") int id,
                                                        @Query("lang") String lang);
    @GET("member_by_email?")
    Observable<RegisteredMemberCheck> getUserCheck(@Query("email") String email);

    @Headers({"Content-type: application/json"})
    @POST("login")
    Observable<Response<AuthResponse>> postLoginUser(@Body LoginClient.Input data);

    @Headers({"Content-type: application/json"})
    @POST("register")
    Observable<AuthResponse> postRegisterUser(@Body RegisterClient.Input data);

}
