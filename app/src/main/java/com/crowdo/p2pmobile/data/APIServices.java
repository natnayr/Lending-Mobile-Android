package com.crowdo.p2pmobile.data;

import java.io.File;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import rx.Observable;

/**
 * Created by cwdsg05 on 9/11/16.
 */

public interface APIServices {

    public static final String LAMBDA_BASE_URL = "https://krune2aiye.execute-api.ap-southeast-1.amazonaws.com/";
    public static final String STAGE = "dev/";

    public static final String P2P_BASE_URL = "https://p2p.crowdo.com/";

    @GET("loans")
    Observable<List<LoanListItem>> getLoansList();

    @GET("loans/{id}")
    Observable<LoanDetail> getLoanDetail(@Path("id") int id);

    @Streaming
    @GET("download_factsheet/{id}")
    Observable<Response<ResponseBody>> getLoanFactSheet(@Path("id") int id);

    @POST("members")
    Observable<RegisteredMemberCheck> postUserCheck(@Body RegisteredMemberCheckInput inputBody);

}
