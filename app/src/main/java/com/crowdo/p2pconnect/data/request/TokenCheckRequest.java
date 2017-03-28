package com.crowdo.p2pconnect.data.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cwdsg05 on 28/3/17.
 */

public class TokenCheckRequest {

    @SerializedName("auth_token")
    @Expose
    String authToken;

    public TokenCheckRequest(){
    }

    public TokenCheckRequest(String authToken){
        super();
        this.authToken = authToken;
    }
}
