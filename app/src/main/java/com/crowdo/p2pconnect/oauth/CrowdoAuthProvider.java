package com.crowdo.p2pconnect.oauth;

import android.accounts.Account;

import com.andretietz.retroauth.AndroidToken;
import com.andretietz.retroauth.AndroidTokenType;
import com.andretietz.retroauth.Provider;
import com.andretietz.retroauth.TokenStorage;
import com.crowdo.p2pconnect.data.APIServices;
import com.crowdo.p2pconnect.helpers.ConstantVariables;

import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;

/**
 * Created by cwdsg05 on 16/7/17.
 */

public class CrowdoAuthProvider implements Provider<Account, AndroidTokenType, AndroidToken>{

    private APIServices apiServices;

    @Override
    public Request authenticateRequest(Request request, AndroidToken androidToken) {
        return request.newBuilder()
                .header("Authorization", androidToken.token)
                .build();
    }

    @Override
    public boolean retryRequired(int count, Response response, TokenStorage<Account, AndroidTokenType, AndroidToken> tokenStorage, Account account, AndroidTokenType androidTokenType, AndroidToken androidToken) {
        if(!response.isSuccessful()){
            if(response.code() == ConstantVariables.HTTP_UNAUTHORISED){
                tokenStorage.removeToken(account, androidTokenType, androidToken);
                return true;
            }
        }

        return false;
    }

    public void onRetrofitCreated(Retrofit retrofit){
        this.apiServices = retrofit.create(APIServices.class);
    }
}
