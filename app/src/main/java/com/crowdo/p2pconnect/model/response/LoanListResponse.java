package com.crowdo.p2pconnect.model.response;

import com.crowdo.p2pconnect.model.core.Loan;
import com.crowdo.p2pconnect.model.others.Server;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.squareup.moshi.Json;

import java.util.List;

/**
 * Created by cwdsg05 on 30/5/17.
 */

public class LoanListResponse {

    @Json(name = "server")
    private Server server;

    @Json(name = "loans")
    private List<Loan> loans;

    public Server getServer() {
        return server;
    }

    public List<Loan> getLoans() {
        return loans;
    }

}
