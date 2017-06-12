package com.crowdo.p2pconnect.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cwdsg05 on 12/6/17.
 */

public class MemberInfoResponse {

    @SerializedName("server")
    @Expose
    private ServerResponse serverResponse;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("number_of_pending_bids")
    @Expose
    private int numberOfPendingBids;
    @SerializedName("total_pending_bids")
    @Expose
    private String totalPendingBids;
    @SerializedName("available_cash_balance")
    @Expose
    private int availableCashBalance;

    public ServerResponse getServerResponse() {
        return serverResponse;
    }

    public void setServerResponse(ServerResponse serverResponse) {
        this.serverResponse = serverResponse;
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

    public String getTotalPendingBids() {
        return totalPendingBids;
    }

    public void setTotalPendingBids(String totalPendingBids) {
        this.totalPendingBids = totalPendingBids;
    }

    public int getAvailableCashBalance() {
        return availableCashBalance;
    }

    public void setAvailableCashBalance(int availableCashBalance) {
        this.availableCashBalance = availableCashBalance;
    }
}
