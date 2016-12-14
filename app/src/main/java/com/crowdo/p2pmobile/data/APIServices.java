package com.crowdo.p2pmobile.data;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by cwdsg05 on 9/11/16.
 */

public interface APIServices {

    public static final String BASE_URL = "https://krune2aiye.execute-api.ap-southeast-1.amazonaws.com/";
    public static final String STAGE = "dev/";

    @GET("loans")
    Observable<List<LoanListItem>> getLoansList();

    @GET("loans/{id}")
    Observable<LoanDetail> getLoanDetail(@Path("id") int id);
}
