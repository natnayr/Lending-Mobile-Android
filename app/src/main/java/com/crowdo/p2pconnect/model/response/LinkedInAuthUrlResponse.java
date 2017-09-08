package com.crowdo.p2pconnect.model.response;

import com.crowdo.p2pconnect.model.others.Server;
import com.squareup.moshi.Json;

/**
 * Created by cwdsg05 on 6/9/17.
 */

public class LinkedInAuthUrlResponse {

    @Json(name = "server")
    private Server server;

    @Json(name = "oauth_url")
    private String oauthUrl;

    public LinkedInAuthUrlResponse() {
    }

    public LinkedInAuthUrlResponse(String message, int status) {
        this.server = new Server(message, status);
    }

    public Server getServer() {
        return server;
    }

    public String getOauthUrl() {
        return oauthUrl;
    }
}
