package com.crowdo.p2pmobile.data;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by cwdsg05 on 9/11/16.
 */

public interface APIServicesInterface {

    public static final String BASE_URL = "https://krune2aiye.execute-api.ap-southeast-1.amazonaws.com/";
    public static final String STAGE = "dev/";

    @GET("loans")
    Call<List<LoanItem>> getLoansList();

}
