package com.crowdo.p2pconnect.data.client;

import com.crowdo.p2pconnect.data.APIServices;
import com.crowdo.p2pconnect.model.RegisteredMemberCheck;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

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

        final Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(APIServices.API_BASE_URL + APIServices.STAGE)
                .build();

        this.apiServices = retrofit.create(APIServices.class);
    }

    public static RegisteredMemberCheckClient getInstance(){
        if(instance == null)
            instance = new RegisteredMemberCheckClient();

        return instance;
    }

    public Observable<RegisteredMemberCheck> postUserCheck(String email){
        return apiServices.getUserCheck(email);
    }
}

