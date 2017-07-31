package com.crowdo.p2pconnect.support;

import android.app.Activity;
import android.util.Log;

import com.crowdo.p2pconnect.data.client.MemberClient;
import com.crowdo.p2pconnect.helpers.CallBackUtil;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.model.response.MemberInfoResponse;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by cwdsg05 on 12/6/17.
 */

public class MemberInfoRetrieval {

    public static final String LOG_TAG = MemberInfoRetrieval.class.getSimpleName();

    public void retrieveInfo(final Activity activity, final CallBackUtil<MemberInfoResponse> callBackUtil){

        MemberClient.getInstance(activity)
                .getMemberInfo(ConstantVariables.getUniqueAndroidID(activity))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<MemberInfoResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<MemberInfoResponse> response) {
                        if(response.isSuccessful()){
                            callBackUtil.eventCallBack(response.body());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(LOG_TAG, "APP retrieveInfo onComplete");
                    }
                });
    }
}
