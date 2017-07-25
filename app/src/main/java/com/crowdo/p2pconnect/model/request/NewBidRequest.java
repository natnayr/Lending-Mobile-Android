package com.crowdo.p2pconnect.model.request;

import com.squareup.moshi.Json;

/**
 * Created by cwdsg05 on 7/6/17.
 */

public class NewBidRequest {

    @Json(name = "ask_bid")
    public NewBid newBid;
    @Json(name = "site_config")
    public String siteConfig;
    @Json(name = "device_id")
    public String deviceId;

    public NewBidRequest(long investAmount, int loanId, String siteConfig, String deviceId){
        this.siteConfig = siteConfig;
        this.deviceId = deviceId;
        newBid = new NewBid(investAmount, loanId);
    }

    private static class NewBid {

        @Json(name = "invest_amount")
        public long investAmount;
        @Json(name = "loan_id")
        public long loanId;

        public NewBid(long investAmount, int loanId) {
            this.investAmount = investAmount;
            this.loanId = loanId;
        }
    }
}
