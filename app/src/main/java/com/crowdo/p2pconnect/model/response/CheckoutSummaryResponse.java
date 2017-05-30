package com.crowdo.p2pconnect.model.response;

import com.crowdo.p2pconnect.model.core.Investment;
import com.crowdo.p2pconnect.model.core.Server;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by cwdsg05 on 30/5/17.
 */

public class CheckoutSummaryResponse {

    @SerializedName("server")
    @Expose
    public Server server;

    @SerializedName("total_pending_bids")
    @Expose
    public long totalPendingBids;

    @SerializedName("available_cash_balance")
    @Expose
    public long availableCashBalance;

    @SerializedName("required_amount")
    @Expose
    public String requiredAmount;

    @SerializedName("bids")
    @Expose
    public List<Investment> bids;

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public long getTotalPendingBids() {
        return totalPendingBids;
    }

    public void setTotalPendingBids(long totalPendingBids) {
        this.totalPendingBids = totalPendingBids;
    }

    public long getAvailableCashBalance() {
        return availableCashBalance;
    }

    public void setAvailableCashBalance(long availableCashBalance) {
        this.availableCashBalance = availableCashBalance;
    }

    public String getRequiredAmount() {
        return requiredAmount;
    }

    public void setRequiredAmount(String requiredAmount) {
        this.requiredAmount = requiredAmount;
    }

    public List<Investment> getBids() {
        return bids;
    }

    public void setBids(List<Investment> bids) {
        this.bids = bids;
    }

}
