package com.crowdo.p2pconnect.model.response;

/**
 * Created by cwdsg05 on 18/11/16.
 */

import com.crowdo.p2pconnect.model.core.Investment;
import com.crowdo.p2pconnect.model.core.Loan;
import com.crowdo.p2pconnect.model.others.Server;
import com.squareup.moshi.Json;

import java.util.List;

public class LoanDetailResponse {

    @Json(name = "server")
    private Server server;

    @Json(name = "loan")
    private Loan loan;

    @Json(name = "bids")
    private List<Investment> bids;

    @Json(name = "first_repayment")
    private String firstRepayment;

    @Json(name = "last_repayment")
    private String lastRepayment;

    public Server getServer(){
        return server;
    }

    public void setServer(Server server){
        this.server = server;
    }

    public Loan getLoan(){
        return loan;
    }

    public void setLoan(Loan loan){
        this.loan = loan;
    }

    public List<Investment> getBids() {
        return bids;
    }

    public void setBids(List<Investment> bids) {
        this.bids = bids;
    }

    public String getFirstRepayment() {
        return firstRepayment;
    }

    public void setFirstRepayment(String firstRepayment) {
        this.firstRepayment = firstRepayment;
    }

    public String getLastRepayment() {
        return lastRepayment;
    }

    public void setLastRepayment(String lastRepayment) {
        this.lastRepayment = lastRepayment;
    }

}
