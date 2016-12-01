package com.crowdo.p2pmobile.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cwdsg05 on 1/12/16.
 */

public class LoanListClient {

    private static final String LOG_TAG = LoanListClient.class.getSimpleName();

    public static final String BASE_URL = "https://krune2aiye.execute-api.ap-southeast-1.amazonaws.com/";
    public static final String STAGE = "dev/";

    private static LoanListClient instance;
    private APIServices apiServices;


    public LoanListClient(){
        final Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();

        final Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL + STAGE)
                .build();

        this.apiServices = retrofit.create(APIServices.class);
    }

    public static LoanListClient getInstance(){

        if(instance == null)
            instance = new LoanListClient();

        return instance;
    }

    public Observable<List<LoanItem>> getLiveLoans(){
        return apiServices.getLoansList();
    }

}
