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

    @SerializedName("loan_id")
    @Expose
    private String loanId;

    @SerializedName("invest_amount")
    @Expose
    private Long investAmount = null;

    @SerializedName("original_invest_amount")
    @Expose
    private Long originalInvestAmount = null;

    public InvestBid(int bidId, Long investAmount){
        this.bidId = bidId;
        this.investAmount = investAmount;
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

    public Long getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(Long investAmount) {
        this.investAmount = investAmount;
    }

    public Long getOriginalInvestAmount() {
        return originalInvestAmount;
    }

    public void setOriginalInvestAmount(Long originalInvestAmount) {
        this.originalInvestAmount = originalInvestAmount;
    }
}
