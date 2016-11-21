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

    @SerializedName("target_amount_out")
    @Expose
    public long targetAmountOut;

    @SerializedName("grade")
    @Expose
    public String grade;

    @SerializedName("funding_duration")
    @Expose
    public int fundingDuration;

    @SerializedName("funding_start_date")
    @Expose
    public Date fundingStartDate;

    @SerializedName("funding_end_date")
    @Expose
    public Date fundingEndDate;

    @SerializedName("loan_id_out")
    @Expose
    public String loanIdOut;

    @SerializedName("funded_percentage_cache")
    @Expose
    public double fundedPercentageCache;

    @SerializedName("interest_rate_out")
    @Expose
    public double interestRateOut;

    @SerializedName("loan_status")
    @Expose
    public String loanStatus;

    @SerializedName("sort_weight")
    @Expose
    public long sortWeight;

    @SerializedName("security")
    @Expose
    public String security;

    @SerializedName("collateral_description_out")
    @Expose
    public String collateralDescriptionOut;

    @SerializedName("id")
    @Expose
    public long id;

    @SerializedName("funding_amount_to_complete_cache")
    @Expose
    public long fundingAmountToCompleteCache;

}