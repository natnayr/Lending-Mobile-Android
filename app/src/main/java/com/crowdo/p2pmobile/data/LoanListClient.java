package com.crowdo.p2pmobile.data;

import com.crowdo.p2pmobile.model.LoanListItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by cwdsg05 on 1/12/16.
 */

public class LoanListClient {

    private static final String LOG_TAG = LoanListClient.class.getSimpleName();

    private static LoanListClient instance;
    private APIServices apiServices;

    public LoanListClient(){
        final Gson gson = new GsonBuilder()
                .create();

        final Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(APIServices.API_BASE_URL + APIServices.STAGE)
                .build();

        this.apiServices = retrofit.create(APIServices.class);
    }

    public static LoanListClient getInstance(){
        if(instance == null)
            instance = new LoanListClient();

        return instance;
    }

    public Observable<List<LoanListItem>> getLiveLoans(){
        return apiServices.getLoansList();
    }

}
