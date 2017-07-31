package com.crowdo.p2pconnect.data.client;

import android.content.Context;
import android.webkit.MimeTypeMap;

import com.andretietz.retroauth.AndroidAuthenticationHandler;
import com.andretietz.retroauth.AndroidTokenType;
import com.andretietz.retroauth.Retroauth;
import com.crowdo.p2pconnect.data.APIServices;
import com.crowdo.p2pconnect.data.ReceivingCookiesInterceptor;
import com.crowdo.p2pconnect.data.SendingCookiesInterceptor;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.model.request.TopUpSubmitRequest;
import com.crowdo.p2pconnect.model.response.TopUpHistoryResponse;
import com.crowdo.p2pconnect.model.response.TopUpSubmitResponse;
import com.crowdo.p2pconnect.oauth.AuthProvider;
import com.serjltt.moshi.adapters.FallbackOnNull;
import com.serjltt.moshi.adapters.SerializeNulls;
import com.squareup.moshi.Moshi;

import java.io.File;

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
import retrofit2.http.Multipart;

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
                .addInterceptor(new SendingCookiesInterceptor(context))
                .addInterceptor(new ReceivingCookiesInterceptor(context))
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

    public Observable<Response<TopUpSubmitResponse>> postTopUpInit(String transactionReference,
                                                                   String deviceId){
        return apiServices.postTopUpInit(new TopUpSubmitRequest(transactionReference, deviceId,
                ConstantVariables.API_SITE_CONFIG_ID, ConstantVariables.CURRENCY_IDR));
    }

    public Observable<Response<TopUpSubmitResponse>> putTopUpUpload(File fileUpload, String mediaType,
                                                                    long topUpId, String deviceId){

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        RequestBody requestBodyFile = RequestBody.create(MediaType.parse(mediaType), fileUpload);
        builder.addFormDataPart("top_up[transaction_proof]", fileUpload.getName(), requestBodyFile);
        MultipartBody requestMultipartBody = builder.build();

        return apiServices.putTopUpUpload(topUpId, deviceId, requestMultipartBody);
    }


    public Observable<Response<TopUpHistoryResponse>> getTopUpHistory(String deviceId){
        return apiServices.getTopUpHistory(deviceId, ConstantVariables.API_SITE_CONFIG_ID,
                ConstantVariables.CURRENCY_IDR);
    }

}
