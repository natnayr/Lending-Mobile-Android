package com.crowdo.p2pmobile.helper;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by cwdsg05 on 12/12/16.
 */

public class DateUtils {

    public static final String SQL_DATE_PATTERN = "yyyy-MM-dd";

    public static int findDaysLeft(String DATE_TIME_REGION, String fundingEndDate){
        DateTimeFormatter dtf = DateTimeFormat.forPattern(SQL_DATE_PATTERN); //e.g 2016-11-18
        DateTime sgNow = new DateTime(DateTimeZone.forID(DATE_TIME_REGION)); // set SG time
        DateTime endDate = dtf.parseDateTime(fundingEndDate);
        return Days.daysBetween(sgNow.toLocalDate(), endDate.toLocalDate()).getDays();
    }

    public static String dateTimeFormatter(String outPattern, String date){
        DateTimeFormatter dtf = DateTimeFormat.forPattern(SQL_DATE_PATTERN);
        return dtf.parseDateTime(date).toString(outPattern);
    }


}
