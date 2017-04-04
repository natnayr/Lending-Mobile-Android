package com.crowdo.p2pconnect.view.fragments;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.data.client.AuthClient;
import com.crowdo.p2pconnect.data.response_model.AuthResponse;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.HTTPResponseUtils;
import com.crowdo.p2pconnect.helpers.HashingUtils;
import com.crowdo.p2pconnect.helpers.LocaleHelper;
import com.crowdo.p2pconnect.helpers.RegexValidationUtil;
import com.crowdo.p2pconnect.helpers.SnackBarUtil;
import com.crowdo.p2pconnect.helpers.SoftInputHelper;
import com.crowdo.p2pconnect.view.activities.AuthActivity;
import com.crowdo.p2pconnect.viewholders.RegisterViewHolder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by cwdsg05 on 15/3/17.
 */

public class RegisterFragment extends Fragment{

    @BindString(R.string.auth_http_error_message) String mHttpErrorServerMessage;
    @BindString(R.string.auth_http_handling_message) String mHttpErrorHandlingMessage;

    @BindString(R.string.auth_email_incorrect_format) String mEmailIncorrectFormatMessage;
    @BindString(R.string.auth_email_not_valid) String mEmailInvalidPrompt;
    @BindString(R.string.auth_password_incorrect_format) String mPasswordIncorrectFormatMessage;
    @BindString(R.string.auth_password_too_short) String mPasswordTooShortPrompt;
    @BindString(R.string.auth_password_incorrect_match) String mPasswordIncorrectMatchMessage;
    @BindString(R.string.auth_password_do_not_match) String mPasswordDoNotMatchPrompt;

    @BindColor(R.color.color_accent) int mColorAccent;
    @BindColor(R.color.color_icons_text) int mColorIconText;
    @BindColor(R.color.color_primary_700) int mColorPrimaryDark;

    private static final String LOG_TAG = RegisterFragment.class.getSimpleName();
    public static final String REGISTER_FRAGMENT_TAG = "REGISTER_FRAGMENT_TAG";
    private static final int TIME_DELAY_FOR_SUCESS_TRANSFER = 1000;

    private RegisterViewHolder viewHolder;
    private Disposable disposableRegisterUser;
    private String initAccountType;
    private String mPasswordHash;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initAccountType = getArguments().getString(AuthActivity.ARG_ACCOUNT_TYPE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, rootView);

        viewHolder = new RegisterViewHolder(rootView, getActivity());
        viewHolder.init();

        viewHolder.mExitImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        viewHolder.mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if no view has focus:
                SoftInputHelper.hideSoftKeyboard(getActivity());

                submit();
            }
        });

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(disposableRegisterUser != null){
            if(!disposableRegisterUser.isDisposed()) {
                disposableRegisterUser.dispose();
            }
        }
    }

    private void submit() {
        final String inputEmail = viewHolder.mRegisterEmailEditText.getText().toString().toLowerCase().trim();
        final String inputName = viewHolder.mRegisterNameEditText.getText().toString().trim();
        final String inputPassword = viewHolder.mRegisterPasswordEmailText.getText().toString();
        final String inputConfirmPassword = viewHolder.mRegisterConfirmPasswdEditText.getText().toString();

        //fix emailbox for user
        if (!inputEmail.equals(viewHolder.mRegisterEmailEditText.getText().toString())) {
            viewHolder.mRegisterEmailEditText.setText(inputEmail);
        }

        //local incorrect email check
        if (!RegexValidationUtil.isValidEmailFormat(inputEmail)) {
            SnackBarUtil.snackBarForAuthCreate(getView(),
                    mEmailIncorrectFormatMessage, Snackbar.LENGTH_SHORT,
                    mColorIconText, mColorPrimaryDark).show();
            viewHolder.mRegisterEmailEditText.setError(mEmailInvalidPrompt);
            return;
        }

        //local incorrect password check
        if (!RegexValidationUtil.isValidPasswordLength(inputPassword)) {
            SnackBarUtil.snackBarForAuthCreate(getView(),
                    mPasswordIncorrectFormatMessage, Snackbar.LENGTH_SHORT,
                    mColorIconText, mColorPrimaryDark).show();
            viewHolder.mRegisterPasswordEmailText.setError(mPasswordTooShortPrompt);
            return;
        }

        //check if they match each other
        if (!inputPassword.equals(inputConfirmPassword)){
            SnackBarUtil.snackBarForAuthCreate(getView(),
                    mPasswordIncorrectMatchMessage, Snackbar.LENGTH_SHORT,
                    mColorIconText, mColorPrimaryDark).show();
            viewHolder.mRegisterConfirmPasswdEditText.setError(mPasswordDoNotMatchPrompt);
            return;
        }

        //store password
        mPasswordHash = HashingUtils.hashSHA256(inputPassword);

        AuthClient.getInstance()
                .registerUser(inputEmail, inputName, inputPassword, inputConfirmPassword,
                        LocaleHelper.getLanguage(getActivity()),
                        ConstantVariables.getUniqueAndroidID(getActivity()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<AuthResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposableRegisterUser = d;
                    }

                    @Override
                    public void onNext(Response<AuthResponse> response) {
                        Log.d(LOG_TAG, "APP: http-message:" + response.message()
                                + " http-code:" + response.code()
                                + ", http-body: {" + response.body().toString() + "}");

                        handleResult(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
                        SnackBarUtil.snackBarForAuthCreate(getView(),
                                "error: " + e.getMessage(),
                                Snackbar.LENGTH_SHORT,
                                mColorIconText, mColorPrimaryDark).show();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(LOG_TAG, "APP: onCompleted reached for AuthClient.registerUser()");

                    }
                });

    }

    private void handleResult(Response<AuthResponse> response){
        final Bundle data = new Bundle();
        final Intent res = new Intent();

        //check response
        if(!response.isSuccessful()){
            String errorBody = "error: http response error";
            try {
                errorBody = "error: " + response.errorBody().string();
            }catch (IOException e){
                e.printStackTrace();
                Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
            }
            SnackBarUtil.snackBarForAuthCreate(getView(),
                    errorBody,
                    Snackbar.LENGTH_SHORT,
                    mColorIconText, mColorPrimaryDark).show();
            Log.e(LOG_TAG, "ERROR: " + response.errorBody().toString());
            //TODO: do more error handling in future..
            return;
        }

        AuthResponse authResponse = response.body();

        //failed login response from server
        if(HTTPResponseUtils.check4xxClientError(authResponse.getStatus())){
            SnackBarUtil.snackBarForAuthCreate(getView(),
                    authResponse.getMessage(),
                    Snackbar.LENGTH_SHORT,
                    mColorIconText, mColorAccent).show();

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

            return;
        }

        //success register
        if(HTTPResponseUtils.check2xxSuccess(authResponse.getStatus())){
            //show success
            SnackBarUtil.snackBarForAuthCreate(getView(),
                    authResponse.getMessage(),
                    Snackbar.LENGTH_SHORT,
                    mColorIconText, mColorAccent).show();

            try {
                data.putString(AccountManager.KEY_ACCOUNT_NAME, authResponse.getMember().getEmail());
                data.putString(AccountManager.KEY_ACCOUNT_TYPE, initAccountType);
                data.putString(AccountManager.KEY_AUTHTOKEN, authResponse.getAuthToken());
                data.putString(AccountManager.KEY_PASSWORD, mPasswordHash);

            }catch(Exception e){
                SnackBarUtil.snackBarForAuthCreate(getView(),
                        mHttpErrorHandlingMessage + "\n" + e.getMessage(),
                        Snackbar.LENGTH_SHORT,
                        mColorIconText, mColorPrimaryDark).show();
                e.printStackTrace();
                Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
                return;
            }

            //wait to end, and pass to finishAuth to end
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    res.putExtras(data);
                    //finalise auth
                    ((AuthActivity) getActivity()).finishAuth(res, null);
                }
            }, TIME_DELAY_FOR_SUCESS_TRANSFER);
        }
    }
}
