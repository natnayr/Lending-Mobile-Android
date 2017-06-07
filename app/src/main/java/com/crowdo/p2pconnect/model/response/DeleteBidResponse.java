package com.crowdo.p2pconnect.model.response;

import com.crowdo.p2pconnect.model.core.Investment;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cwdsg05 on 7/6/17.
 */

public class DeleteBidResponse {

    @SerializedName("server")
    @Expose
    private ServerResponse server;

    @SerializedName("deleted_bid")
    @Expose
    private Investment deletedBid;

    public ServerResponse getServer() {
        return server;
    }

    public void setServer(ServerResponse server) {
        this.server = server;
    }

    public Investment getDeletedBid() {
        return deletedBid;
    }

    public void setDeletedBid(Investment deletedBid) {
        this.deletedBid = deletedBid;
    }
}
