package com.crowdo.p2pconnect.data.client;

import android.os.Build;
import android.util.Log;

import com.crowdo.p2pconnect.data.APIServices;
import com.crowdo.p2pconnect.data.response.AuthResponse;
import com.crowdo.p2pconnect.model.Member;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by cwdsg05 on 22/3/17.
 */

public class LoginClient {

    private static final String LOG_TAG = LoginClient.class.getSimpleName();
    private static LoginClient instance;
    private APIServices apiServices;

    public LoginClient(){
        final Gson gson = new GsonBuilder()
                .create();

        final Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(APIServices.API_TEST_URL + APIServices.TEST_STAGE)
                .build();

        this.apiServices = retrofit.create(APIServices.class);
    }

    public static LoginClient getInstance(){
        if(instance == null){
            instance = new LoginClient();
        }
        return instance;
    }

    public Observable<AuthResponse> loginUser(String email, String password, String deviceID){
        Log.d(LOG_TAG, "APP: passing to apiServices.loginUser, email:" + email
                + " device_id:" + deviceID);
        return apiServices.postLoginUser(new Input(email, password, deviceID));
    }

    public class Input{
        String email;
        String password;
        String deviceId;
        public Input(String email, String password, String deviceId){
            this.email = email;
            this.password = password;
            this.deviceId = deviceId;
        }
    }

}
