package com.crowdo.p2pmobile.data;

/**
 * Created by cwdsg05 on 18/11/16.
 */


import javax.annotation.Generated;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class LoanDetail {

    @SerializedName("sort_weight")
    @Expose
    public int sortWeight;

    @SerializedName("tenure_out")
    @Expose
    public int tenureOut;

    @SerializedName("collateral_out")
    @Expose
    public String collateralOut;

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

    @SerializedName("start_date_out")
    @Expose
    public String startDateOut;

    @SerializedName("funded_percentage_cache")
    @Expose
    public int fundedPercentageCache;

    @SerializedName("interest_rate_out")
    @Expose
    public double interestRateOut;

    @SerializedName("loan_status")
    @Expose
    public String loanStatus;

    @SerializedName("loan_type")
    @Expose
    public String loanType;

    @SerializedName("security")
    @Expose
    public String security;

    @SerializedName("collateral_description_out")
    @Expose
    public String collateralDescriptionOut;

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
