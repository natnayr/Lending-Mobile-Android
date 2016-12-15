package com.crowdo.p2pmobile.data;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by cwdsg05 on 6/12/16.
 */

public class LoanDetailClient {

    private static final String LOG_TAG = LoanListClient.class.getSimpleName();

    private static LoanDetailClient instance;
    private APIServices apiServices;

    public LoanDetailClient(){
        final Gson gson = new GsonBuilder()
                .create();

        final Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(APIServices.LAMBDA_BASE_URL + APIServices.STAGE)
                .build();

        this.apiServices = retrofit.create(APIServices.class);
    }

    public static LoanDetailClient getInstance(){

        if(instance == null)
            instance = new LoanDetailClient();

        return instance;
    }

    public Observable<LoanDetail> getLoanDetails(int id){
        Log.d(LOG_TAG, "TEST: parsing to apiServices.getLoanDetail: " + id);
        return apiServices.getLoanDetail(id);
    }

}
