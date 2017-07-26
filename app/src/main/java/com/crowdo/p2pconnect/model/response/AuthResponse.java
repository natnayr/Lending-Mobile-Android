package com.crowdo.p2pconnect.model.response;

import com.crowdo.p2pconnect.model.core.Member;
import com.crowdo.p2pconnect.model.others.Server;
import com.squareup.moshi.Json;

/**
 * Created by cwdsg05 on 23/3/17.
 */

public class AuthResponse {

    @Json(name = "server")
    public Server server;

    @Json(name = "auth_token")
    private String authToken;

    @Json(name = "auth_summary")
    private Member member;

    public Server getServer(){ return server; }

    public String getAuthToken() {
        return authToken;
    }

    public Member getMember() {
        return member;
    }

}
