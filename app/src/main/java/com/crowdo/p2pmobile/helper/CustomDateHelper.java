package com.crowdo.p2pmobile.helper;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;

/**
 * Created by cwdsg05 on 12/12/16.
 */

public class CustomDateHelper {

    public static int findDaysLeft(String DATE_TIME_REGION, String fundingEndDate){
        DateTime sgNow = new DateTime(DateTimeZone.forID(DATE_TIME_REGION)); // set SG time
        DateTime endDate = new DateTime(fundingEndDate);
        return Days.daysBetween(sgNow.toLocalDate(), endDate.toLocalDate()).getDays();
    }
}
