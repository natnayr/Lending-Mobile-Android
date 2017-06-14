package com.crowdo.p2pconnect.model.response;

/**
 * Created by cwdsg05 on 18/11/16.
 */

import com.crowdo.p2pconnect.model.core.Investment;
import com.crowdo.p2pconnect.model.core.Loan;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoanDetailResponse {

    @SerializedName("server")
    @Expose
    private ServerResponse serverResponse;

    @SerializedName("loan")
    @Expose
    private Loan loan;

    @SerializedName("bids")
    @Expose
    private List<Investment> bids;

    @SerializedName("first_repayment")
    @Expose
    private String firstRepayment;

    @SerializedName("last_repayment")
    @Expose
    private String lastRepayment;

    public ServerResponse getServerResponse(){
        return serverResponse;
    }

    public void setServerResponse(ServerResponse serverResponse){
        this.serverResponse = serverResponse;
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
