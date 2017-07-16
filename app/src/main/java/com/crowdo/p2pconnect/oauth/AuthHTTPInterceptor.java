package com.crowdo.p2pconnect.oauth;

import android.util.Log;
import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by cwdsg05 on 11/4/17.
 */

public class AuthHTTPInterceptor implements Interceptor{

    private String authToken;
    private final static String LOG_TAG = AuthHTTPInterceptor.class.getSimpleName();

    public AuthHTTPInterceptor(){
//        this.authToken = AuthAccountUtils.getAuthTokenFromRealm();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder builder = original.newBuilder()
                .header("Authorization", authToken);
        Log.d(LOG_TAG, "APP authToken: " + authToken);
        Request request = builder.build();
        return chain.proceed(request);
    }
}
