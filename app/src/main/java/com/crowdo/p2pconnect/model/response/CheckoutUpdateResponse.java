package com.crowdo.p2pconnect.model.response;

import com.crowdo.p2pconnect.model.others.Server;
import com.crowdo.p2pconnect.model.others.InvestBid;
import com.serjltt.moshi.adapters.FallbackOnNull;
import com.squareup.moshi.Json;

import java.util.List;

/**
 * Created by cwdsg05 on 22/6/17.
 */

public class CheckoutUpdateResponse {

    @Json(name = "server")
    private Server server;

    @Json(name = "array")
    private List<InvestBid> array = null;

    @Json(name = "total_pending_bids")
    private String totalPendingBids;

    @Json(name = "total_cash_balance")
    @FallbackOnNull(fallbackInt = 0)
    private int totalCashBalance;

    public Server getServer() {
        return server;
    }

    public List<InvestBid> getArray() {
        return array;
    }

    public String getTotalPendingBids() {
        return totalPendingBids;
    }

    public int getTotalCashBalance() {
        return totalCashBalance;
    }

}
