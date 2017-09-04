package com.crowdo.p2pconnect.data.client;

import android.content.Context;

import com.andretietz.retroauth.AndroidAuthenticationHandler;
import com.andretietz.retroauth.AndroidTokenType;
import com.andretietz.retroauth.Retroauth;
import com.crowdo.p2pconnect.data.APIServices;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.model.request.CheckoutBatchRequest;
import com.crowdo.p2pconnect.model.others.InvestBid;
import com.crowdo.p2pconnect.model.response.CheckoutSummaryResponse;
import com.crowdo.p2pconnect.model.response.CheckoutUpdateResponse;
import com.crowdo.p2pconnect.model.response.MessageResponse;
import com.crowdo.p2pconnect.oauth.AuthProvider;
import com.serjltt.moshi.adapters.FallbackOnNull;
import com.serjltt.moshi.adapters.SerializeNulls;
import com.squareup.moshi.Moshi;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * Created by cwdsg05 on 30/5/17.
 */

public class CheckoutClient implements ClientInterface{

    private static final String LOG_TAG = CheckoutClient.class.getSimpleName();

    private Retrofit retrofit;
    private APIServices apiServices;
    private static CheckoutClient instance;

    public CheckoutClient(Context context){

        final Moshi moshi = new Moshi.Builder()
                .add(FallbackOnNull.ADAPTER_FACTORY)
                .add(SerializeNulls.ADAPTER_FACTORY)
                .build();

        //Http Interceptor
        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        AuthProvider provider = new AuthProvider();

        retrofit = new Retroauth.Builder<>(AndroidAuthenticationHandler.create(provider,
                AndroidTokenType.Factory.create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .client(httpClient)
                .baseUrl(APIServices.API_LIVE_BASE_URL + APIServices.LIVE_STAGE)
                .build();

        provider.onRetrofitCreated(retrofit);

        this.apiServices = retrofit.create(APIServices.class);
    }

    public static CheckoutClient getInstance(Context context){
        if(instance == null)
            instance = new CheckoutClient(context);
        return instance;
    }

    public Retrofit getRetrofit(){
        return retrofit;
    }

    public Observable<Response<CheckoutSummaryResponse>> getCheckoutSummary(String deviceId){

        return apiServices.getCheckoutSummary(deviceId, ConstantVariables.API_SITE_CONFIG_ID);
    }

    public Observable<Response<CheckoutUpdateResponse>> postCheckoutUpdate(
            List<InvestBid> investBidBatch, String deviceId){
        return apiServices.postCheckoutUpdate(new CheckoutBatchRequest(investBidBatch, deviceId,
                ConstantVariables.API_SITE_CONFIG_ID));
    }

    public Observable<Response<MessageResponse>> postCheckoutConfirm(
            List<InvestBid> investBidBatch, String deviceId){
        return apiServices.postCheckoutConfirm(new CheckoutBatchRequest(investBidBatch, deviceId,
                ConstantVariables.API_SITE_CONFIG_ID));
    }

}
