package com.crowdo.p2pconnect.model.core;

import com.serjltt.moshi.adapters.FallbackOnNull;
import com.squareup.moshi.Json;

/**
 * Created by cwdsg05 on 30/5/17.
 */

public class Loan {

    @Json(name = "id")
    private Integer id;

    @Json(name = "partner_id")
    private Integer partnerId;

    @Json(name = "partner_portfolio_id")
    private Integer partnerPortfolioId;

    @Json(name = "borrower_id")
    private Integer borrowerId;

    @Json(name = "currency_out")
    private String currency;

    @Json(name = "loan_status")
    private String loanStatus;

    @Json(name = "customer_id")
    private String customerId;

    @Json(name = "loan_id_out")
    private String loanId;

    @Json(name = "loan_type")
    private String loanType;

    @Json(name = "collateral")
    private String collateral;

    @Json(name = "collateral_description")
    private String collateralDescription;

    @Json(name = "target_amount_out")
    private Double targetAmount;

    @Json(name = "interest_rate_out")
    private Double interestRate;

    @Json(name = "pmt_out")
    private Double pmt;

    @Json(name = "total_interest_out")
    private Double totalInterestOut;

    @Json(name = "num_of_on_time_payments")
    private Integer numOfOnTimePayments;

    @Json(name = "num_of_delayed_payments")
    private Integer numOfDelayedPayments;

    @Json(name = "num_of_default_payments")
    private Integer numOfDefaultPayments;

    @Json(name = "original_tenure")
    private Integer originalTenure;

    @Json(name = "tenure_out")
    private Integer tenure;

    @Json(name = "frequency_out")
    private String frequencyOut;

    @Json(name = "start_date_out")
    private String startDate;

    @Json(name = "funding_duration")
    private Integer fundingDuration;

    @Json(name = "funding_start_date")
    private String fundingStartDate;

    @Json(name = "funding_end_date")
    private String fundingEndDate;

    @Json(name = "investments_count")
    private Integer investmentsCount;

    @Json(name = "grade")
    private String grade;

    @Json(name = "created_at")
    private String createdAt;

    @Json(name = "updated_at")
    private String updatedAt;

    @Json(name = "underlying_agreement")
    private URLAttachment underlyingAgreement;

    @Json(name = "master_agreement")
    private URLAttachment masterAgreement;

    @Json(name = "funded_amount_cache")
    private Double fundedAmountCache;

    @Json(name = "one_percent_promotion")
    private Boolean onePercentPromotion;

    @Json(name = "loan_to_value")
    private Integer loanToValue;

    @Json(name = "borrower_age")
    private Integer borrowerAge;

    @Json(name = "borrower_gender")
    private String borrowerGender;

    @Json(name = "funded_percentage_cache")
    private Integer fundedPercentageCache;

    @Json(name = "funding_amount_to_complete_cache")
    private Double fundingAmountToCompleteCache;

    @Json(name = "underlying_approved")
    private Boolean underlyingApproved;

    @Json(name = "underlying_approved_at")
    private String underlyingApprovedAt;

    @Json(name = "sort_weight")
    private Integer sortWeight;

    @Json(name = "reject_reason")
    private String rejectReason;

    @Json(name = "security")
    private String security;

    @Json(name = "liquidation_flag")
    private String liquidationFlag;

    @Json(name = "disbursed_proof")
    private URLAttachment disbursedProof;

    public Integer getId() {
        return this.id;
    }

    public Integer getPartnerId() {
        return this.partnerId;
    }

    public Integer getPartnerPortfolioId() {
        return this.partnerPortfolioId;
    }

    public Integer getBorrowerId() {
        return this.borrowerId;
    }

    public String getCurrency() {
        return this.currency;
    }

    public String getLoanStatus() {
        return this.loanStatus;
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public String getLoanId() {
        return this.loanId;
    }

    public String getLoanType() {
        return this.loanType;
    }

    public String getCollateral() {
        return this.collateral;
    }

    public String getCollateralDescription() {
        return this.collateralDescription;
    }

    public double getTargetAmount() {
        return this.targetAmount;
    }

    public double getInterestRate() {
        return this.interestRate;
    }

    public double getPmt() {
        return this.pmt;
    }

    public double getTotalInterestOut() {
        return this.totalInterestOut;
    }

    public Integer getNumOfOnTimePayments() {
        return this.numOfOnTimePayments;
    }

    public Integer getNumOfDelayedPayments() {
        return this.numOfDelayedPayments;
    }

    public Integer getNumOfDefaultPayments() {
        return this.numOfDefaultPayments;
    }

    public Integer getOriginalTenure() {
        return this.originalTenure;
    }

    public Integer getTenure() {
        return this.tenure;
    }

    public String getFrequencyOut() {
        return this.frequencyOut;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public Integer getFundingDuration() {
        return this.fundingDuration;
    }

    public String getFundingStartDate() {
        return this.fundingStartDate;
    }

    public String getFundingEndDate() {
        return this.fundingEndDate;
    }

    public Integer getInvestmentsCount() {
        return this.investmentsCount;
    }

    public String getGrade() {
        return this.grade;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public String getUpdatedAt() {
        return this.updatedAt;
    }

    public URLAttachment getUnderlyingAgreement() {
        return this.underlyingAgreement;
    }

    public URLAttachment getMasterAgreement() {
        return this.masterAgreement;
    }

    public double getFundedAmountCache() {
        return this.fundedAmountCache;
    }

    public boolean isOnePercentPromotion() {
        return this.onePercentPromotion;
    }

    public Integer getLoanToValue() {
        return this.loanToValue;
    }

    public Integer getBorrowerAge() {
        return this.borrowerAge;
    }

    public String getBorrowerGender() {
        return this.borrowerGender;
    }

    public Integer getFundedPercentageCache() {
        return this.fundedPercentageCache;
    }

    public double getFundingAmountToCompleteCache() {
        return this.fundingAmountToCompleteCache;
    }

    public boolean isUnderlyingApproved() {
        return this.underlyingApproved;
    }

    public String getUnderlyingApprovedAt() {
        return this.underlyingApprovedAt;
    }

    public Integer getSortWeight() {
        return this.sortWeight;
    }

    public String getRejectReason() {
        return this.rejectReason;
    }

    public String getSecurity() {
        return this.security;
    }

    public String getLiquidationFlag() {
        return this.liquidationFlag;
    }

    public URLAttachment getDisbursedProof() {
        return this.disbursedProof;
    }

    private static class URLAttachment {

        @Json(name = "url")
        private String url;

        public String getUrl() {
            return url;
        }

    }
}
