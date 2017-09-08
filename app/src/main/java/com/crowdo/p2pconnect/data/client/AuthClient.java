package com.crowdo.p2pconnect.data.client;

import android.content.Context;

import com.crowdo.p2pconnect.data.APIServices;
import com.crowdo.p2pconnect.model.request.LoginRequest;
import com.crowdo.p2pconnect.model.request.RegisterRequest;
import com.crowdo.p2pconnect.model.request.SocialRequest;
import com.crowdo.p2pconnect.model.response.AuthResponse;
import com.crowdo.p2pconnect.model.response.LinkedInAuthUrlResponse;
import com.serjltt.moshi.adapters.FallbackOnNull;
import com.serjltt.moshi.adapters.SerializeNulls;
import com.squareup.moshi.Moshi;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * Created by cwdsg05 on 22/3/17.
 */

public class AuthClient implements ClientInterface{

    private static final String LOG_TAG = AuthClient.class.getSimpleName();
    private static AuthClient instance;
    private APIServices apiServices;
    private Retrofit retrofit;

    public AuthClient(Context context) {

        final Moshi moshi = new Moshi.Builder()
                .add(FallbackOnNull.ADAPTER_FACTORY)
                .add(SerializeNulls.ADAPTER_FACTORY)
                .build();

        //Http Inteceptor
        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .client(httpClient)
                .baseUrl(APIServices.API_LIVE_BASE_URL + APIServices.LIVE_STAGE)
                .build();

        this.apiServices = retrofit.create(APIServices.class);
    }

    public static AuthClient getInstance(Context context) {
        if (instance == null) {
            instance = new AuthClient(context);
        }
        return instance;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public Observable<Response<AuthResponse>> postLoginUser(String email, String password, String deviceID) {
        //no token needed thus Retrofit instance can be built at constructor
        return apiServices.postLoginUser(new LoginRequest(email, password, deviceID));
    }

    public Observable<Response<AuthResponse>> postRegisterUser(String email, String name, String password,
                                                               String passwordConfirmation,
                                                               String localePreference, String deviceId) {
        //no token needed thus Retrofit instance can be built at constructor
        return apiServices.postRegisterUser(new RegisterRequest(name, email, password,
                passwordConfirmation, localePreference, deviceId));
    }

    public Observable<Response<LinkedInAuthUrlResponse>> getRequestLinkedInOAuthUrl(String deviceID){
        return apiServices.getRequestOauthUrl(deviceID);
    }

    public Observable<Response<AuthResponse>> postFBSocialAuthUser(String provider, String uid,
                                                                   String accessToken, String localePreference,
                                                                   String deviceId){
        return apiServices.postFBSocialAuthUser(new SocialRequest(provider, uid, accessToken,
                localePreference, deviceId));
    }

}
