package com.crowdo.p2pconnect.oauth;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by cwdsg05 on 11/4/17.
 */

public class AuthenticationHTTPInterceptor implements Interceptor{

    private String authToken;

    public AuthenticationHTTPInterceptor(String token){
        this.authToken = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder builder = original.newBuilder()
                .header("Authorization", authToken);

        Request request = builder.build();
        return chain.proceed(request);
    }

    public static OkHttpClient.Builder authTokenDecorator(final String authToken, OkHttpClient.Builder httpClient){
        if(!TextUtils.isEmpty(authToken)){
            final AuthenticationHTTPInterceptor interceptor = new AuthenticationHTTPInterceptor(authToken);
            if(!httpClient.interceptors().contains(interceptor)){
                httpClient.addInterceptor(interceptor);
            }
        }
        return httpClient;
    }
}
