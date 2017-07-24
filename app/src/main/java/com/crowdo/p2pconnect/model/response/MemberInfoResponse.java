package com.crowdo.p2pconnect.model.response;

import com.crowdo.p2pconnect.model.others.BankInfo;
import com.crowdo.p2pconnect.model.others.Server;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cwdsg05 on 12/6/17.
 */

public class MemberInfoResponse {

    @SerializedName("server")
    @Expose
    private Server server;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("number_of_pending_bids")
    @Expose
    private int numberOfPendingBids;
    @SerializedName("total_pending_bid_amount")
    @Expose
    private double totalPendingBidAmount;
    @SerializedName("available_cash_balance")
    @Expose
    private int availableCashBalance;
    @SerializedName("bank_info")
    @Expose
    private BankInfo bankInfo;

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getNumberOfPendingBids() {
        return numberOfPendingBids;
    }

    public void setNumberOfPendingBids(int numberOfPendingBids) {
        this.numberOfPendingBids = numberOfPendingBids;
    }

    public double getTotalPendingBidAmount() {
        return totalPendingBidAmount;
    }

    public void setTotalPendingBidAmount(double totalPendingBidAmount) {
        this.totalPendingBidAmount = totalPendingBidAmount;
    }

    public int getAvailableCashBalance() {
        return availableCashBalance;
    }

    public void setAvailableCashBalance(int availableCashBalance) {
        this.availableCashBalance = availableCashBalance;
    }

    public BankInfo getBankInfo() {
        return bankInfo;
    }

    public void setBankInfo(BankInfo bankInfo) {
        this.bankInfo = bankInfo;
    }
}
