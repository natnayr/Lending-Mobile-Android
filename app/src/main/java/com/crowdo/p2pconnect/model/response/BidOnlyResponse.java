package com.crowdo.p2pconnect.model.response;

import com.crowdo.p2pconnect.model.core.Investment;
import com.crowdo.p2pconnect.model.others.Server;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.squareup.moshi.Json;

/**
 * Created by cwdsg05 on 7/6/17.
 */

public class BidOnlyResponse {

    @Json(name = "server")
    private Server server;

    @Json(name = "bid")
    private Investment bid;

    public Server getServer() {
        return server;
    }

    public Investment getBid() {
        return bid;
    }

}
