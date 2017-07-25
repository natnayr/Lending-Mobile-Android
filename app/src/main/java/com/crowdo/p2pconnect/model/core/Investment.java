package com.crowdo.p2pconnect.model.core;

import com.squareup.moshi.Json;

/**
 * Created by cwdsg05 on 30/5/17.
 */

public class Investment {

    @Json(name = "id")
    private int id;

    @Json(name = "state")
    private String state;

    @Json(name = "loan_id")
    private int loanId;

    @Json(name = "invest_amount")
    private long investAmount;

    @Json(name = "created_at")
    private String createdAt;

    @Json(name = "updated_at")
    private String updatedAt;

    @Json(name = "participation_agreement")
    private String participationAgreement;

    @Json(name = "expected_principal_return")
    private String expectedPrincipalReturn;

    @Json(name = "expected_interest_return")
    private String expectedInterestReturn;

    @Json(name = "actual_principal_return")
    private String actualPrincipalReturn;

    @Json(name = "actual_interest_return")
    private String actualInterestReturn;

    @Json(name = "expected_amount")
    private String expectedAmount;

    @Json(name = "paid_amount")
    private String paidAmount;

    @Json(name = "unpaid_amount")
    private String unpaidAmount;

    @Json(name = "pending_bid_reminder_sent")
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
