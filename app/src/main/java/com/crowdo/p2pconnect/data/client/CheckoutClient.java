package com.crowdo.p2pconnect.data.client;

import android.content.Context;

import com.crowdo.p2pconnect.data.APIServices;
import com.crowdo.p2pconnect.data.SendingCookiesInterceptor;
import com.crowdo.p2pconnect.data.ReceivingCookiesInterceptor;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.model.response.CheckoutSummaryResponse;
import com.crowdo.p2pconnect.oauth.AuthenticationHTTPInterceptor;
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
 * Created by cwdsg05 on 30/5/17.
 */

public class CheckoutClient {

    private static final String LOG_TAG = CheckoutClient.class.getSimpleName();

    private Retrofit.Builder builder;
    private OkHttpClient.Builder httpClientBuilder;

    private static CheckoutClient instance;

    public CheckoutClient(Context context){
        final Gson gson = new GsonBuilder()
                .create();

        //Http Interceptor
        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClientBuilder = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new SendingCookiesInterceptor(context))
                .addInterceptor(new ReceivingCookiesInterceptor(context));

        builder = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(APIServices.API_LIVE_BASE_URL + APIServices.LIVE_STAGE);
    }

    public static CheckoutClient getInstance(Context context){
        if(instance == null)
            instance = new CheckoutClient(context);
        return instance;
    }

    public Observable<Response<CheckoutSummaryResponse>> getCheckoutSummary(String token,
                                                                            String deviceId){
        OkHttpClient httpClient = AuthenticationHTTPInterceptor
                .authTokenDecorator(token, httpClientBuilder).build();

        return builder
                .client(httpClient)
                .build()
                .create(APIServices.class)
                .getCheckoutSummary(deviceId, ConstantVariables.API_SITE_CONFIG_ID);
    }
}
