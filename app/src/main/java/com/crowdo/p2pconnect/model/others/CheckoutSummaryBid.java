package com.crowdo.p2pconnect.model.others;

/**
 * Created by cwdsg05 on 25/5/17.
 */

public class CheckoutSummaryBid {

    private String loanId;
    private int tenureOut;
    private String grade;
    private double interestRateOut;
    private long investAmount;

    public CheckoutSummaryBid() {
    }

    public CheckoutSummaryBid(String loanId, int tenureOut, String grade, double interestRateOut, long investAmount) {
        this.loanId = loanId;
        this.tenureOut = tenureOut;
        this.grade = grade;
        this.interestRateOut = interestRateOut;
        this.investAmount = investAmount;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public int getTenureOut() {
        return tenureOut;
    }

    public void setTenureOut(int tenureOut) {
        this.tenureOut = tenureOut;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public double getInterestRateOut() {
        return interestRateOut;
    }

    public void setInterestRateOut(double interestRateOut) {
        this.interestRateOut = interestRateOut;
    }

    public long getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(long investAmount) {
        this.investAmount = investAmount;
    }
}
