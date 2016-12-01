package com.crowdo.p2pmobile;

import com.crowdo.p2pmobile.data.APIServices;
import com.crowdo.p2pmobile.data.LoanItem;
import com.crowdo.p2pmobile.data.LoanListClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscription;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static org.junit.Assert.assertTrue;

/**
 * Created by cwdsg05 on 21/11/16.
 */

public class TestRetrofitCalls {

    public static final String BASE_URL = "https://krune2aiye.execute-api.ap-southeast-1.amazonaws.com/";
    public static final String STAGE = "dev/";
    public static final String LOG_TAG = TestRetrofitCalls.class.getSimpleName();

//    @Test
//    public void testDownloadList() throws Exception{
//
//        LoanListClient client = LoanListClient.getInstance();
//        Subscription subscription = LoanListClient.getInstance()
//                .getLiveLoans()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<List<LoanItem>>() {
//                    @Override
//                    public void onCompleted() {
//                        System.out.println("Completed");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        System.out.println("Error");
//                    }
//
//                    @Override
//                    public void onNext(List<LoanItem> loanItems) {
//                        System.out.println("onNext");
//
//                        assertTrue(loanItems.size() > 0);
//                        // Print out first entry to check
//                        System.out.println("ListSize => " + loanItems.size());
//                        LoanItem item = loanItems.get(0);
//                        System.out.println("tenure => " + item.tenureOut);
//                        System.out.println("collateralOut => " + item.collateralOut);
//                        System.out.println("targetAmountOut => " + item.targetAmountOut);
//                        System.out.println("grade => " + item.grade);
//                        System.out.println("fundingDuration => " + item.fundingDuration);
//                        System.out.println("fundingStartDate => " + item.fundingStartDate);
//                        System.out.println("fundingEndDate => " + item.fundingEndDate);
//                        System.out.println("loanIdOut => " + item.loanIdOut);
//                        System.out.println("fundedPercentageCache => " + item.fundedPercentageCache);
//                        System.out.println("interestRateOut => " + item.interestRateOut);
//                        System.out.println("loanStatus => " + item.loanStatus);
//                        System.out.println("sortWeight => " + item.sortWeight);
//                        System.out.println("security => " + item.security);
//                        System.out.println("id => " + item.id);
//                        System.out.println("fundingAmountToCompleteCache => " + item.fundingAmountToCompleteCache);
//                    }
//                });
//    }
}
