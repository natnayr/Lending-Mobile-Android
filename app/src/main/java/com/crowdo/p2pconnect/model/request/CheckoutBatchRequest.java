package com.crowdo.p2pconnect.model.request;

import com.crowdo.p2pconnect.model.others.InvestBid;
import com.squareup.moshi.Json;

import java.util.List;

/**
 * Created by cwdsg05 on 22/6/17.
 */

public class CheckoutBatchRequest {

    @Json(name = "bids")
    public Batch batch;

    @Json(name = "device_id")
    public String deviceId;

    @Json(name = "site_config")
    public String siteConfig;

    public CheckoutBatchRequest(List<InvestBid> batch, String deviceId,
                                String siteConfig){
        this.batch = new Batch();
        this.batch.setBatch(batch);
        this.deviceId = deviceId;
        this.siteConfig = siteConfig;
    }


    private static class Batch {

        @Json(name = "batch")
        private List<InvestBid> batch = null;

        public List<InvestBid> getBatch() {
            return batch;
        }

        public void setBatch(List<InvestBid> batch) {
            this.batch = batch;
        }
    }
}
