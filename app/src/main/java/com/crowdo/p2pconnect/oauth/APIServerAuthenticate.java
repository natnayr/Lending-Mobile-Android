package com.crowdo.p2pconnect.oauth;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.crowdo.p2pconnect.data.client.LoginClient;
import com.crowdo.p2pconnect.data.response.AuthResponse;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.SnackBarUtil;
import com.crowdo.p2pconnect.helpers.StatusCodeAPIUtil;
import com.crowdo.p2pconnect.model.Member;

import javax.security.auth.login.LoginException;

import retrofit2.Response;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cwdsg05 on 24/3/17.
 */

public class APIServerAuthenticate{

    private static final String LOG_TAG = APIServerAuthenticate.class.getSimpleName();
    public static final String RESPONSE_AUTH_TOKEN = "";
    public static final String

    public String userSignUp(String name, String email, String pass, String authType) throws Exception {
        return null;
    }

    public Bundle userSignIn(String email, String password, String authType, Context context) throws Exception {

        Bundle result = new Bundle();
        result.

        Subscription loginSubscription = LoginClient.getInstance()
                .loginUser(email, password, ConstantVariables.getUniqueAndroidID(context))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<AuthResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
                    }

                    @Override
                    public void onNext(Response<AuthResponse> response) {
                        Log.d(LOG_TAG, "APP: http-message:" + response.message()
                                + " http-code:" + response.code()
                                + ", http-body: {" + response.body().toString() + "}");


                        //check response
                        if (!response.isSuccessful()) {

                            final Snackbar snack = SnackBarUtil.snackBarForAuthCreate(getView(),
                                    "Error: " + response.errorBody(),
                                    Snackbar.LENGTH_SHORT,
                                    mColorIconText, mColorPrimaryDark);
                            snack.show();
                            //TODO: do error handling in future..
                            return;
                        }

                        AuthResponse authResponse = response.body();

                        //failed login response from server
                        if (StatusCodeAPIUtil.check4xxClientError(authResponse.getStatus())) {
                            final Snackbar snack = SnackBarUtil.snackBarForAuthCreate(getView(),
                                    authResponse.getMessage(),
                                    Snackbar.LENGTH_SHORT,
                                    mColorIconText, mColorAccent);
                            snack.show();
                            return;
                        }

                        //success login
                        if (StatusCodeAPIUtil.check2xxSuccess(authResponse.getStatus())) {
                            final Snackbar snack = SnackBarUtil.snackBarForAuthCreate(getView(),
                                    authResponse.getMessage(),
                                    Snackbar.LENGTH_SHORT,
                                    mColorIconText, mColorAccent);
                            snack.show();

                            Member memberInfo = authResponse.getMember();
                            String token = authResponse.getAuthToken();


                        }

                    }
                });

    }
}
