package com.crowdo.p2pmobile.data;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by cwdsg05 on 9/11/16.
 */

public interface APIServices {

//    public static final String API_BASE_URL = "https://krune2aiye.execute-api.ap-southeast-1.amazonaws.com/";

    public static final String API_BASE_URL = "http://p2pid.crowdo.com/";
    public static final String STAGE = "internal_api/";

    @GET("loan_listing")
    Observable<List<LoanListItem>> getLoansList();

    @GET("loan_details/{id}")
    Observable<LoanDetail> getLoanDetail(@Path("id") int id);

    @Streaming
    @GET("download_factsheet/{id}")
    Observable<Response<ResponseBody>> getLoanFactSheet(@Path("id") int id);

    @GET("member_by_email?")
    Observable<RegisteredMemberCheck> getUserCheck(@Query("email") String email);

    @Streaming
    @GET
    Observable<Response<ResponseBody>> getWebViewDownloadFile(@Url String fileUrl);
}
