package com.crowdo.p2pmobile.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoanListItem {

    @SerializedName("loan_id")
    @Expose
    public String loanId;

    @SerializedName("funding_start_date")
    @Expose
    public String fundingStartDate;

    @SerializedName("sort_weight")
    @Expose
    public int sortWeight;

    @SerializedName("collateral")
    @Expose
    public String collateral;

    @SerializedName("grade")
    @Expose
    public String grade;

    @SerializedName("interest_rate")
    @Expose
    public double interestRate;

    @SerializedName("tenure")
    @Expose
    public int tenure;

    @SerializedName("funding_end_date")
    @Expose
    public String fundingEndDate;

    @SerializedName("funding_duration")
    @Expose
    public int fundingDuration;

    @SerializedName("currency")
    @Expose
    public String currency;

    @SerializedName("frequency")
    @Expose
    public String frequency;

    @SerializedName("funded_percentage_cache")
    @Expose
    public int fundedPercentageCache;

    @SerializedName("target_amount")
    @Expose
    public double targetAmount;

    @SerializedName("loan_status")
    @Expose
    public String loanStatus;

    @SerializedName("security")
    @Expose
    public String security;

    @SerializedName("id")
    @Expose
    public int id;
}