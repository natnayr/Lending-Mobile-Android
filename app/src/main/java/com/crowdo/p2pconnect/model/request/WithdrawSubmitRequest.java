package com.crowdo.p2pconnect.model.request;

import com.squareup.moshi.Json;

/**
 * Created by cwdsg05 on 14/8/17.
 */

public class WithdrawSubmitRequest {

    @Json(name = "withdrawal")
    public WithdrawAttachedInfo withdrawal;

    @Json(name = "device_id")
    public String deviceId;

    @Json(name = "site_config")
    public String siteConfig;

    @Json(name = "currency")
    public String currency;

    /**
     * No args constructor for use in serialization
     *
     */
    public WithdrawSubmitRequest() {
    }

    /**
     *
     * @param amount
     * @param siteConfig
     * @param deviceId
     * @param currency
     */
    public WithdrawSubmitRequest(String amount, String deviceId, String siteConfig, String currency) {
        super();
        this.withdrawal = new WithdrawAttachedInfo(amount);
        this.deviceId = deviceId;
        this.siteConfig = siteConfig;
        this.currency = currency;
    }

    private static class WithdrawAttachedInfo{

        @Json(name = "amount")
        public String amount;

        /**
         * No args constructor for use in serialization
         *
         */
        public WithdrawAttachedInfo() {
        }

        /**
         *
         * @param amount
         */
        public WithdrawAttachedInfo(String amount) {
            super();
            this.amount = amount;
        }

    }
}
