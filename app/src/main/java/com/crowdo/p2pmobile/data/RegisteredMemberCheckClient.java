package com.crowdo.p2pmobile.data;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by cwdsg05 on 28/12/16.
 */

public class RegisteredMemberCheckClient {

    private static final String LOG_TAG = RegisteredMemberCheckClient.class.getSimpleName();

    private static RegisteredMemberCheckClient instance;
    private APIServices apiServices;

    public RegisteredMemberCheckClient(){
        final Gson gson = new GsonBuilder()
                .create();

        OkHttpClient.Builder client = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client.addInterceptor(loggingInterceptor);

        final Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(APIServices.LAMBDA_BASE_URL + APIServices.STAGE)
                .client(client.build())
                .build();

        this.apiServices = retrofit.create(APIServices.class);
    }

    public static RegisteredMemberCheckClient getInstance(){
        if(instance == null)
            instance = new RegisteredMemberCheckClient();

        return instance;
    }

    public Call<RegisteredMemberCheck> postUserCheck(String email){
        Log.d(LOG_TAG, "TEST: parsing to apiServices.postUserCheck: " + email);
        Map<String, String> requestBody = new HashMap<String, String>();
        requestBody.put("email", email);
        return apiServices.postUserCheck(requestBody);
    }
}

