package com.crowdo.p2pmobile;

import android.util.Log;

import com.crowdo.p2pmobile.data.APIServicesInterface;
import com.crowdo.p2pmobile.data.LoanItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.assertTrue;

/**
 * Created by cwdsg05 on 21/11/16.
 */

public class TestRetrofitCalls {

    public static final String BASE_URL = "https://krune2aiye.execute-api.ap-southeast-1.amazonaws.com/";
    public static final String STAGE = "dev/";
    public static final String LOG_TAG = TestRetrofitCalls.class.getSimpleName();

    @Test
    public void testDownloadList() throws Exception{

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL + STAGE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        APIServicesInterface apiService = retrofit.create(APIServicesInterface.class);
        Call<List<LoanItem>> call = apiService.getLoansList();

        Response<List<LoanItem>> response = call.execute();

        List<LoanItem> list = response.body();

        assertTrue(response.isSuccessful());
        assertTrue(list.size() > 0);

        System.out.println("ListSize => " + list.size());
        LoanItem item = list.get(0);
        System.out.println("tenure => " + item.tenureOut);
        System.out.println("collateralOut => " + item.collateralOut);
        System.out.println("targetAmountOut => " + item.targetAmountOut);
        System.out.println("grade => " + item.grade);
        System.out.println("fundingDuration => " + item.fundingDuration);
        System.out.println("fundingStartDate => " + item.fundingStartDate);
        System.out.println("fundingEndDate => " + item.fundingEndDate);
        System.out.println("loanIdOut => " + item.loanIdOut);
        System.out.println("fundedPercentageCache => " + item.fundedPercentageCache);
        System.out.println("interestRateOut => " + item.interestRateOut);
        System.out.println("loanStatus => " + item.loanStatus);
        System.out.println("sortWeight => " + item.sortWeight);
        System.out.println("security => " + item.security);
        System.out.println("collateralDescriptionOut => " + item.collateralDescriptionOut);
        System.out.println("id => " + item.id);
        System.out.println("fundingAmountToCompleteCache => " + item.fundingAmountToCompleteCache);
    }
}
