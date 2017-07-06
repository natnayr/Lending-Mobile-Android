package com.crowdo.p2pconnect.helpers;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

/**
 * Created by cwdsg05 on 26/1/17.
 */

public class ConstantVariables {

    public static final long REALM_DB_VERSION = 2;
    public static final long IDR_BASE_UNIT = 1000000;

    public static final int REQUEST_CODE_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 12312;
    public static final int REQUEST_CODE_PERMISSIONS_OVERLAY = 10101;
    public static final int REQUEST_CODE_AUTHENTICATION = 20202;

    public static final String APP_LANG_DEFAULT = "en";
    public static final String APP_LANG_EN = "en";
    public static final String APP_LANG_ID = "id";
    public static final String PDF_CONTENT_TYPE = "application/pdf";

    public static final String OUT_DATE_TIME_FORMAT = "dd MMM yyyy";
    public static final String DATE_TIME_REGION = "Asia/Singapore";

    public static final String API_SITE_CONFIG_SG = "sg";
    public static final String API_SITE_CONFIG_ID = "id";

    public static final String CURRENCY_IDR = "IDR";
    public static final String CURRENCY_SGD = "SGD";

    public static String getUniqueAndroidID(Context context){
        return Build.SERIAL + Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public static final String PREF_KEY_LOADED_LEARNINGCENTER_DB = "LOADED_LEARNINGCENTER_DB_KEY";
    public static final String PREF_KEY_SELECTED_LANGUAGE = "LOCALE_HELPER_SELECTED_LANGUGAGE";
    public static final String PREF_KEY_COOKIES = "C_IS_FOR_COOKIE_KEY";

    public static final String LEARNING_CENTER_CSV_FILE_LOCATION = "FAQ_template_2017.csv";
    public static final String LEARNING_CENTER_DB_CATEGORY_KEY_GENERAL = "General";
    public static final String LEARNING_CENTER_DB_CATEGORY_KEY_INVESTOR = "Investor";
    public static final String LEARNING_CENTER_DB_CATEGORY_KEY_BORROWER = "Borrower";

    public static final String IN_SEC_COLLATERALIZED = "Collateral";
    public static final String IN_SEC_UNCOLLATERALIZED = "Uncollateralized";
    public static final String IN_SEC_INVOICE_OR_CHEQUE = "Working Order/Invoice";
    public static final String IN_SEC_PERSONAL_GUARANTEE = "Personal Guarantee";

    public static final int IN_TERM_3 = 3;
    public static final int IN_TERM_4 = 4;
    public static final int IN_TERM_5 = 5;
    public static final int IN_TERM_6 = 6;
    public static final int IN_TERM_9 = 9;

    public static final String EN_MILLIONS_TRUNCATE = "M";
    public static final String EN_BILLIONS_TRUNCATE = "B";
    public static final String ID_MILLIONS_TRUNCATE = "JT";
    public static final String ID_BILLIONS_TRUNCATE = "M";

    public static final String PARTICIPATION_AGREEMENT_URL = "https://crowdo.co.id/documents/participation_agreement.pdf";
    public static final String PARTICIPATION_AGREEMENT_FILENAME = "participation_agreement.pdf";

    public static final int HTTP_SUCCESS_OK = 200;
    public static final int HTTP_BAD_REQUEST = 400;
    public static final int HTTP_UNAUTHORISED = 401;
    public static final int HTTP_NOT_FOUND = 404;
    public static final int HTTP_PRECONDITION_FAILED = 412;
    public static final int HTTP_INVESTOR_FAILED_ACCREDITATION = 418;
    public static final int HTTP_UNPROCESSABLE_ENTITY = 422;
}
