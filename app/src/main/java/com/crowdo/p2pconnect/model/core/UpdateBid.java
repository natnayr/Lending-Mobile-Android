package com.crowdo.p2pconnect.model.core;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cwdsg05 on 22/6/17.
 */
public class UpdateBid {
    @SerializedName("bid_id")
    @Expose
    private int bidId;

    @SerializedName("invest_amount")
    @Expose
    private int investAmount;

    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public int getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(int investAmount) {
        this.investAmount = investAmount;
    }
}
