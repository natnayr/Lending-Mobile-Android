package com.crowdo.p2pconnect.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cwdsg05 on 7/6/17.
 */

public class BidRequest {

    @SerializedName("bid")
    @Expose
    private Bid bid;

    @SerializedName("site_config")
    @Expose
    private String siteConfig;

    @SerializedName("device_id")
    @Expose
    private String deviceId;

    public Bid getBid() {
        return bid;
    }

    public void setBid(Bid bid) {
        this.bid = bid;
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

    private class Bid{
        @SerializedName("invest_amount")
        @Expose
        private Integer investAmount;

        @SerializedName("loan_id")
        @Expose
        private Integer loanId;

        public Integer getInvestAmount() {
            return investAmount;
        }

        public void setInvestAmount(Integer investAmount) {
            this.investAmount = investAmount;
        }

        public Integer getLoanId() {
            return loanId;
        }

        public void setLoanId(Integer loanId) {
            this.loanId = loanId;
        }
    }
}
