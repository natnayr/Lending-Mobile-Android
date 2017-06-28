package com.crowdo.p2pconnect.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by cwdsg05 on 22/6/17.
 */

public class CheckoutBatchRequest {

    @SerializedName("bids")
    @Expose
    private Batch batch;

    @SerializedName("device_id")
    @Expose
    private String deviceId;

    @SerializedName("site_config")
    @Expose
    private String siteConfig;

    public CheckoutBatchRequest(List<InvestBid> batch, String deviceId,
                                String siteConfig){
        this.batch = new Batch();
        this.batch.setBatch(batch);
        this.deviceId = deviceId;
        this.siteConfig = siteConfig;
    }

    public Batch getBatch() {
        return batch;
    }

    public void setBatch(Batch batch) {
        this.batch = batch;
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

    private class Batch {

        @SerializedName("batch")
        @Expose
        private List<InvestBid> batch = null;

        public List<InvestBid> getBatch() {
            return batch;
        }

        public void setBatch(List<InvestBid> batch) {
            this.batch = batch;
        }
    }
}
