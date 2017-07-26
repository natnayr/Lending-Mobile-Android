package com.crowdo.p2pconnect.model.others;

import com.squareup.moshi.Json;

/**
 * Created by cwdsg05 on 14/7/17.
 */

public class BankInfo {

    @Json(name = "account_name")
    public String accountName;

    @Json(name = "account_number")
    public String accountNumber;

    @Json(name = "account_type")
    public String accountType;

    @Json(name = "account_holder")
    public String accountHolder;

    @Json(name = "bank_name")
    public String bankName;

    @Json(name = "branch_name")
    public String branchName;

    @Json(name = "swift_code")
    public String swiftCode;

    @Json(name = "bank_address")
    public String bankAddress;

    @Json(name = "bank_country")
    public String bankCountry;

    @Json(name = "minimum_top_up")
    public long minimumTopUp;

    @Json(name = "minimum_investment")
    public long minimumInvestment;

    @Json(name = "unit_of_investment")
    public long unitOfInvestment;

    @Json(name = "unit_of_investment_desc")
    public String unitOfInvestmentDesc;

    @Json(name = "recommended_top_up")
    public long recommendedTopUp;
}
