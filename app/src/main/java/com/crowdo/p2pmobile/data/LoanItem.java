package com.crowdo.p2pmobile.data;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Generated("org.jsonschema2pojo")
public class LoanItem {

    @SerializedName("tenure_out")
    @Expose
    public int tenureOut;

    @SerializedName("collateral_out")
    @Expose
    public String collateralOut;

    @SerializedName("security")
    @Expose
    public String security;

    @SerializedName("target_amount_out")
    @Expose
    public double targetAmountOut;

    @SerializedName("grade")
    @Expose
    public String grade;

    @SerializedName("funding_duration")
    @Expose
    public int fundingDuration;

    @SerializedName("funding_start_date")
    @Expose
    public String fundingStartDate;

    @SerializedName("funding_end_date")
    @Expose
    public String fundingEndDate;

    @SerializedName("loan_id_out")
    @Expose
    public String loanIdOut;

    @SerializedName("funded_percentage_cache")
    @Expose
    public int fundedPercentageCache;

    @SerializedName("interest_rate_out")
    @Expose
    public double interestRateOut;

    @SerializedName("loan_status")
    @Expose
    public String loanStatus;

    @SerializedName("sort_weight")
    @Expose
    public int sortWeight;

    @SerializedName("frequency_out")
    @Expose
    public String frequencyOut;

    @SerializedName("currency_out")
    @Expose
    public String currencyOut;

    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("funding_amount_to_complete_cache")
    @Expose
    public double fundingAmountToCompleteCache;
}