package com.crowdo.p2pconnect.data.client;

import android.content.Context;

import com.crowdo.p2pconnect.data.APIServices;
import com.crowdo.p2pconnect.data.SendingCookiesInterceptor;
import com.crowdo.p2pconnect.data.ReceivingCookiesInterceptor;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.SharedPreferencesUtils;
import com.crowdo.p2pconnect.model.request.CheckoutUpdateRequest;
import com.crowdo.p2pconnect.model.request.UpdateBid;
import com.crowdo.p2pconnect.model.response.CheckoutSummaryResponse;
import com.crowdo.p2pconnect.model.response.CheckoutUpdateResponse;
import com.crowdo.p2pconnect.oauth.AuthHTTPInterceptor;
import com.crowdo.p2pconnect.oauth.CrowdoAccountGeneral;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;

/**
 * Created by cwdsg05 on 30/5/17.
 */

public class CheckoutClient implements ClientInterface{

    private static final String LOG_TAG = CheckoutClient.class.getSimpleName();

    private Retrofit retrofit;
    private APIServices apiServices;
    private static CheckoutClient instance;

    public CheckoutClient(Context context){
        final Gson gson = new GsonBuilder().serializeNulls().create();

        //Http Interceptor
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
                List<UpdateBid> updateBidBatch, String deviceId){
        return apiServices.postCheckoutUpdate(new CheckoutUpdateRequest(updateBidBatch, deviceId,
                ConstantVariables.API_SITE_CONFIG_ID));
    }
}
