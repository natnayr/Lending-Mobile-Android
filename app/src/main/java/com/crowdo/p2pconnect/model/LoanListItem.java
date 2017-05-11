package com.crowdo.p2pconnect.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoanListItem {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("loan_status")
    @Expose
    private String loanStatus;

    @SerializedName("collateral")
    @Expose
    private String collateral;

    @SerializedName("funding_duration")
    @Expose
    private int fundingDuration;

    @SerializedName("funding_start_date")
    @Expose
    private String fundingStartDate;

    @SerializedName("funding_end_date")
    @Expose
    private String fundingEndDate;

    @SerializedName("grade")
    @Expose
    private String grade;

    @SerializedName("funded_percentage_cache")
    @Expose
    private int fundedPercentageCache;

    @SerializedName("sort_weight")
    @Expose
    private int sortWeight;

    @SerializedName("security")
    @Expose
    private String security;

    @SerializedName("loan_id_out")
    @Expose
    private String loanId;

    @SerializedName("currency_out")
    @Expose
    private String currency;

    @SerializedName("interest_rate_out")
    @Expose
    private String interestRate;

    @SerializedName("target_amount_out")
    @Expose
    private String targetAmount;

    @SerializedName("tenure_out")
    @Expose
    private int tenure;

    @SerializedName("frequency_out")
    @Expose
    private String frequency;

    @SerializedName("master_agreement")
    @Expose
    private String masterAgreement;

    @SerializedName("underlying_agreement")
    @Expose
    private String underlyingAgreement;

    @SerializedName("disbursed_proof")
    @Expose
    private String disbursedProof;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLoanStatus() {
        return loanStatus.trim();
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }

    public String getCollateral() {
        return collateral.replaceAll("_", " ").trim();
    }

    public void setCollateral(String collateral) {
        this.collateral = collateral.replaceAll("_", " ").trim();
    }

    public int getFundingDuration() {
        return fundingDuration;
    }

    public void setFundingDuration(Integer fundingDuration) {
        this.fundingDuration = fundingDuration;
    }

    public String getFundingStartDate() {
        return fundingStartDate.trim();
    }

    public void setFundingStartDate(String fundingStartDate) {
        this.fundingStartDate = fundingStartDate.trim();
    }

    public String getFundingEndDate() {
        return fundingEndDate;
    }

    public void setFundingEndDate(String fundingEndDate) {
        this.fundingEndDate = fundingEndDate.trim();
    }

    public String getGrade() {
        return grade.trim().toUpperCase();
    }

    public void setGrade(String grade) {
        this.grade = grade.trim().toUpperCase();
    }

    public int getFundedPercentageCache() {
        return fundedPercentageCache;
    }

    public void setFundedPercentageCache(Integer fundedPercentageCache) {
        this.fundedPercentageCache = fundedPercentageCache;
    }

    public int getSortWeight() {
        return sortWeight;
    }

    public void setSortWeight(Integer sortWeight) {
        this.sortWeight = sortWeight;
    }

    public String getSecurity() {
        return security.trim();
    }

    public void setSecurity(String security) {
        this.security = security.trim();
    }

    public String getLoanId() {
        return loanId.trim().toUpperCase();
    }

    public void setLoanId(String loanId) {
        this.loanId = loanId.trim().toUpperCase();
    }

    public String getCurrency() {
        return currency.trim().toUpperCase();
    }

    public void setCurrency(String currency) {
        this.currency = currency.trim().toUpperCase();
    }

    public double getInterestRate() {
        return Double.parseDouble(interestRate);
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate.trim();
    }

    public double getTargetAmount() {
        return Double.parseDouble(targetAmount.trim());
    }

    public void setTargetAmount(String targetAmount) {
        this.targetAmount = targetAmount.trim();
    }

    public int getTenure() {
        return tenure;
    }

    public void setTenure(Integer tenure) {
        this.tenure = tenure;
    }

    public String getFrequency() {
        return frequency.trim();
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency.trim();
    }

    public String getMasterAgreement() {
        return masterAgreement;
    }

    public void setMasterAgreement(String masterAgreement) {
        this.masterAgreement = masterAgreement;
    }

    public String getUnderlyingAgreement() {
        return underlyingAgreement;
    }

    public void setUnderlyingAgreement(String underlyingAgreement) {
        this.underlyingAgreement = underlyingAgreement;
    }

    public String getDisbursedProof() {
        return disbursedProof;
    }

    public void setDisbursedProof(String disbursedProof) {
        this.disbursedProof = disbursedProof;
    }
}