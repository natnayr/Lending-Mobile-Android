package com.crowdo.p2pmobile.data;

import java.util.Date;

/**
 * Created by cwdsg05 on 9/11/16.
 */

public class LoanFromApi {

    long recordId;
    String status;
    String loadId;  //e.g. CWD-010123456
    String grade;
    double interestRate;
    long targetAmount;
    long amountToCompleteCache;
    int tenureNumMonths;
    String security; //e.g. Collateral, Working Order/Invoice
    String collateral;  //e.g. Jewelry
    String collateralDescription;
    int fundingDurationDays;
    Date fundingStartDate;
    Date fundingEndDate;
    double fundedPercentageCache;
    int sortWeight;



}
