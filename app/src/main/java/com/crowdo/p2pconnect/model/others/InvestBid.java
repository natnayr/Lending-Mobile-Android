package com.crowdo.p2pconnect.model.others;

import com.squareup.moshi.Json;

/**
 * Created by cwdsg05 on 22/6/17.
 */
public class InvestBid {

    @Json(name = "bid_id")
    private int bidId;

    @Json(name = "loan_id")
    private String loanId;

    @Json(name = "invest_amount")
    private long investAmount;

    @Json(name = "original_invest_amount")
    private long originalInvestAmount;

    public InvestBid(int bidId, Long investAmount){
        this.bidId = bidId;
        this.investAmount = investAmount;
    }

    public InvestBid(int bidId, String loanId, long investAmount, long originalInvestAmount) {
        this.bidId = bidId;
        this.loanId = loanId;
        this.investAmount = investAmount;
        this.originalInvestAmount = originalInvestAmount;
    }

    public int getBidId() {
        return bidId;
    }

    public void setBidId(int bidId) {
        this.bidId = bidId;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
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
