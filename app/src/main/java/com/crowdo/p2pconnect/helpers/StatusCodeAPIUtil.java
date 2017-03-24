package com.crowdo.p2pconnect.helpers;

/**
 * Created by cwdsg05 on 24/3/17.
 */

public class StatusCodeAPIUtil {

    public static final int HTTP_STATUS_CODE_200_SUCCESS = 200;
    public static final int HTTP_STATUS_CODE_401_SESSION_ERROR = 401;

    public static boolean check2xxSuccess(int statusCode){
        //2xx success
        if(statusCode >= 200 && statusCode < 300){
            return true;
        }
        return false;
    }

    public static boolean check4xxClientError(int statusCode){
        //4xx Client Error
        if(statusCode >= 400 && statusCode < 500){
            return true;
        }
        return false;
    }

    public static boolean check5xxServerError(int statusCode){
        //5xx ServerError codes
        if(statusCode >= 500 && statusCode < 600){
            return true;
        }
        return false;
    }

}
