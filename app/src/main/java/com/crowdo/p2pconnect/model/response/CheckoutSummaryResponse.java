package com.crowdo.p2pconnect.model.response;

import com.crowdo.p2pconnect.model.core.Investment;
import com.crowdo.p2pconnect.model.core.Loan;
import com.crowdo.p2pconnect.model.others.Server;
import com.serjltt.moshi.adapters.FallbackOnNull;
import com.squareup.moshi.Json;

import java.util.List;

/**
 * Created by cwdsg05 on 30/5/17.
 */

public class CheckoutSummaryResponse {

    @Json(name = "server")
    private Server server;

    @Json(name = "number_of_pending_bids")
    @FallbackOnNull(fallbackInt = 0)
    private int numOfPendingBids;

    @Json(name = "total_pending_bids")
    @FallbackOnNull(fallbackLong = 0)
    private long totalPendingBids;

    @Json(name = "available_cash_balance")
    @FallbackOnNull(fallbackLong = 0)
    private long availableCashBalance;

    @Json(name = "bids")
    private List<Investment> bids;

    @Json(name = "loans")
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
