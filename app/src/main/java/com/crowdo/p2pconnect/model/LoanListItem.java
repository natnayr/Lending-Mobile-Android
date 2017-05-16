package com.crowdo.p2pconnect.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoanListItem {

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
    private double totalInterest;

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
    private String frequency;

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
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(int partnerId) {
        this.partnerId = partnerId;
    }

    public int getPartnerPortfolioId() {
        return partnerPortfolioId;
    }

    public void setPartnerPortfolioId(int partnerPortfolioId) {
        this.partnerPortfolioId = partnerPortfolioId;
    }

    public int getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(int borrowerId) {
        this.borrowerId = borrowerId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getLoanId() {
        return loanId;
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getCollateral() {
        return collateral;
    }

    public void setCollateral(String collateral) {
        this.collateral = collateral;
    }

    public String getCollateralDescription() {
        return collateralDescription;
    }

    public void setCollateralDescription(String collateralDescription) {
        this.collateralDescription = collateralDescription;
    }

    public double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRateOut(double interestRate) {
        this.interestRate = interestRate;
    }

    public double getPmt() {
        return pmt;
    }

    public void setPmt(double pmtOut) {
        this.pmt = pmt;
    }

    public double getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterestOut(double totalInterestOut) {
        this.totalInterest = totalInterestOut;
    }

    public int getNumOfOnTimePayments() {
        return numOfOnTimePayments;
    }

    public void setNumOfOnTimePayments(int numOfOnTimePayments) {
        this.numOfOnTimePayments = numOfOnTimePayments;
    }

    public int getNumOfDelayedPayments() {
        return numOfDelayedPayments;
    }

    public void setNumOfDelayedPayments(int numOfDelayedPayments) {
        this.numOfDelayedPayments = numOfDelayedPayments;
    }

    public int getNumOfDefaultPayments() {
        return numOfDefaultPayments;
    }

    public void setNumOfDefaultPayments(int numOfDefaultPayments) {
        this.numOfDefaultPayments = numOfDefaultPayments;
    }

    public int getOriginalTenure() {
        return originalTenure;
    }

    public void setOriginalTenure(int originalTenure) {
        this.originalTenure = originalTenure;
    }

    public int getTenure() {
        return tenure;
    }

    public void setTenureOut(int tenure) {
        this.tenure = tenure;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getFundingDuration() {
        return fundingDuration;
    }

    public void setFundingDuration(int fundingDuration) {
        this.fundingDuration = fundingDuration;
    }

    public String getFundingStartDate() {
        return fundingStartDate;
    }

    public void setFundingStartDate(String fundingStartDate) {
        this.fundingStartDate = fundingStartDate;
    }

    public String getFundingEndDate() {
        return fundingEndDate;
    }

    public void setFundingEndDate(String fundingEndDate) {
        this.fundingEndDate = fundingEndDate;
    }

    public int getInvestmentsCount() {
        return investmentsCount;
    }

    public void setInvestmentsCount(int investmentsCount) {
        this.investmentsCount = investmentsCount;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
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

    public String getUnderlyingAgreement() {
        return underlyingAgreement;
    }

    public void setUnderlyingAgreement(String underlyingAgreement) {
        this.underlyingAgreement = underlyingAgreement;
    }

    public String getMasterAgreement() {
        return masterAgreement;
    }

    public void setMasterAgreement(String masterAgreement) {
        this.masterAgreement = masterAgreement;
    }

    public double getFundedAmountCache() {
        return fundedAmountCache;
    }

    public void setFundedAmountCache(double fundedAmountCache) {
        this.fundedAmountCache = fundedAmountCache;
    }

    public boolean isOnePercentPromotion() {
        return onePercentPromotion;
    }

    public void setOnePercentPromotion(boolean onePercentPromotion) {
        this.onePercentPromotion = onePercentPromotion;
    }

    public int getLoanToValue() {
        return loanToValue;
    }

    public void setLoanToValue(int loanToValue) {
        this.loanToValue = loanToValue;
    }

    public int getBorrowerAge() {
        return borrowerAge;
    }

    public void setBorrowerAge(int borrowerAge) {
        this.borrowerAge = borrowerAge;
    }

    public String getBorrowerGender() {
        return borrowerGender;
    }

    public void setBorrowerGender(String borrowerGender) {
        this.borrowerGender = borrowerGender;
    }

    public int getFundedPercentageCache() {
        return fundedPercentageCache;
    }

    public void setFundedPercentageCache(int fundedPercentageCache) {
        this.fundedPercentageCache = fundedPercentageCache;
    }

    public double getFundingAmountToCompleteCache() {
        return fundingAmountToCompleteCache;
    }

    public void setFundingAmountToCompleteCache(double fundingAmountToCompleteCache) {
        this.fundingAmountToCompleteCache = fundingAmountToCompleteCache;
    }

    public boolean isUnderlyingApproved() {
        return underlyingApproved;
    }

    public void setUnderlyingApproved(boolean underlyingApproved) {
        this.underlyingApproved = underlyingApproved;
    }

    public String getUnderlyingApprovedAt() {
        return underlyingApprovedAt;
    }

    public void setUnderlyingApprovedAt(String underlyingApprovedAt) {
        this.underlyingApprovedAt = underlyingApprovedAt;
    }

    public int getSortWeight() {
        return sortWeight;
    }

    public void setSortWeight(int sortWeight) {
        this.sortWeight = sortWeight;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public Object getLiquidationFlag() {
        return liquidationFlag;
    }

    public void setLiquidationFlag(String liquidationFlag) {
        this.liquidationFlag = liquidationFlag;
    }

    public Object getDisbursedProof() {
        return disbursedProof;
    }

    public void setDisbursedProof(String disbursedProof) {
        this.disbursedProof = disbursedProof;
    }
}