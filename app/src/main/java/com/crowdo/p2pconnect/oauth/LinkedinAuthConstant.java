package com.crowdo.p2pconnect.oauth;

/**
 * Created by cwdsg05 on 8/9/17.
 */

public class LinkedinAuthConstant {
    public static final String READ_BASICPROFILE = "r_basicprofile";
    public static final String READ_EMAILADDRESS = "r_emailaddress";

    public static final String AUTHORIZATION_REQUEST_STATE = "code"; //implementing device ID
    public static final String AUTHORIZATION_REQUEST_SCOPE = READ_BASICPROFILE + " " + READ_EMAILADDRESS;
    public static final String TEMPORARY_TOKEN_REQUEST_URL = "";

}
