package com.crowdo.p2pconnect.data.client;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.crowdo.p2pconnect.data.APIServices;
import com.crowdo.p2pconnect.data.AddCookiesInterceptor;
import com.crowdo.p2pconnect.data.ReceivedCookiesInterceptor;
import com.crowdo.p2pconnect.model.LoanDetail;
import com.crowdo.p2pconnect.oauth.AuthenticationInterceptor;
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
 * Created by cwdsg05 on 6/12/16.
 */

public class LoanDetailClient {

    private static final String LOG_TAG = LoanListClient.class.getSimpleName();

    private Retrofit.Builder builder;
    private OkHttpClient.Builder httpClient;

    private static LoanDetailClient instance;

    public LoanDetailClient(Context context){
        final Gson gson = new GsonBuilder()
                .create();

        //Http Inteceptor
        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new AddCookiesInterceptor(context))
                .addInterceptor(new ReceivedCookiesInterceptor(context));

        builder = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(APIServices.PRASANTH_API_URL + APIServices.PRASANTH_API_STAGE);

    }

    public static LoanDetailClient getInstance(Context context){
        if(instance == null)
            instance = new LoanDetailClient(context);
        return instance;
    }

    public Retrofit createTokenService(final String authToken){
        if(!TextUtils.isEmpty(authToken)){
            final AuthenticationInterceptor interceptor = new AuthenticationInterceptor(authToken);
            if(!httpClient.interceptors().contains(interceptor)){
                httpClient.addInterceptor(interceptor);
            }
        }
        return builder.client(httpClient.build()).build();
    }


    public Observable<Response<LoanDetail>> getLoanDetails(String token, int id, String deviceId){
        Log.d(LOG_TAG, "APP passing to apiServices.getLoanDetail: " + id);

        return createTokenService(token)
                .create(APIServices.class)
                .getLoanDetail(id, deviceId);
    }

}
