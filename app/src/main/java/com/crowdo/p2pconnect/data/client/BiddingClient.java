package com.crowdo.p2pconnect.data.client;

import android.content.Context;

import com.andretietz.retroauth.AndroidAuthenticationHandler;
import com.andretietz.retroauth.AndroidTokenType;
import com.andretietz.retroauth.Retroauth;
import com.crowdo.p2pconnect.data.APIServices;
import com.crowdo.p2pconnect.data.ReceivingCookiesInterceptor;
import com.crowdo.p2pconnect.data.SendingCookiesInterceptor;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.model.request.DeleteBidRequest;
import com.crowdo.p2pconnect.model.request.NewBidRequest;
import com.crowdo.p2pconnect.model.response.BidOnlyResponse;
import com.crowdo.p2pconnect.model.response.CheckBidResponse;
import com.crowdo.p2pconnect.oauth.AuthProvider;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * Created by cwdsg05 on 7/6/17.
 */

public class BiddingClient implements ClientInterface{

    private static final String LOG_TAG = BiddingClient.class.getSimpleName();

    private Retrofit retrofit;
    private static BiddingClient instance;
    private APIServices apiServices;

    public BiddingClient(Context context){

//        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient.Builder()
//                .addInterceptor(loggingInterceptor)
                .addInterceptor(new SendingCookiesInterceptor(context))
                .addInterceptor(new ReceivingCookiesInterceptor(context))
                .build();

        AuthProvider provider = new AuthProvider();

        retrofit = new Retroauth.Builder<>(AndroidAuthenticationHandler.create(provider,
                AndroidTokenType.Factory.create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create())
                .client(httpClient)
                .baseUrl(APIServices.API_LIVE_BASE_URL + APIServices.LIVE_STAGE)
                .build();

        provider.onRetrofitCreated(retrofit);

        this.apiServices = retrofit.create(APIServices.class);
    }

    public Retrofit getRetrofit(){ return retrofit; }

    public static BiddingClient getInstance(Context context){
        if(instance == null)
            instance = new BiddingClient(context);
        return instance;
    }

    public Observable<Response<CheckBidResponse>> postCheckBid(long investAmount, int loanId,
                                                               String deviceId){
        return apiServices.postCheckBid(new NewBidRequest(investAmount, loanId,
                ConstantVariables.API_SITE_CONFIG_ID, deviceId));
    }

    public Observable<Response<BidOnlyResponse>> postAcceptBid(long investAmount, int loanId,
                                                               String deviceId){
        return apiServices.postAcceptBid(new NewBidRequest(investAmount, loanId,
                ConstantVariables.API_SITE_CONFIG_ID, deviceId));
    }

    public Observable<Response<BidOnlyResponse>> postDeleteBid(int bidId, String deviceId){
        return apiServices.postDeleteBid(new DeleteBidRequest(ConstantVariables.API_SITE_CONFIG_ID,
                deviceId, bidId));
    }
}
