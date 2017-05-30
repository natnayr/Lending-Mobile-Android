package com.crowdo.p2pconnect.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cwdsg05 on 30/5/17.
 */

public class LoanResponse {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("partner_id")
    @Expose
    private int partnerId;

    @SerializedName("partner_portfolio_id")
    @Expose
    private int partnerPortfolioId;

    @SerializedName("borrower_id")
    @Expose
    private int borrowerId;

    @SerializedName("currency_out")
    @Expose
    private String currency;

    @SerializedName("loan_status")
    @Expose
    private String loanStatus;

    @SerializedName("customer_id")
    @Expose
    private String customerId;

    @SerializedName("loan_id_out")
    @Expose
    private String loanId;

    @SerializedName("loan_type")
    @Expose
    private String loanType;

    @SerializedName("collateral")
    @Expose
    private String collateral;

    @SerializedName("collateral_description")
    @Expose
    private String collateralDescription;

    @SerializedName("target_amount_out")
    @Expose
    private double targetAmount;

    @SerializedName("interest_rate_out")
    @Expose
    private double interestRate;

    @SerializedName("pmt_out")
    @Expose
    private double pmt;

    @SerializedName("total_interest_out")
    @Expose
    private double totalInterestOut;

    @SerializedName("num_of_on_time_payments")
    @Expose
    private int numOfOnTimePayments;

    @SerializedName("num_of_delayed_payments")
    @Expose
    private int numOfDelayedPayments;

    @SerializedName("num_of_default_payments")
    @Expose
    private int numOfDefaultPayments;

    @SerializedName("original_tenure")
    @Expose
    private int originalTenure;

    @SerializedName("tenure_out")
    @Expose
    private int tenure;

    @SerializedName("frequency_out")
    @Expose
    private String frequencyOut;

    @SerializedName("start_date_out")
    @Expose
    private String startDate;

    @SerializedName("funding_duration")
    @Expose
    private int fundingDuration;

    @SerializedName("funding_start_date")
    @Expose
    private String fundingStartDate;

    @SerializedName("funding_end_date")
    @Expose
    private String fundingEndDate;

    @SerializedName("investments_count")
    @Expose
    private int investmentsCount;

    @SerializedName("grade")
    @Expose
    private String grade;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("underlying_agreement")
    @Expose
    private String underlyingAgreement;

    @SerializedName("master_agreement")
    @Expose
    private String masterAgreement;

    @SerializedName("funded_amount_cache")
    @Expose
    private double fundedAmountCache;

    @SerializedName("one_percent_promotion")
    @Expose
    private boolean onePercentPromotion;

    @SerializedName("loan_to_value")
    @Expose
    private int loanToValue;

    @SerializedName("borrower_age")
    @Expose
    private int borrowerAge;

    @SerializedName("borrower_gender")
    @Expose
    private String borrowerGender;

    @SerializedName("funded_percentage_cache")
    @Expose
    private int fundedPercentageCache;

    @SerializedName("funding_amount_to_complete_cache")
    @Expose
    private double fundingAmountToCompleteCache;

    @SerializedName("underlying_approved")
    @Expose
    private boolean underlyingApproved;

    @SerializedName("underlying_approved_at")
    @Expose
    private String underlyingApprovedAt;

    @SerializedName("sort_weight")
    @Expose
    private int sortWeight;

    @SerializedName("reject_reason")
    @Expose
    private String rejectReason;

    @SerializedName("security")
    @Expose
    private String security;

    @SerializedName("liquidation_flag")
    @Expose
    private String liquidationFlag;

    @SerializedName("disbursed_proof")
    @Expose
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
