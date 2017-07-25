package com.crowdo.p2pconnect.commons;

import android.app.Activity;
import android.util.Log;

import com.crowdo.p2pconnect.data.client.DefinitionsClient;
import com.crowdo.p2pconnect.helpers.CallBackUtil;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.model.response.DefinitionBankInfoResponse;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by cwdsg05 on 25/7/17.
 */

public class DefinitionsRetrieval {

    public static final String DEFINITIONS_INDONESIA_IDR = "indonesia";
    public static final String DEFINITIONS_MALAYSIA_MYR = "malaysia";
    public static final String DEFINITIONS_SINGAPORE_SGD = "singapore";
    public static final String DEFINITIONS_SINGAPORE_USD = "singapore_usd";
    public static final String DEFINITIONS_SINGAPORE_USD_OVERSEAS = "singapore_usd_oversea";

    public static final String LOG_TAG = DefinitionsRetrieval.class.getSimpleName();

    public void retreiveInfo(final Activity activity, final CallBackUtil<DefinitionBankInfoResponse> callBackUtil){

        DefinitionsClient.getInstance(activity)
                .getDefinitionsInfo(ConstantVariables.getUniqueAndroidID(activity))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<DefinitionBankInfoResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Response<DefinitionBankInfoResponse> response) {
                        if(response.isSuccessful()){
                            callBackUtil.eventCallBack(response.body());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                        Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(LOG_TAG, "APP retreiveInfo onComplete");
                    }
                });

    }
}
