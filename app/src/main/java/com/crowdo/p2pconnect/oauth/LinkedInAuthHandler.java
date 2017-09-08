package com.crowdo.p2pconnect.oauth;

import android.content.Context;

import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.github.scribejava.apis.LinkedInApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by cwdsg05 on 8/9/17.
 */

public class LinkedInAuthHandler {
    private final OAuth20Service auth20Service;

    public LinkedInAuthHandler(String clientId, String clientKey, Context context){
        auth20Service = new ServiceBuilder(clientId).
                apiSecret(clientKey)
                .scope(LinkedinAuthConstant.AUTHORIZATION_REQUEST_SCOPE)
                .callback(ConstantVariables.AUTH_LINKEDIN_REDIRECT_URL)
                .state(ConstantVariables.getUniqueAndroidID(context))
                .build(LinkedInApi20.instance());
    }

    public String getAuthorizationUrl() throws IOException, InterruptedException, ExecutionException {
        return auth20Service.getAuthorizationUrl();
    }
}
