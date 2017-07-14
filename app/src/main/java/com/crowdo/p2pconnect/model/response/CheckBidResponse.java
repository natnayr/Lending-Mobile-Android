package com.crowdo.p2pconnect.model.response;

import com.crowdo.p2pconnect.model.others.Server;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cwdsg05 on 7/6/17.
 */

public class CheckBidResponse {

    @SerializedName("server")
    @Expose
    private Server server;

    @SerializedName("original_invest_amount")
    @Expose
    private Integer originalInvestAmount;

    @SerializedName("invest_amount")
    @Expose
    private Integer investAmount;

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Integer getOriginalInvestAmount() {
        return originalInvestAmount;
    }

    public void setOriginalInvestAmount(Integer originalInvestAmount) {
        this.originalInvestAmount = originalInvestAmount;
    }

    public Integer getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(Integer investAmount) {
        this.investAmount = investAmount;
    }


}
