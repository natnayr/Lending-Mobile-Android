package com.crowdo.p2pconnect.commons;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.crowdo.p2pconnect.data.client.MemberClient;
import com.crowdo.p2pconnect.helpers.CallBackUtil;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.HTTPResponseUtils;
import com.crowdo.p2pconnect.helpers.SnackBarUtil;
import com.crowdo.p2pconnect.model.response.MemberInfoResponse;
import com.crowdo.p2pconnect.oauth.AuthAccountUtils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by cwdsg05 on 12/6/17.
 */

public class MemberDataRetrieval {

    public static final String LOG_TAG = MemberDataRetrieval.class.getSimpleName();
    private MemberInfoResponse mMemberInfoResposne = null;

    public void retrieveMemberInfo(final Activity activity, final CallBackUtil<MemberInfoResponse> callBackUtil){

        Log.d(LOG_TAG, "APP retrieveMemberInfo");

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
                            mMemberInfoResposne = response.body();
                            callBackUtil.eventCallBack(mMemberInfoResposne);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(LOG_TAG, "APP retrieveMemberInfo Rx onComplete");
                    }
                });
    }
}
