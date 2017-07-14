package com.crowdo.p2pconnect.model.response;

import com.crowdo.p2pconnect.model.core.Investment;
import com.crowdo.p2pconnect.model.others.Server;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cwdsg05 on 7/6/17.
 */

public class BidOnlyResponse {

    @SerializedName("server")
    @Expose
    private Server server;

    @SerializedName("bid")
    @Expose
    private Investment bid;

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Investment getBid() {
        return bid;
    }

    public void setExistingBid(Investment existingBid) {
        this.bid = bid;
    }
}
