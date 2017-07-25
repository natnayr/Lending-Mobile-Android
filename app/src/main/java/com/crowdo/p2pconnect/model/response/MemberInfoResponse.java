package com.crowdo.p2pconnect.model.response;

import com.crowdo.p2pconnect.model.others.BankInfo;
import com.crowdo.p2pconnect.model.others.Server;
import com.squareup.moshi.Json;

/**
 * Created by cwdsg05 on 12/6/17.
 */

public class MemberInfoResponse {

    @Json(name = "server")
    private Server server;
    @Json(name = "name")
    private String name;
    @Json(name = "email")
    private String email;
    @Json(name = "user_id")
    private long userId;
    @Json(name = "number_of_pending_bids")
    private int numberOfPendingBids;
    @Json(name = "total_pending_bid_amount")
    private long totalPendingBidAmount;
    @Json(name = "available_cash_balance")
    private long availableCashBalance;
    @Json(name = "bank_info")
    private BankInfo bankInfo;

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getNumberOfPendingBids() {
        return numberOfPendingBids;
    }

    public void setNumberOfPendingBids(int numberOfPendingBids) {
        this.numberOfPendingBids = numberOfPendingBids;
    }

    public long getTotalPendingBidAmount() {
        return totalPendingBidAmount;
    }

    public void setTotalPendingBidAmount(long totalPendingBidAmount) {
        this.totalPendingBidAmount = totalPendingBidAmount;
    }

    public long getAvailableCashBalance() {
        return availableCashBalance;
    }

    public void setAvailableCashBalance(long availableCashBalance) {
        this.availableCashBalance = availableCashBalance;
    }

    public BankInfo getBankInfo() {
        return bankInfo;
    }

    public void setBankInfo(BankInfo bankInfo) {
        this.bankInfo = bankInfo;
    }
}

