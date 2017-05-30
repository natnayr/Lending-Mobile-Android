package com.crowdo.p2pconnect.model.response;

/**
 * Created by cwdsg05 on 18/11/16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoanDetailResponse {

    @SerializedName("loan")
    @Expose
    private LoanResponse loan;

    @SerializedName("first_repayment")
    @Expose
    private String firstRepayment;

    @SerializedName("last_repayment")
    @Expose
    private String lastRepayment;

    public LoanResponse getLoanResponse(){
        return loan;
    }

    public void setLoanResponse(LoanResponse loanResponse){
        this.loan = loanResponse;
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
