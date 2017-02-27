package com.crowdo.p2pconnect.model;

/**
 * Created by cwdsg05 on 18/11/16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoanDetail {

    @SerializedName("loan_id")
    @Expose
    public String loanId;

    @SerializedName("collateral_description")
    @Expose
    public String collateralDescription;

    @SerializedName("grade")
    @Expose
    public String grade;

    @SerializedName("last_repayment")
    @Expose
    public String lastRepayment;

    @SerializedName("currency")
    @Expose
    public String currency;

    @SerializedName("frequency")
    @Expose
    public String frequency;

    @SerializedName("funded_percentage_cache")
    @Expose
    public int fundedPercentageCache;

    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("loan_type")
    @Expose
    public String loanType;

    @SerializedName("interest_rate")
    @Expose
    public double interestRate;

    @SerializedName("funding_start_date")
    @Expose
    public String fundingStartDate;

    @SerializedName("target_amount")
    @Expose
    public double targetAmount;

    @SerializedName("start_date")
    @Expose
    public String startDate;

    @SerializedName("collateral")
    @Expose
    public String collateral;

    @SerializedName("sort_weight")
    @Expose
    public int sortWeight;

    @SerializedName("first_repayment")
    @Expose
    public String firstRepayment;

    @SerializedName("funding_duration")
    @Expose
    public int fundingDuration;

    @SerializedName("funding_end_date")
    @Expose
    public String fundingEndDate;

    @SerializedName("tenure")
    @Expose
    public int tenure;

    @SerializedName("loan_status")
    @Expose
    public String loanStatus;

    @SerializedName("security")
    @Expose
    public String security;

    @SerializedName("funding_amount_to_complete_cache")
    @Expose
    public double fundingAvalibleAmount;


}
