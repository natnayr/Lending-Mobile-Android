package com.crowdo.p2pconnect.helpers;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by cwdsg05 on 12/12/16.
 */

public class DateUtils {

    public static final String SQL_DATE_ONLY_PATTERN = "yyyy-MM-dd";
    public static final String SQL_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";

    public static int findDaysLeft(String DATE_TIME_REGION, String fundingEndDate){
        DateTimeFormatter dtf = DateTimeFormat.forPattern(SQL_DATE_ONLY_PATTERN); //e.g 2016-11-18
        DateTime sgNow = new DateTime(DateTimeZone.forID(DATE_TIME_REGION)); // set SG time
        DateTime endDate = dtf.parseDateTime(fundingEndDate);
        return Days.daysBetween(sgNow.toLocalDate(), endDate.toLocalDate()).getDays();
    }

    public static String dateOnlyFormatter(String outPattern, String date){
        DateTimeFormatter dtf = DateTimeFormat.forPattern(SQL_DATE_ONLY_PATTERN);
        return dtf.parseDateTime(date).toString(outPattern);
    }

    public static String dateTimeFormatter(String outPattern, String date){
        DateTimeFormatter dtf = DateTimeFormat.forPattern(SQL_DATE_TIME_PATTERN);
        return dtf.parseDateTime(date).toString(outPattern);
    }

}
