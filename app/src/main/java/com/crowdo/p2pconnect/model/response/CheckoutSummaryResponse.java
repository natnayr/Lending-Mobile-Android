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
    
    public int getNumOfPendingBids() {
        return numOfPendingBids;
    }

    public long getTotalPendingBids() {
        return totalPendingBids;
    }

    public long getAvailableCashBalance() {
        return availableCashBalance;
    }

    public List<Investment> getBids() {
        return bids;
    }

    public List<Loan> getLoans() {
        return loans;
    }

}
