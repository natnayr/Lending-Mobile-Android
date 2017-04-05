package com.crowdo.p2pconnect.data.response_model;

import com.crowdo.p2pconnect.model.Member;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cwdsg05 on 23/3/17.
 */

public class OAuthResponse {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("auth_token")
    @Expose
    private String authToken;

    @SerializedName("user")
    @Expose
    private Member member;

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

    public Member getMember() {
        return member;
    }

    public void setUser(Member member) {
        this.member = member;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "message:" + getMessage()
                + " status:"+ getStatus()
                + " authToken:" + getAuthToken();
    }
}
