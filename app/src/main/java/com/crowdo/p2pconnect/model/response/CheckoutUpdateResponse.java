package com.crowdo.p2pconnect.model.response;

import com.crowdo.p2pconnect.model.others.Server;
import com.crowdo.p2pconnect.model.request.InvestBid;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by cwdsg05 on 22/6/17.
 */

public class CheckoutUpdateResponse {

    @SerializedName("server")
    @Expose
    private Server server;

    @SerializedName("array")
    @Expose
    private List<InvestBid> array = null;

    @SerializedName("total_pending_bids")
    @Expose
    private String totalPendingBids;

    @SerializedName("total_cash_balance")
    @Expose
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
