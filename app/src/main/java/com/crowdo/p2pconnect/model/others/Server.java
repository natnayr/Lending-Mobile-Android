package com.crowdo.p2pconnect.model.others;

import com.squareup.moshi.Json;

/**
 * Created by cwdsg05 on 30/5/17.
 */

public class Server {

    @Json(name = "message")
    private String message;

    @Json(name = "status")
    private int status;

    public Server(String message, int status){
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
