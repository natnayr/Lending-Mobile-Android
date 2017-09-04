package com.crowdo.p2pconnect.data.client;

import android.content.Context;

import com.andretietz.retroauth.AndroidAuthenticationHandler;
import com.andretietz.retroauth.AndroidTokenType;
import com.andretietz.retroauth.Retroauth;
import com.crowdo.p2pconnect.data.APIServices;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.model.request.TopUpSubmitRequest;
import com.crowdo.p2pconnect.model.request.WithdrawSubmitRequest;
import com.crowdo.p2pconnect.model.response.TopUpHistoryResponse;
import com.crowdo.p2pconnect.model.response.TopUpSubmitResponse;
import com.crowdo.p2pconnect.model.response.WithdrawHistoryResponse;
import com.crowdo.p2pconnect.model.response.WithdrawSubmitResponse;
import com.crowdo.p2pconnect.oauth.AuthProvider;
import com.serjltt.moshi.adapters.FallbackOnNull;
import com.serjltt.moshi.adapters.SerializeNulls;
import com.squareup.moshi.Moshi;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * Created by cwdsg05 on 31/7/17.
 */

public class WalletClient implements ClientInterface{

    private Retrofit retrofit;
    private APIServices apiServices;
    private static WalletClient instance;

    public WalletClient(Context context){
        final Moshi moshi = new Moshi.Builder()
                .add(FallbackOnNull.ADAPTER_FACTORY)
                .add(SerializeNulls.ADAPTER_FACTORY)
                .build();

        //Http Interceptor
        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
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

    @Override
    public Retrofit getRetrofit() {
        return retrofit;
    }

    public static WalletClient getInstance(Context context){
        if(instance == null)
            instance = new WalletClient(context);
        return instance;
    }

    public Observable<Response<TopUpSubmitResponse>> postRequestTopUpInit(String transactionReference,
                                                                          String deviceId){
        return apiServices.postRequestTopUpInit(new TopUpSubmitRequest(transactionReference, deviceId,
                ConstantVariables.API_SITE_CONFIG_ID, ConstantVariables.CURRENCY_IDR));
    }

    public Observable<Response<TopUpSubmitResponse>> putRequestTopUpUpload(File fileUpload, String mediaType,
                                                                           long topUpId, String deviceId){
        RequestBody fileBody = RequestBody.create(MediaType.parse(mediaType), fileUpload);
        MultipartBody.Part filePartBody = MultipartBody.Part
                .createFormData("top_up[transaction_proof]",
                        fileUpload.getName(),
                        fileBody);

        return apiServices.putRequestTopUpUpload(topUpId, deviceId, filePartBody);
    }

    public Observable<Response<TopUpHistoryResponse>> getTopUpHistory(String deviceId){
        return apiServices.getTopUpHistory(deviceId, ConstantVariables.API_SITE_CONFIG_ID,
                ConstantVariables.CURRENCY_IDR);
    }

    public Observable<Response<WithdrawSubmitResponse>> postRequestWithdraw(String amount, String deviceId){
        return apiServices.postRequestWithdraw(new WithdrawSubmitRequest(amount, deviceId,
                ConstantVariables.API_SITE_CONFIG_ID, ConstantVariables.CURRENCY_IDR));
    }

    public Observable<Response<WithdrawHistoryResponse>> getWithdrawHistory(String deviceId){
        return apiServices.getWithdrawHistory(deviceId, ConstantVariables.API_SITE_CONFIG_ID,
                ConstantVariables.CURRENCY_IDR);
    }

}
