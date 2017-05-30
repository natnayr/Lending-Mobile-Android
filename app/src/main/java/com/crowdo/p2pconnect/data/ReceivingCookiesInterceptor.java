package com.crowdo.p2pconnect.data;

import android.content.Context;
import android.util.Log;

import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.SharedPreferencesUtils;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by cwdsg05 on 11/4/17.
 */

public class ReceivingCookiesInterceptor implements Interceptor{

    private final static String LOG_TAG = ReceivingCookiesInterceptor.class.getSimpleName();
    private Context mContext;

    public ReceivingCookiesInterceptor(Context context){
        this.mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        if(originalResponse.header("Set-Cookie") != null){
            if(!originalResponse.header("Set-Cookie").isEmpty()){
                HashSet<String> cookies = new HashSet<>();

                for(String header : originalResponse.headers("Set-Cookie")){
                    Log.d(LOG_TAG, "APP Cookie Header Received: " + header);
                    cookies.add(header);
                }

                SharedPreferencesUtils.setSharedPrefStringSet(mContext,
                        ConstantVariables.PREF_KEY_COOKIES, cookies);
            }
        }


        return originalResponse;
    }
}
