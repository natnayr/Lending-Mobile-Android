package com.crowdo.p2pconnect.model.response;

import com.crowdo.p2pconnect.model.core.Investment;
import com.crowdo.p2pconnect.model.core.Loan;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by cwdsg05 on 30/5/17.
 */

public class CheckoutSummaryResponse {

    @SerializedName("serverResponse")
    @Expose
    private ServerResponse serverResponse;

    @SerializedName("total_pending_bids")
    @Expose
    private long totalPendingBids;

    @SerializedName("available_cash_balance")
    @Expose
    private long availableCashBalance;

    @SerializedName("required_amount")
    @Expose
    private String requiredAmount;

    @SerializedName("bids")
    @Expose
    private List<Investment> bids;

    @SerializedName("loans")
    @Expose
    private List<Loan> loans;

    public ServerResponse getServerResponse() {
        return serverResponse;
    }

    public void setServerResponse(ServerResponse serverResponse) {
        this.serverResponse = serverResponse;
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

    public List<Loan> getLoans() {
        return loans;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }
}
