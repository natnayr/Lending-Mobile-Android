package com.crowdo.p2pconnect.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cwdsg05 on 11/4/17.
 */

public class ErrorResponse {

    @SerializedName("server")
    @Expose
    private ServerResponse serverResponse;

    public ErrorResponse() {
    }

    public ErrorResponse(String message, int status) {
        this.serverResponse = new ServerResponse(message, status);
    }


    public ServerResponse getServerResponse() {
        return serverResponse;
    }

    public void setServerResponse(ServerResponse serverResponse) {
        this.serverResponse = serverResponse;
    }
}
