package com.crowdo.p2pconnect.oauth;

/**
 * Created by cwdsg05 on 8/9/17.
 */

public class SocialAuthConstant {

    public static final String AUTH_FACEBOOK_PROVIDER_VALUE = "facebook";
    public static final String[] AUTH_FACEBOOK_READ_PERMISSIONS = new String[]{"email", "user_work_history", "user_location", "user_education_history"};

    public static final String AUTH_LINKEDIN_REDIRECT_URL = "http://192.168.1.5:3000/api/v2/oauth/linkedin/callback";
    public static final String AUTH_LINKEDIN_CLIENT_ID = "75wudfk13y91r8";
    public static final String AUTH_LINKEDIN_CLIENT_SECRET = "KGwSaojd66cc80wO";

    public static final String AUTH_LINKEDIN_READ_BASICPROFILE = "r_basicprofile";
    public static final String AUTH_LINKEDIN_READ_EMAILADDRESS = "r_emailaddress";
    public static final String AUTHORIZATION_REQUEST_SCOPE = AUTH_LINKEDIN_READ_BASICPROFILE + " " + AUTH_LINKEDIN_READ_EMAILADDRESS;

    public static final String AUTH_LINKEDIN_RESULT_NAME_EXTRA = "EXTRA_LINKEDIN_RESULT_NAME";
    public static final String AUTH_LINKEDIN_RESULT_TOKEN_EXTRA = "EXTRA_LINKEDIN_RESULT_TOKEN";
    public static final String AUTH_LINKEDIN_RESULT_EMAIL_EXTRA = "EXTRA_LINKEDIN_RESULT_EMAIL";
    public static final String AUTH_LINKEDIN_RESULT_LOCALE_EXTRA = "EXTRA_LINKEDIN_RESULT_LOCALE";
    public static final String AUTH_LINKEDIN_RESULT_FAILURE_EXTRA = "EXTRA_LINKEDIN_RESULT_FAILURE";
}
