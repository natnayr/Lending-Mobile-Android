package com.crowdo.p2pconnect.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cwdsg05 on 7/6/17.
 */

public class AskBidRequest {

    @SerializedName("ask_bid")
    @Expose
    private AskBid askBid;

    @SerializedName("site_config")
    @Expose
    private String siteConfig;

    @SerializedName("device_id")
    @Expose
    private String deviceId;

    public AskBidRequest(long investAmount, int loanId, String siteConfig, String deviceId){
        this.siteConfig = siteConfig;
        this.deviceId = deviceId;
        askBid = new AskBid(investAmount, loanId);
    }

    public AskBid getAskBid() {
        return askBid;
    }

    public void setAskBid(AskBid askBid) {
        this.askBid = askBid;
    }

    public String getSiteConfig() {
        return siteConfig;
    }

    public void setSiteConfig(String siteConfig) {
        this.siteConfig = siteConfig;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    private class AskBid {
        @SerializedName("invest_amount")
        @Expose
        private Long investAmount;

        @SerializedName("loan_id")
        @Expose
        private Integer loanId;

        public AskBid(long investAmount, int loanId) {
            this.investAmount = investAmount;
            this.loanId = loanId;
        }

        public long getInvestAmount() {
            return investAmount;
        }

        public void setInvestAmount(long investAmount) {
            this.investAmount = investAmount;
        }

        public int getLoanId() {
            return loanId;
        }

        public void setLoanId(int loanId) {
            this.loanId = loanId;
        }
    }
}
