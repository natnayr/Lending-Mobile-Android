package com.crowdo.p2pconnect.model.response;

import com.crowdo.p2pconnect.model.core.Investment;
import com.crowdo.p2pconnect.model.core.Loan;
import com.crowdo.p2pconnect.model.others.Server;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by cwdsg05 on 30/5/17.
 */

public class CheckoutSummaryResponse {

    @SerializedName("server")
    @Expose
    private Server server;

    @SerializedName("number_of_pending_bids")
    @Expose
    private int numOfPendingBids;

    @SerializedName("total_pending_bids")
    @Expose
    private long totalPendingBids;

    @SerializedName("available_cash_balance")
    @Expose
    private long availableCashBalance;

    @SerializedName("bids")
    @Expose
    private List<Investment> bids;

    @SerializedName("loans")
    @Expose
    private List<Loan> loans;

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public int getNumOfPendingBids() {
        return numOfPendingBids;
    }

    public void setNumOfPendingBids(int numOfPendingBids) {
        this.numOfPendingBids = numOfPendingBids;
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

    public List<Investment> getBids() {
        return bids;
    }

    public void setBids(List<Investment> bids) {
        this.bids = bids;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }
}
