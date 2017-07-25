package com.crowdo.p2pconnect.model.response;

import com.crowdo.p2pconnect.model.others.Server;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.squareup.moshi.Json;

/**
 * Created by cwdsg05 on 7/6/17.
 */

public class CheckBidResponse {

    @Json(name = "server")
    public Server server;
    @Json(name = "original_invest_amount")
    public long originalInvestAmount;
    @Json(name = "invest_amount")
    public long investAmount;

    public Server getServer() {
        return server;
    }

    public long getOriginalInvestAmount() {
        return originalInvestAmount;
    }

    public long getInvestAmount() {
        return investAmount;
    }

}
