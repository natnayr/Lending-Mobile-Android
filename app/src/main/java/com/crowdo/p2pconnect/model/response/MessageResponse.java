package com.crowdo.p2pconnect.model.response;

import com.crowdo.p2pconnect.model.others.Server;
import com.squareup.moshi.Json;

/**
 * Created by cwdsg05 on 11/4/17.
 */

public class MessageResponse {

    @Json(name = "server")
    private Server server;

    public MessageResponse() {
    }

    public MessageResponse(String message, int status) {
        this.server = new Server(message, status);
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }
}
