package com.crowdo.p2pconnect.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cwdsg05 on 30/5/17.
 */

public class InvestmentResponse {

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
    private String investAmount;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("participation_agreement")
    @Expose
    private Object participationAgreement;

    @SerializedName("e_signature_ip_address")
    @Expose
    private Object eSignatureIpAddress;

    @SerializedName("e_signature_timestamp")
    @Expose
    private Object eSignatureTimestamp;

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

    @SerializedName("wallet_id")
    @Expose
    private int walletId;

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

    public String getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(String investAmount) {
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

    public Object getParticipationAgreement() {
        return participationAgreement;
    }

    public void setParticipationAgreement(Object participationAgreement) {
        this.participationAgreement = participationAgreement;
    }

    public Object getESignatureIpAddress() {
        return eSignatureIpAddress;
    }

    public void setESignatureIpAddress(Object eSignatureIpAddress) {
        this.eSignatureIpAddress = eSignatureIpAddress;
    }

    public Object getESignatureTimestamp() {
        return eSignatureTimestamp;
    }

    public void setESignatureTimestamp(Object eSignatureTimestamp) {
        this.eSignatureTimestamp = eSignatureTimestamp;
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

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }
}
