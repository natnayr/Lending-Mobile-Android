package com.crowdo.p2pconnect.data.client;

import android.content.Context;

import com.andretietz.retroauth.AndroidAuthenticationHandler;
import com.andretietz.retroauth.AndroidTokenType;
import com.andretietz.retroauth.Retroauth;
import com.crowdo.p2pconnect.data.APIServices;
import com.crowdo.p2pconnect.data.SendingCookiesInterceptor;
import com.crowdo.p2pconnect.data.ReceivingCookiesInterceptor;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.model.response.LoanDetailResponse;
import com.crowdo.p2pconnect.model.response.LoanListResponse;
import com.crowdo.p2pconnect.oauth.CrowdoAuthProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

public class LoanClient implements ClientInterface{

    private static final String LOG_TAG = LoanClient.class.getSimpleName();

    private static LoanClient instance;
    private Retrofit retrofit;
    private APIServices apiServices;

    public LoanClient(Context context){
        final Gson gson = new GsonBuilder().serializeNulls().create();

//        //Http Inteceptor
//        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient.Builder()
//                .addInterceptor(loggingInterceptor)
                .addInterceptor(new SendingCookiesInterceptor(context))
                .addInterceptor(new ReceivingCookiesInterceptor(context))
                .build();


        CrowdoAuthProvider provider = new CrowdoAuthProvider();

        retrofit = new Retroauth.Builder<>(AndroidAuthenticationHandler.create(provider,
                AndroidTokenType.Factory.create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .baseUrl(APIServices.API_LIVE_BASE_URL + APIServices.LIVE_STAGE)
                .build();

        provider.onRetrofitCreated(retrofit);

        this.apiServices = retrofit.create(APIServices.class);
    }

    public Retrofit getRetrofit(){ return retrofit; }

    public static LoanClient getInstance(Context context){
        if(instance == null)
            instance = new LoanClient(context);
        return instance;
    }


    public Observable<Response<LoanListResponse>> getLiveLoans(String deviceId){
        return apiServices.getLoansList(deviceId, ConstantVariables.API_SITE_CONFIG_ID);
    }

    public Observable<Response<LoanDetailResponse>> getLoanDetails(int loanId, String deviceId){
        return apiServices.getLoanDetail(loanId, deviceId, ConstantVariables.API_SITE_CONFIG_ID);
    }

}
