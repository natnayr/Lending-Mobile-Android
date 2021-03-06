package com.crowdo.p2pconnect.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cwdsg05 on 21/3/17.
 */

public class RegexValidationUtil {

    public static boolean isValidEmailFormat(String email) {
        boolean isValid = false;

        String expression = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static boolean isValidPasswordLength(String password){
        boolean isValid = false;

        String expression = "(.{8,})";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(password);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
}
