package com.crowdo.p2pconnect.model.response;

import com.crowdo.p2pconnect.model.core.Member;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cwdsg05 on 23/3/17.
 */

public class AuthResponse {

    @SerializedName("serverResponse")
    @Expose
    public ServerResponse serverResponse;

    @SerializedName("auth_token")
    @Expose
    private String authToken;

    @SerializedName("auth_summary")
    @Expose
    private Member member;

    public ServerResponse getServerResponse(){ return serverResponse; }

    public void setServerResponse(ServerResponse serverResponse){ this.serverResponse = serverResponse; }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Member getMember() {
        return member;
    }

    public void setUser(Member member) {
        this.member = member;
    }

}
