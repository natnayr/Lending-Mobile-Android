package com.crowdo.p2pconnect.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cwdsg05 on 22/6/17.
 */
public class InvestBid {
    @SerializedName("bid_id")
    @Expose
    private int bidId;

    @SerializedName("invest_amount")
    @Expose
    private long investAmount;

    @SerializedName("original_invest_amount")
    @Expose
    private long originalInvestAmount;

    public InvestBid(int bidId, long investAmount){
        this.bidId = bidId;
        this.investAmount = investAmount;
    }

    public InvestBid(int bidId, long investAmount, long originalInvestAmount){
        this.bidId = bidId;
        this.investAmount = investAmount;
        this.originalInvestAmount = originalInvestAmount;
    }

    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public long getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(long investAmount) {
        this.investAmount = investAmount;
    }

    public long getOriginalInvestAmount() {
        return originalInvestAmount;
    }

    public void setOriginalInvestAmount(long originalInvestAmount) {
        this.originalInvestAmount = originalInvestAmount;
    }
}
