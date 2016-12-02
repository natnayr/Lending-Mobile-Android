package com.crowdo.p2pmobile.data;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by cwdsg05 on 9/11/16.
 */

public interface APIServices {

    @GET("loans")
    Observable<List<LoanListItem>> getLoansList();
}
