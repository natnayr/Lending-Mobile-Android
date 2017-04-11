package com.crowdo.p2pconnect.helpers;

import android.util.Log;

import com.crowdo.p2pconnect.data.response_model.APIErrorResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by cwdsg05 on 11/4/17.
 */

public class ErrorUtils {
    private static final String LOG_TAG = ErrorUtils.class.getSimpleName();

    public static APIErrorResponse parseError(ResponseBody errorBody){
        Gson gson = new Gson();
        APIErrorResponse apiError = new APIErrorResponse();
        try{
            Log.d(LOG_TAG, "APP parseError: " + errorBody.string());
            apiError = gson.fromJson(errorBody.string(), APIErrorResponse.class);
        }catch (IOException e){
            Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
            e.printStackTrace();
        }
        return apiError;
    }
}
