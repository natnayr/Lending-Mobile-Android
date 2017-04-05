package com.crowdo.p2pconnect.data.client;

import android.util.Log;

import com.crowdo.p2pconnect.data.APIServices;
import com.crowdo.p2pconnect.model.LoanDetail;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

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
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(APIServices.API_OLD_BASE_URL + APIServices.OLD_STAGE)
                .build();

        this.apiServices = retrofit.create(APIServices.class);
    }

    public static LoanDetailClient getInstance(){

        if(instance == null)
            instance = new LoanDetailClient();

        return instance;
    }

    public Observable<LoanDetail> getLoanDetails(int id){
        Log.d(LOG_TAG, "APP: passing to apiServices.getLoanDetail: " + id);
        return apiServices.getLoanDetail(id);
    }

}
