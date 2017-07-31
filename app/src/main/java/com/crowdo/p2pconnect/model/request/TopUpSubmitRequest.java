package com.crowdo.p2pconnect.model.request;

import com.squareup.moshi.Json;

/**
 * Created by cwdsg05 on 31/7/17.
 */

public class TopUpSubmitRequest {

    @Json(name = "top_up")
    public TopUpAttachedInfo topUp;
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
    public TopUpSubmitRequest() {
    }

    /**
     *
     * @param siteConfig
     * @param deviceId
     * @param currency
     */
    public TopUpSubmitRequest(String transactionReference, String deviceId, String siteConfig, String currency) {
        super();
        this.topUp = new TopUpAttachedInfo(transactionReference);
        this.deviceId = deviceId;
        this.siteConfig = siteConfig;
        this.currency = currency;
    }

    private static class TopUpAttachedInfo{

        @Json(name = "transaction_reference")
        public String transactionReference;
        /**
         * No args constructor for use in serialization
         *
         */
        public TopUpAttachedInfo() {
        }

        /**
         *
         * @param transactionReference
         */
        public TopUpAttachedInfo(String transactionReference) {
            super();
            this.transactionReference = transactionReference;
        }
    }


}
