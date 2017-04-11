package com.crowdo.p2pconnect.data.client;

import com.crowdo.p2pconnect.data.APIServices;
import com.crowdo.p2pconnect.model.LoanListItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

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

        //Http Inteceptor
        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        final Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .baseUrl(APIServices.PRASANTH_API_URL + APIServices.PRASANTH_API_STAGE)
                .build();

        this.apiServices = retrofit.create(APIServices.class);
    }

    public static LoanListClient getInstance(){
        if(instance == null)
            instance = new LoanListClient();

        return instance;
    }

    public Observable<Response<List<LoanListItem>>> getLiveLoans(String token, String deviceId){
        return apiServices.getLoansList(token, deviceId);
    }

}
