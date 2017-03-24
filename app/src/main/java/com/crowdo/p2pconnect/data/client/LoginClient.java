package com.crowdo.p2pconnect.data.client;

import android.util.Log;
import com.crowdo.p2pconnect.data.APIServices;
import com.crowdo.p2pconnect.data.response.AuthResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Response;
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
                .serializeNulls()
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

    public Observable<Response<AuthResponse>> loginUser(String email, String password, String deviceID){
        Log.d(LOG_TAG, "APP: apiServices.loginUser execute  email:" + email
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
