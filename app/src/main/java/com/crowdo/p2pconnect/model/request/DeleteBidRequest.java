package com.crowdo.p2pconnect.model.request;

import com.squareup.moshi.Json;

/**
 * Created by cwdsg05 on 20/6/17.
 */

public class DeleteBidRequest {

    @Json(name = "site_config")
    public String siteConfig;

    @Json(name = "device_id")
    public String deviceId;

    @Json(name = "bid_id")
    public Integer bidId;

    /**
     * No args constructor for use in serialization
     *
     */
    public DeleteBidRequest() {
    }

    /**
     *
     * @param bidId
     * @param siteConfig
     * @param deviceId
     */
    public DeleteBidRequest(String siteConfig, String deviceId, Integer bidId) {
        super();
        this.siteConfig = siteConfig;
        this.deviceId = deviceId;
        this.bidId = bidId;
    }
}
