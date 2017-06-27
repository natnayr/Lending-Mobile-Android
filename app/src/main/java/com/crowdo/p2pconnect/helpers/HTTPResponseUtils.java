package com.crowdo.p2pconnect.helpers;

import android.support.design.widget.Snackbar;
import android.util.Log;

import com.crowdo.p2pconnect.data.client.ClientInterface;
import com.crowdo.p2pconnect.model.response.MessageResponse;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by cwdsg05 on 24/3/17.
 */

public class HTTPResponseUtils {

    private static final String LOG_TAG = HTTPResponseUtils.class.getSimpleName();
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

    public static String errorServerResponseConvert(ClientInterface client, ResponseBody errorBody){
        String serverErrorMessage = "Error: Not Successful";
        //Invalid Investment Amount (e.g. 0, -1, etc)
        if(errorBody != null) {
            Converter<ResponseBody, MessageResponse> errorConverter =
                    client.getRetrofit().responseBodyConverter(
                            MessageResponse.class, new Annotation[0]);
            try{
                MessageResponse errorResponse = errorConverter
                        .convert(errorBody);
                serverErrorMessage = errorResponse.getServerResponse().getMessage();
                Log.d(LOG_TAG, "APP errorServerResponseConvert 4xx Error Code: " +
                        errorResponse.getServerResponse().getStatus());
            }catch (IOException e) {
                e.printStackTrace();
                Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
            }
        }
        return serverErrorMessage;
    }

}
