package com.crowdo.p2pconnect.model.core;

import com.squareup.moshi.Json;

/**
 * Created by cwdsg05 on 30/5/17.
 */

public class Investment {

    @Json(name = "id")
    private Integer id;

    @Json(name = "state")
    private String state;

    @Json(name = "loan_id")
    private Integer loanId;

    @Json(name = "invest_amount")
    private Long investAmount;

    @Json(name = "created_at")
    private String createdAt;

    @Json(name = "updated_at")
    private String updatedAt;

    @Json(name = "participation_agreement")
    private URLAttachment participationAgreement;

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
    private Boolean pendingBidReminderSent;

    public int getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public int getLoanId() {
        return loanId;
    }

    public long getInvestAmount() {
        return investAmount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public URLAttachment getParticipationAgreement() {
        return participationAgreement;
    }

    public String getExpectedPrincipalReturn() {
        return expectedPrincipalReturn;
    }

    public String getExpectedInterestReturn() {
        return expectedInterestReturn;
    }

    public String getActualPrincipalReturn() {
        return actualPrincipalReturn;
    }

    public String getActualInterestReturn() {
        return actualInterestReturn;
    }

    public String getExpectedAmount() {
        return expectedAmount;
    }

    public String getPaidAmount() {
        return paidAmount;
    }

    public String getUnpaidAmount() {
        return unpaidAmount;
    }

    public boolean isPendingBidReminderSent() {
        return pendingBidReminderSent;
    }

    private static class URLAttachment {
        @Json(name = "url")
        private String url;

        public String getUrl() {
            return url;
        }

    }
}
