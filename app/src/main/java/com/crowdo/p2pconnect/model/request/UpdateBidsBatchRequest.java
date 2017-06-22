package com.crowdo.p2pconnect.model.request;

import com.crowdo.p2pconnect.model.core.UpdateBid;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by cwdsg05 on 22/6/17.
 */

public class UpdateBidsBatchRequest {

    @SerializedName("bids")
    @Expose
    private UpdateBatch updateBatch;

    @SerializedName("device_id")
    @Expose
    private String deviceId;

    @SerializedName("site_config")
    @Expose
    private String siteConfig;

    public UpdateBatch getUpdateBatch() {
        return updateBatch;
    }

    public void setUpdateBatch(UpdateBatch updateBatch) {
        this.updateBatch = updateBatch;
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

    private class UpdateBatch {

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
