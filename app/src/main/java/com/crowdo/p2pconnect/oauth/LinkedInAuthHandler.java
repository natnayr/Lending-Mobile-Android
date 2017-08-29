package com.crowdo.p2pconnect.oauth;

import com.github.scribejava.apis.LinkedInApi20;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by cwdsg05 on 29/8/17.
 */

public class LinkedInAuthHandler {

    private final String clientId;
    private final String clientKey;
    private final OAuth20Service auth20Service;

    public LinkedInAuthHandler(String clientId, String clientKey){
        this.clientId = clientId;
        this.clientKey = clientKey;
        auth20Service = new ServiceBuilder(clientId).
                apiSecret(clientKey)
                .scope(LinkedInConstants.AUTHORIZATION_REQUEST_SCOPE)
                .callback("http://www.google.com/")
                .state(LinkedInConstants.AUTHORIZATION_REQUEST_STATE)
                .build(LinkedInApi20.instance());
    }

    public String getAuthorizationUrl() throws IOException, InterruptedException, ExecutionException{
        return auth20Service.getAuthorizationUrl();
    }


}
