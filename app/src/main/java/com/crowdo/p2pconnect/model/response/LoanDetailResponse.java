package com.crowdo.p2pconnect.model.response;

/**
 * Created by cwdsg05 on 18/11/16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoanDetailResponse {

    @SerializedName("loan")
    @Expose
    private Loan loan;

    @SerializedName("first_repayment")
    @Expose
    private String firstRepayment;

    @SerializedName("last_repayment")
    @Expose
    private String lastRepayment;

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

    public int getId() {
        return loan.id;
    }

    public void setId(int id) {
        this.loan.id = id;
    }

    public int getPartnerId() {
        return loan.partnerId;
    }

    public void setPartnerId(int partnerId) {
        this.loan.partnerId = partnerId;
    }

    public int getPartnerPortfolioId() {
        return loan.partnerPortfolioId;
    }

    public void setPartnerPortfolioId(int partnerPortfolioId) {
        this.loan.partnerPortfolioId = partnerPortfolioId;
    }

    public int getBorrowerId() {
        return loan.borrowerId;
    }

    public void setBorrowerId(int borrowerId) {
        this.loan.borrowerId = borrowerId;
    }

    public String getCurrency() {
        return loan.currency;
    }

    public void setCurrencyOut(String currency) {
        this.loan.currency = currency;
    }

    public String getLoanStatus() {
        return loan.loanStatus;
    }

    public void setLoanStatus(String loanStatus) {
        this.loan.loanStatus = loanStatus;
    }

    public String getCustomerId() {
        return loan.customerId;
    }

    public void setCustomerId(String customerId) {
        this.loan.customerId = customerId;
    }

    public String getLoanId() {
        return loan.loanId;
    }

    public void setLoanId(String loanId) {
        this.loan.loanId = loanId.trim().toUpperCase();
    }

    public String getLoanType() {
        return loan.loanType;
    }

    public void setLoanType(String loanType) {
        this.loan.loanType = loanType;
    }

    public String getCollateral() {
        return loan.collateral;
    }

    public void setCollateral(String collateral) {
        this.loan.collateral = collateral;
    }

    public String getCollateralDescription() {
        return loan.collateralDescription;
    }

    public void setCollateralDescription(String collateralDescription) {
        this.loan.collateralDescription = collateralDescription;
    }

    public double getTargetAmount() {
        return loan.targetAmount;
    }

    public void setTargetAmount(double targetAmount) {
        this.loan.targetAmount = targetAmount;
    }

    public double getInterestRate() {
        return loan.interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.loan.interestRate = interestRate;
    }

    public double getPmt() {
        return loan.pmt;
    }

    public void setPmt(double pmt) {
        this.loan.pmt = pmt;
    }

    public double getTotalInterestOut() {
        return loan.totalInterestOut;
    }

    public void setTotalInterestOut(double totalInterestOut) {
        this.loan.totalInterestOut = totalInterestOut;
    }

    public int getNumOfOnTimePayments() {
        return loan.numOfOnTimePayments;
    }

    public void setNumOfOnTimePayments(int numOfOnTimePayments) {
        this.loan.numOfOnTimePayments = numOfOnTimePayments;
    }

    public int getNumOfDelayedPayments() {
        return loan.numOfDelayedPayments;
    }

    public void setNumOfDelayedPayments(int numOfDelayedPayments) {
        this.loan.numOfDelayedPayments = numOfDelayedPayments;
    }

    public int getNumOfDefaultPayments() {
        return loan.numOfDefaultPayments;
    }

    public void setNumOfDefaultPayments(int numOfDefaultPayments) {
        this.loan.numOfDefaultPayments = numOfDefaultPayments;
    }

    public int getOriginalTenure() {
        return loan.originalTenure;
    }

    public void setOriginalTenure(int originalTenure) {
        this.loan.originalTenure = originalTenure;
    }

    public int getTenure() {
        return loan.tenure;
    }

    public void setTenure(int tenure) {
        this.loan.tenure = tenure;
    }

    public String getFrequencyOut() {
        return loan.frequencyOut;
    }

    public void setFrequencyOut(String frequencyOut) {
        this.loan.frequencyOut = frequencyOut;
    }

    public String getStartDate() {
        return loan.startDate;
    }

    public void setStartDate(String startDate) {
        this.loan.startDate = startDate;
    }

    public int getFundingDuration() {
        return loan.fundingDuration;
    }

    public void setFundingDuration(int fundingDuration) {
        this.loan.fundingDuration = fundingDuration;
    }

    public String getFundingStartDate() {
        return loan.fundingStartDate;
    }

    public void setFundingStartDate(String fundingStartDate) {
        this.loan.fundingStartDate = fundingStartDate;
    }

    public String getFundingEndDate() {
        return loan.fundingEndDate;
    }

    public void setFundingEndDate(String fundingEndDate) {
        this.loan.fundingEndDate = fundingEndDate;
    }

    public int getInvestmentsCount() {
        return loan.investmentsCount;
    }

    public void setInvestmentsCount(int investmentsCount) {
        this.loan.investmentsCount = investmentsCount;
    }

    public String getGrade() {
        return loan.grade;
    }

    public void setGrade(String grade) {
        this.loan.grade = grade;
    }

    public String getCreatedAt() {
        return loan.createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.loan.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return loan.updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.loan.updatedAt = updatedAt;
    }

    public String getUnderlyingAgreement() {
        return loan.underlyingAgreement;
    }

    public void setUnderlyingAgreement(String underlyingAgreement) {
        this.loan.underlyingAgreement = underlyingAgreement;
    }

    public String getMasterAgreement() {
        return loan.masterAgreement;
    }

    public void setMasterAgreement(String masterAgreement) {
        this.loan.masterAgreement = masterAgreement;
    }

    public double getFundedAmountCache() {
        return loan.fundedAmountCache;
    }

    public void setFundedAmountCache(double fundedAmountCache) {
        this.loan.fundedAmountCache = fundedAmountCache;
    }

    public boolean isOnePercentPromotion() {
        return loan.onePercentPromotion;
    }

    public void setOnePercentPromotion(boolean onePercentPromotion) {
        this.loan.onePercentPromotion = onePercentPromotion;
    }

    public int getLoanToValue() {
        return loan.loanToValue;
    }

    public void setLoanToValue(int loanToValue) {
        this.loan.loanToValue = loanToValue;
    }

    public int getBorrowerAge() {
        return loan.borrowerAge;
    }

    public void setBorrowerAge(int borrowerAge) {
        this.loan.borrowerAge = borrowerAge;
    }

    public String getBorrowerGender() {
        return loan.borrowerGender;
    }

    public void setBorrowerGender(String borrowerGender) {
        this.loan.borrowerGender = borrowerGender;
    }

    public int getFundedPercentageCache() {
        return loan.fundedPercentageCache;
    }

    public void setFundedPercentageCache(int fundedPercentageCache) {
        this.loan.fundedPercentageCache = fundedPercentageCache;
    }

    public double getFundingAmountToCompleteCache() {
        return loan.fundingAmountToCompleteCache;
    }

    public void setFundingAmountToCompleteCache(double fundingAmountToCompleteCache) {
        this.loan.fundingAmountToCompleteCache = fundingAmountToCompleteCache;
    }

    public boolean isUnderlyingApproved() {
        return loan.underlyingApproved;
    }

    public void setUnderlyingApproved(boolean underlyingApproved) {
        this.loan.underlyingApproved = underlyingApproved;
    }

    public String getUnderlyingApprovedAt() {
        return loan.underlyingApprovedAt;
    }

    public void setUnderlyingApprovedAt(String underlyingApprovedAt) {
        this.loan.underlyingApprovedAt = underlyingApprovedAt;
    }

    public int getSortWeight() {
        return loan.sortWeight;
    }

    public void setSortWeight(int sortWeight) {
        this.loan.sortWeight = sortWeight;
    }

    public String getRejectReason() {
        return loan.rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.loan.rejectReason = rejectReason;
    }

    public String getSecurity() {
        return loan.security;
    }

    public void setSecurity(String security) {
        this.loan.security = security;
    }

    public String getLiquidationFlag() {
        return loan.liquidationFlag;
    }

    public void setLiquidationFlag(String liquidationFlag) {
        this.loan.liquidationFlag = liquidationFlag;
    }

    public String getDisbursedProof() {
        return loan.disbursedProof;
    }

    public void setDisbursedProof(String disbursedProof) {
        this.loan.disbursedProof = disbursedProof;
    }

    class Loan {
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
    }
}
