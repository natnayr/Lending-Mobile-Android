package com.crowdo.p2pconnect.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cwdsg05 on 23/3/17.
 */

public class AuthResponse {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("auth_token")
    @Expose
    private String authToken;

    @SerializedName("auth_summary")
    @Expose
    private MemberResponse memberResponse;

    @SerializedName("status")
    @Expose
    private int status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public MemberResponse getMemberResponse() {
        return memberResponse;
    }

    public void setUser(MemberResponse memberResponse) {
        this.memberResponse = memberResponse;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
