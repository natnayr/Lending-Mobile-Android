package com.crowdo.p2pconnect.model.core;

import com.squareup.moshi.Json;

/**
 * Created by cwdsg05 on 30/5/17.
 */

public class Loan {

    @Json(name = "id")
    private int id;

    @Json(name = "partner_id")
    private int partnerId;

    @Json(name = "partner_portfolio_id")
    private int partnerPortfolioId;

    @Json(name = "borrower_id")
    private int borrowerId;

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
    private double targetAmount;

    @Json(name = "interest_rate_out")
    private double interestRate;

    @Json(name = "pmt_out")
    private double pmt;

    @Json(name = "total_interest_out")
    private double totalInterestOut;

    @Json(name = "num_of_on_time_payments")
    private int numOfOnTimePayments;

    @Json(name = "num_of_delayed_payments")
    private int numOfDelayedPayments;

    @Json(name = "num_of_default_payments")
    private int numOfDefaultPayments;

    @Json(name = "original_tenure")
    private int originalTenure;

    @Json(name = "tenure_out")
    private int tenure;

    @Json(name = "frequency_out")
    private String frequencyOut;

    @Json(name = "start_date_out")
    private String startDate;

    @Json(name = "funding_duration")
    private int fundingDuration;

    @Json(name = "funding_start_date")
    private String fundingStartDate;

    @Json(name = "funding_end_date")
    private String fundingEndDate;

    @Json(name = "investments_count")
    private int investmentsCount;

    @Json(name = "grade")
    private String grade;

    @Json(name = "created_at")
    private String createdAt;

    @Json(name = "updated_at")
    private String updatedAt;

    @Json(name = "underlying_agreement")
    private String underlyingAgreement;

    @Json(name = "master_agreement")
    private String masterAgreement;

    @Json(name = "funded_amount_cache")
    private double fundedAmountCache;

    @Json(name = "one_percent_promotion")
    private boolean onePercentPromotion;

    @Json(name = "loan_to_value")
    private int loanToValue;

    @Json(name = "borrower_age")
    private int borrowerAge;

    @Json(name = "borrower_gender")
    private String borrowerGender;

    @Json(name = "funded_percentage_cache")
    private int fundedPercentageCache;

    @Json(name = "funding_amount_to_complete_cache")
    private double fundingAmountToCompleteCache;

    @Json(name = "underlying_approved")
    private boolean underlyingApproved;

    @Json(name = "underlying_approved_at")
    private String underlyingApprovedAt;

    @Json(name = "sort_weight")
    private int sortWeight;

    @Json(name = "reject_reason")
    private String rejectReason;

    @Json(name = "security")
    private String security;

    @Json(name = "liquidation_flag")
    private String liquidationFlag;

    @Json(name = "disbursed_proof")
    private String disbursedProof;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPartnerId() {
        return this.partnerId;
    }

    public void setPartnerId(int partnerId) {
        this.partnerId = partnerId;
    }

    public int getPartnerPortfolioId() {
        return this.partnerPortfolioId;
    }

    public void setPartnerPortfolioId(int partnerPortfolioId) {
        this.partnerPortfolioId = partnerPortfolioId;
    }

    public int getBorrowerId() {
        return this.borrowerId;
    }

    public void setBorrowerId(int borrowerId) {
        this.borrowerId = borrowerId;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrencyOut(String currency) {
        this.currency = currency;
    }

    public String getLoanStatus() {
        return this.loanStatus;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getLoanId() {
        return this.loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId.trim().toUpperCase();
    }

    public String getLoanType() {
        return this.loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getCollateral() {
        return this.collateral;
    }

    public void setCollateral(String collateral) {
        this.collateral = collateral;
    }

    public String getCollateralDescription() {
        return this.collateralDescription;
    }

    public void setCollateralDescription(String collateralDescription) {
        this.collateralDescription = collateralDescription;
    }

    public double getTargetAmount() {
        return this.targetAmount;
    }

    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public double getInterestRate() {
        return this.interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public double getPmt() {
        return this.pmt;
    }

    public void setPmt(double pmt) {
        this.pmt = pmt;
    }

    public double getTotalInterestOut() {
        return this.totalInterestOut;
    }

    public void setTotalInterestOut(double totalInterestOut) {
        this.totalInterestOut = totalInterestOut;
    }

    public int getNumOfOnTimePayments() {
        return this.numOfOnTimePayments;
    }

    public void setNumOfOnTimePayments(int numOfOnTimePayments) {
        this.numOfOnTimePayments = numOfOnTimePayments;
    }

    public int getNumOfDelayedPayments() {
        return this.numOfDelayedPayments;
    }

    public void setNumOfDelayedPayments(int numOfDelayedPayments) {
        this.numOfDelayedPayments = numOfDelayedPayments;
    }

    public int getNumOfDefaultPayments() {
        return this.numOfDefaultPayments;
    }

    public void setNumOfDefaultPayments(int numOfDefaultPayments) {
        this.numOfDefaultPayments = numOfDefaultPayments;
    }

    public int getOriginalTenure() {
        return this.originalTenure;
    }

    public void setOriginalTenure(int originalTenure) {
        this.originalTenure = originalTenure;
    }

    public int getTenure() {
        return this.tenure;
    }

    public void setTenure(int tenure) {
        this.tenure = tenure;
    }

    public String getFrequencyOut() {
        return this.frequencyOut;
    }

    public void setFrequencyOut(String frequencyOut) {
        this.frequencyOut = frequencyOut;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getFundingDuration() {
        return this.fundingDuration;
    }

    public void setFundingDuration(int fundingDuration) {
        this.fundingDuration = fundingDuration;
    }

    public String getFundingStartDate() {
        return this.fundingStartDate;
    }

    public void setFundingStartDate(String fundingStartDate) {
        this.fundingStartDate = fundingStartDate;
    }

    public String getFundingEndDate() {
        return this.fundingEndDate;
    }

    public void setFundingEndDate(String fundingEndDate) {
        this.fundingEndDate = fundingEndDate;
    }

    public int getInvestmentsCount() {
        return this.investmentsCount;
    }

    public void setInvestmentsCount(int investmentsCount) {
        this.investmentsCount = investmentsCount;
    }

    public String getGrade() {
        return this.grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUnderlyingAgreement() {
        return this.underlyingAgreement;
    }

    public void setUnderlyingAgreement(String underlyingAgreement) {
        this.underlyingAgreement = underlyingAgreement;
    }

    public String getMasterAgreement() {
        return this.masterAgreement;
    }

    public void setMasterAgreement(String masterAgreement) {
        this.masterAgreement = masterAgreement;
    }

    public double getFundedAmountCache() {
        return this.fundedAmountCache;
    }

    public void setFundedAmountCache(double fundedAmountCache) {
        this.fundedAmountCache = fundedAmountCache;
    }

    public boolean isOnePercentPromotion() {
        return this.onePercentPromotion;
    }

    public void setOnePercentPromotion(boolean onePercentPromotion) {
        this.onePercentPromotion = onePercentPromotion;
    }

    public int getLoanToValue() {
        return this.loanToValue;
    }

    public void setLoanToValue(int loanToValue) {
        this.loanToValue = loanToValue;
    }

    public int getBorrowerAge() {
        return this.borrowerAge;
    }

    public void setBorrowerAge(int borrowerAge) {
        this.borrowerAge = borrowerAge;
    }

    public String getBorrowerGender() {
        return this.borrowerGender;
    }

    public void setBorrowerGender(String borrowerGender) {
        this.borrowerGender = borrowerGender;
    }

    public int getFundedPercentageCache() {
        return this.fundedPercentageCache;
    }

    public void setFundedPercentageCache(int fundedPercentageCache) {
        this.fundedPercentageCache = fundedPercentageCache;
    }

    public double getFundingAmountToCompleteCache() {
        return this.fundingAmountToCompleteCache;
    }

    public void setFundingAmountToCompleteCache(double fundingAmountToCompleteCache) {
        this.fundingAmountToCompleteCache = fundingAmountToCompleteCache;
    }

    public boolean isUnderlyingApproved() {
        return this.underlyingApproved;
    }

    public void setUnderlyingApproved(boolean underlyingApproved) {
        this.underlyingApproved = underlyingApproved;
    }

    public String getUnderlyingApprovedAt() {
        return this.underlyingApprovedAt;
    }

    public void setUnderlyingApprovedAt(String underlyingApprovedAt) {
        this.underlyingApprovedAt = underlyingApprovedAt;
    }

    public int getSortWeight() {
        return this.sortWeight;
    }

    public void setSortWeight(int sortWeight) {
        this.sortWeight = sortWeight;
    }

    public String getRejectReason() {
        return this.rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getSecurity() {
        return this.security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public String getLiquidationFlag() {
        return this.liquidationFlag;
    }

    public void setLiquidationFlag(String liquidationFlag) {
        this.liquidationFlag = liquidationFlag;
    }

    public String getDisbursedProof() {
        return this.disbursedProof;
    }

    public void setDisbursedProof(String disbursedProof) {
        this.disbursedProof = disbursedProof;
    }
}
