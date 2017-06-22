package com.crowdo.p2pconnect.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by cwdsg05 on 22/6/17.
 */

public class CheckoutUpdateRequest {

    @SerializedName("bids")
    @Expose
    private PreUpdateBatch preUpdateBatch;

    @SerializedName("device_id")
    @Expose
    private String deviceId;

    @SerializedName("site_config")
    @Expose
    private String siteConfig;

    public CheckoutUpdateRequest(List<UpdateBid> batch, String deviceId,
                                 String siteConfig){
        this.preUpdateBatch = new PreUpdateBatch();
        this.preUpdateBatch.setBatch(batch);
        this.deviceId = deviceId;
        this.siteConfig = siteConfig;
    }

    public PreUpdateBatch getPreUpdateBatch() {
        return preUpdateBatch;
    }

    public void setPreUpdateBatch(PreUpdateBatch preUpdateBatch) {
        this.preUpdateBatch = preUpdateBatch;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getSiteConfig() {
        return siteConfig;
    }

    public void setSiteConfig(String siteConfig) {
        this.siteConfig = siteConfig;
    }

    private class PreUpdateBatch {

        @SerializedName("batch")
        @Expose
        private List<UpdateBid> batch = null;

        public List<UpdateBid> getBatch() {
            return batch;
        }

        public void setBatch(List<UpdateBid> batch) {
            this.batch = batch;
        }
    }
}
