package com.crowdo.p2pconnect.view.fragments;

import android.accounts.AccountManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.data.client.LoginClient;
import com.crowdo.p2pconnect.data.response.AuthResponse;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.RegexValidationUtil;
import com.crowdo.p2pconnect.helpers.SnackBarUtil;
import com.crowdo.p2pconnect.helpers.StatusCodeAPIUtil;
import com.crowdo.p2pconnect.model.Member;
import com.crowdo.p2pconnect.viewholders.LoginViewHolder;

import butterknife.BindColor;
import butterknife.ButterKnife;
import retrofit2.Response;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cwdsg05 on 8/3/17.
 */

public class LoginFragment extends Fragment{

    @BindColor(R.color.color_accent) int mColorAccent;
    @BindColor(R.color.color_icons_text) int mColorIconText;
    @BindColor(R.color.color_primary_700) int mColorPrimaryDark;
    private static final String LOG_TAG = LoginFragment.class.getSimpleName();

    private LoginViewHolder viewHolder;
    private Subscription loginSubscription;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, rootView);

        viewHolder = new LoginViewHolder(rootView, getActivity());
        viewHolder.initView();

        viewHolder.mExitImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        viewHolder.mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        return rootView;
    }

    private void submit(){
        String inputEmail = viewHolder.mEmailEditText.getText().toString().toLowerCase().trim();
        String inputPassword = viewHolder.mPasswdEditText.getText().toString();

        //local incorrect email check
        if(!RegexValidationUtil.isValidEmailID(inputEmail)){
            final Snackbar snack = SnackBarUtil.snackBarForAuthCreate(getView(),
                    "Email in Incorrect Format", Snackbar.LENGTH_SHORT,
                    mColorIconText, mColorPrimaryDark);
            snack.show();
            return;
        }

        //do http
        loginSubscription = LoginClient.getInstance()
                .loginUser(inputEmail, inputPassword,
                        ConstantVariables.getUniqueAndroidID(getActivity()))
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
                        if(!response.isSuccessful()){
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
                        if(StatusCodeAPIUtil.check4xxClientError(authResponse.getStatus())){
                            final Snackbar snack = SnackBarUtil.snackBarForAuthCreate(getView(),
                                    authResponse.getMessage(),
                                    Snackbar.LENGTH_SHORT,
                                    mColorIconText, mColorAccent);
                            snack.show();
                            return;
                        }

                        //success login
                        if(StatusCodeAPIUtil.check2xxSuccess(authResponse.getStatus())){
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
