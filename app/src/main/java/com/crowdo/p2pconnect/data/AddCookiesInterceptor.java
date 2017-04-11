package com.crowdo.p2pconnect.data;

import android.content.Context;
import android.util.Log;

import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.SharedPreferencesUtils;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by cwdsg05 on 11/4/17.
 */

public class AddCookiesInterceptor implements Interceptor{

    private static final String LOG_TAG = AddCookiesInterceptor.class.getSimpleName();

    private Context mContext;

    public AddCookiesInterceptor(Context context){
        this.mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request.Builder builder = chain.request().newBuilder();
        HashSet<String> cookieSet = (HashSet<String>) SharedPreferencesUtils.getSharedPrefStringSet(mContext,
                ConstantVariables.PREF_KEY_COOKIES, new HashSet<String>());

        for(String cookie : cookieSet){
            builder.addHeader("Cookie", cookie);
            Log.d(LOG_TAG, "APP OkHttp Adding Header: " + cookie);
        }

        return chain.proceed(builder.build());
    }
}
