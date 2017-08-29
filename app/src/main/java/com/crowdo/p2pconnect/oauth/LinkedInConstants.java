package com.crowdo.p2pconnect.oauth;

/**
 * Created by cwdsg05 on 28/8/17.
 */

public class LinkedInConstants {

    public static final String READ_BASICPROFILE = "r_basicprofile";
    public static final String READ_EMAILADDRESS = "r_emailaddress";
    public static final String READ_FULLPROFILE = "r_fullprofile";

    public static final String AUTHORIZATION_REQUEST_STATE = "2Wx3h6jS1f234Pd"; //random generated to prevent CSRF attacks;
    public static final String AUTHORIZATION_REQUEST_SCOPE = READ_BASICPROFILE + " " + READ_EMAILADDRESS;
    public static final String TEMPORARY_TOKEN_REQUEST_URL = "";
    public static final String AUTHORIZATION_CODE_SERVER_URL = "https://www.linkedin.com/oauth/v2/authorization?response_type=";

}
