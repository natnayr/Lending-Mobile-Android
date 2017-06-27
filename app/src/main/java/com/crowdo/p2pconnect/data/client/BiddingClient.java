package com.crowdo.p2pconnect.data.client;

import android.content.Context;

import com.crowdo.p2pconnect.data.APIServices;
import com.crowdo.p2pconnect.data.ReceivingCookiesInterceptor;
import com.crowdo.p2pconnect.data.SendingCookiesInterceptor;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.SharedPreferencesUtils;
import com.crowdo.p2pconnect.model.request.DeleteBidRequest;
import com.crowdo.p2pconnect.model.request.AskBidRequest;
import com.crowdo.p2pconnect.model.response.BidStatusResponse;
import com.crowdo.p2pconnect.model.response.CheckBidResponse;
import com.crowdo.p2pconnect.oauth.AuthHTTPInterceptor;
import com.crowdo.p2pconnect.oauth.CrowdoAccountGeneral;
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
 * Created by cwdsg05 on 7/6/17.
 */

public class BiddingClient implements ClientInterface{

    private static final String LOG_TAG = BiddingClient.class.getSimpleName();

    private Retrofit retrofit;
    private static BiddingClient instance;
    private APIServices apiServices;

    public BiddingClient(Context context){
        final Gson gson = new GsonBuilder().serializeNulls().create();

        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //get token
        String authToken = SharedPreferencesUtils.getSharedPrefString(context,
                CrowdoAccountGeneral.AUTHTOKEN_SHARED_PREF_KEY, null);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new SendingCookiesInterceptor(context))
                .addInterceptor(new ReceivingCookiesInterceptor(context))
                .addInterceptor(new AuthHTTPInterceptor(authToken))
                .build();


        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .baseUrl(APIServices.API_LIVE_BASE_URL + APIServices.LIVE_STAGE)
                .build();

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
        return apiServices.postCheckBid(new AskBidRequest(investAmount, loanId,
                ConstantVariables.API_SITE_CONFIG_ID, deviceId));
    }

    public Observable<Response<BidStatusResponse>> postAcceptBid(long investAmount, int loanId,
                                                                 String deviceId){
        return apiServices.postAcceptBid(new AskBidRequest(investAmount, loanId,
                ConstantVariables.API_SITE_CONFIG_ID, deviceId));
    }

    public Observable<Response<BidStatusResponse>> postDeleteBid(int bidId, String deviceId){
        return apiServices.postDeleteBid(new DeleteBidRequest(ConstantVariables.API_SITE_CONFIG_ID,
                deviceId, bidId));
    }
}
