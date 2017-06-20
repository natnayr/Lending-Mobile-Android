package com.crowdo.p2pconnect.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cwdsg05 on 20/6/17.
 */

public class DeleteBidRequest {

    @SerializedName("site_config")
    @Expose
    private String siteConfig;

    @SerializedName("device_id")
    @Expose
    private String deviceId;

    @SerializedName("bid_id")
    @Expose
    private Integer bidId;

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

    public Integer getBidId() {
        return bidId;
    }

    public void setBidId(Integer bidId) {
        this.bidId = bidId;
    }
}
