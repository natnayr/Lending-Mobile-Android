package com.crowdo.p2pconnect.model.response;

import com.crowdo.p2pconnect.model.core.Loan;
import com.crowdo.p2pconnect.model.others.Server;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by cwdsg05 on 30/5/17.
 */

public class LoanListResponse {

    @SerializedName("server")
    @Expose
    public Server server;

    @SerializedName("loans")
    @Expose
    public List<Loan> loans;
}
