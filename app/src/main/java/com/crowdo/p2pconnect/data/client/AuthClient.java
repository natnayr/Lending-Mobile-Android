package com.crowdo.p2pconnect.data.client;

import com.crowdo.p2pconnect.data.APIServices;
import com.crowdo.p2pconnect.data.request_model.LoginRequest;
import com.crowdo.p2pconnect.data.request_model.RegisterRequest;
import com.crowdo.p2pconnect.data.response_model.OAuthResponse;
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
 * Created by cwdsg05 on 22/3/17.
 */

public class AuthClient {

    private static final String LOG_TAG = AuthClient.class.getSimpleName();
    private static AuthClient instance;
    private APIServices apiServices;

    public AuthClient() {
        final Gson gson = new GsonBuilder()
                .serializeNulls()
                .create();

        //Http Inteceptor
        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        final Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .baseUrl(APIServices.API_LIVE_BASE_URL + APIServices.LIVE_STAGE)
                .build();

        this.apiServices = retrofit.create(APIServices.class);
    }

    public static AuthClient getInstance() {
        if (instance == null) {
            instance = new AuthClient();
        }
        return instance;
    }

    public Observable<Response<OAuthResponse>> loginUser(String email, String password, String deviceID) {
        return apiServices.postLoginUser(new LoginRequest(email, password, deviceID));
    }

    public Observable<Response<OAuthResponse>> registerUser(String email, String name, String password,
                                                            String passwordConfirmation, String localePreference,
                                                            String deviceId) {
        return apiServices.postRegisterUser(new RegisterRequest(name, email, password,
                passwordConfirmation, localePreference, deviceId));
    }


}
