package com.crowdo.p2pconnect.model.response;

import com.crowdo.p2pconnect.model.core.Investment;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cwdsg05 on 7/6/17.
 */

public class BidStatusResponse {

    @SerializedName("server")
    @Expose
    private ServerResponse server;

    @SerializedName("bid")
    @Expose
    private Investment bid;

    public ServerResponse getServer() {
        return server;
    }

    public void setServer(ServerResponse server) {
        this.server = server;
    }

    public Investment getBid() {
        return bid;
    }

    public void setExistingBid(Investment existingBid) {
        this.bid = bid;
    }
}
