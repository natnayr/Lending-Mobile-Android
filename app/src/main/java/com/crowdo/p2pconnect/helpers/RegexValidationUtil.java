package com.crowdo.p2pconnect.helpers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cwdsg05 on 21/3/17.
 */

public class RegexValidationUtil {

    public static boolean isValidEmailID(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
}
