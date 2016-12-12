package com.crowdo.p2pmobile;

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
//                .subscribe(new Observer<List<LoanListItem>>() {
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
//                    public void onNext(List<LoanListItem> loanItems) {
//                        System.out.println("onNext");
//
//                        assertTrue(loanItems.size() > 0);
//                        // Print out first entry to check
//                        System.out.println("ListSize => " + loanItems.size());
//                        LoanListItem item = loanItems.get(0);
//                        System.out.println("tenure => " + item.tenure);
//                        System.out.println("collateral => " + item.collateral);
//                        System.out.println("targetAmount => " + item.targetAmount);
//                        System.out.println("grade => " + item.grade);
//                        System.out.println("fundingDuration => " + item.fundingDuration);
//                        System.out.println("fundingStartDate => " + item.fundingStartDate);
//                        System.out.println("fundingEndDate => " + item.fundingEndDate);
//                        System.out.println("loanId => " + item.loanId);
//                        System.out.println("fundedPercentageCache => " + item.fundedPercentageCache);
//                        System.out.println("interestRate => " + item.interestRate);
//                        System.out.println("loanStatus => " + item.loanStatus);
//                        System.out.println("sortWeight => " + item.sortWeight);
//                        System.out.println("security => " + item.security);
//                        System.out.println("id => " + item.id);
//                        System.out.println("fundingAmountToCompleteCache => " + item.fundingAmountToCompleteCache);
//                    }
//                });
//    }
}
