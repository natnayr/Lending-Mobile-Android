package com.crowdo.p2pconnect.model.response;

/**
 * Created by cwdsg05 on 18/11/16.
 */

import com.crowdo.p2pconnect.model.core.Loan;
import com.crowdo.p2pconnect.model.core.Server;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoanDetailResponse {

    @SerializedName("server")
    @Expose
    private Server server;

    @SerializedName("loan")
    @Expose
    private Loan loan;

    @SerializedName("first_repayment")
    @Expose
    private String firstRepayment;

    @SerializedName("last_repayment")
    @Expose
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
