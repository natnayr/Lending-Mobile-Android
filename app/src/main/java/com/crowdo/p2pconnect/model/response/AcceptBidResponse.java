package com.crowdo.p2pconnect.model.response;

import com.crowdo.p2pconnect.model.core.Investment;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cwdsg05 on 7/6/17.
 */

public class AcceptBidResponse {

    @SerializedName("server")
    @Expose
    private ServerResponse server;

    @SerializedName("existing_bid")
    @Expose
    private Investment existingBid;

    public ServerResponse getServer() {
        return server;
    }

    public void setServer(ServerResponse server) {
        this.server = server;
    }

    public Investment getExistingBid() {
        return existingBid;
    }

    public void setExistingBid(Investment existingBid) {
        this.existingBid = existingBid;
    }
}
