package com.crowdo.p2pconnect.model.core;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cwdsg05 on 30/5/17.
 */

public class Investment {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("loan_id")
    @Expose
    private int loanId;

    @SerializedName("invest_amount")
    @Expose
    private long investAmount;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("participation_agreement")
    @Expose
    private String participationAgreement;

    @SerializedName("expected_principal_return")
    @Expose
    private String expectedPrincipalReturn;

    @SerializedName("expected_interest_return")
    @Expose
    private String expectedInterestReturn;

    @SerializedName("actual_principal_return")
    @Expose
    private String actualPrincipalReturn;

    @SerializedName("actual_interest_return")
    @Expose
    private String actualInterestReturn;

    @SerializedName("expected_amount")
    @Expose
    private String expectedAmount;

    @SerializedName("paid_amount")
    @Expose
    private String paidAmount;

    @SerializedName("unpaid_amount")
    @Expose
    private String unpaidAmount;

    @SerializedName("pending_bid_reminder_sent")
    @Expose
    private boolean pendingBidReminderSent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public long getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(long investAmount) {
        this.investAmount = investAmount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getParticipationAgreement() {
        return participationAgreement;
    }

    public void setParticipationAgreement(String participationAgreement) {
        this.participationAgreement = participationAgreement;
    }

    public String getExpectedPrincipalReturn() {
        return expectedPrincipalReturn;
    }

    public void setExpectedPrincipalReturn(String expectedPrincipalReturn) {
        this.expectedPrincipalReturn = expectedPrincipalReturn;
    }

    public String getExpectedInterestReturn() {
        return expectedInterestReturn;
    }

    public void setExpectedInterestReturn(String expectedInterestReturn) {
        this.expectedInterestReturn = expectedInterestReturn;
    }

    public String getActualPrincipalReturn() {
        return actualPrincipalReturn;
    }

    public void setActualPrincipalReturn(String actualPrincipalReturn) {
        this.actualPrincipalReturn = actualPrincipalReturn;
    }

    public String getActualInterestReturn() {
        return actualInterestReturn;
    }

    public void setActualInterestReturn(String actualInterestReturn) {
        this.actualInterestReturn = actualInterestReturn;
    }

    public String getExpectedAmount() {
        return expectedAmount;
    }

    public void setExpectedAmount(String expectedAmount) {
        this.expectedAmount = expectedAmount;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(String paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getUnpaidAmount() {
        return unpaidAmount;
    }

    public void setUnpaidAmount(String unpaidAmount) {
        this.unpaidAmount = unpaidAmount;
    }

    public boolean isPendingBidReminderSent() {
        return pendingBidReminderSent;
    }

    public void setPendingBidReminderSent(boolean pendingBidReminderSent) {
        this.pendingBidReminderSent = pendingBidReminderSent;
    }
}
