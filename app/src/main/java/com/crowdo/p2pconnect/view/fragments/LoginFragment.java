package com.crowdo.p2pconnect.view.fragments;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.data.client.AuthClient;
import com.crowdo.p2pconnect.model.response.ErrorResponse;
import com.crowdo.p2pconnect.model.response.AuthResponse;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.HashingUtils;
import com.crowdo.p2pconnect.helpers.RegexValidationUtil;
import com.crowdo.p2pconnect.helpers.SnackBarUtil;
import com.crowdo.p2pconnect.helpers.HTTPResponseUtils;
import com.crowdo.p2pconnect.helpers.SoftInputHelper;
import com.crowdo.p2pconnect.model.core.Member;
import com.crowdo.p2pconnect.oauth.CrowdoAccountGeneral;
import com.crowdo.p2pconnect.view.activities.AuthActivity;
import com.crowdo.p2pconnect.viewholders.LoginViewHolder;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.concurrent.TimeUnit;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by cwdsg05 on 8/3/17.
 */

public class LoginFragment extends Fragment implements Observer<Response<AuthResponse>>{

    @BindString(R.string.auth_http_error_message) String mHttpErrorServerMessage;
    @BindString(R.string.auth_http_handling_message) String mHttpErrorHandlingMessage;
    @BindString(R.string.auth_email_incorrect_format) String mEmailIncorrectFormatMessage;
    @BindString(R.string.auth_email_not_valid) String mEmailInvalidPrompt;
    @BindColor(R.color.color_accent) int mColorAccent;
    @BindColor(R.color.color_icons_text) int mColorIconText;
    @BindColor(R.color.color_primary_700) int mColorPrimaryDark;

    private static final String LOG_TAG = LoginFragment.class.getSimpleName();
    public static final String LOGIN_FRAGMENT_TAG = "LOGIN_FRAGMENT_TAG";

    private AuthClient authClient;
    private LoginViewHolder viewHolder;
    private Disposable disposableLoginUser;
    private String initAccountType;
    private String initAccountEmail;
    private String mPasswordHash;
    private AuthResponse authResponse;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initAccountType = getArguments().getString(AuthActivity.ARG_ACCOUNT_TYPE);
        this.initAccountEmail = getArguments().getString(AuthActivity.ARG_ACCOUNT_EMAIL);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, parent, false);
        ButterKnife.bind(this, rootView);

        viewHolder = new LoginViewHolder(rootView, getActivity());
        viewHolder.initView();

        viewHolder.mLoginExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        viewHolder.mLoginSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear keyboard
                SoftInputHelper.hideSoftKeyboard(getActivity());
                submit();
            }
        });

        //fill email if passed through
        if(initAccountEmail != null){
            if(initAccountEmail.length() > 0){
                viewHolder.mLoginEmailEditText.setText(initAccountEmail);
            }
        }

        SoftInputHelper.setupUI(rootView, getActivity(), new EditText[]{
                viewHolder.mLoginEmailEditText, viewHolder.mLoginPasswdEditText});

        viewHolder.mLoginRedirectToRegisterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFragment registerFragment = new RegisterFragment();
                registerFragment.setArguments(getArguments());

                getFragmentManager().beginTransaction()
                        .replace(R.id.auth_content, registerFragment)
                        .commit();
            }
        });

        return rootView;
    }

    @Override
    public void onPause() {
        if(disposableLoginUser != null){
            if(!disposableLoginUser.isDisposed()) {
                disposableLoginUser.dispose();
            }
        }
        super.onPause();
    }

    private void submit() {
        final String inputEmail = viewHolder.mLoginEmailEditText.getText().toString().trim();
        final String inputPassword = viewHolder.mLoginPasswdEditText.getText().toString().trim();

        //hide keyboard
        SoftInputHelper.hideSoftKeyboard(getActivity());

        //local incorrect email check
        if (!RegexValidationUtil.isValidEmailFormat(inputEmail)) {
            final Snackbar snack = SnackBarUtil.snackBarForErrorCreate(getView(),
                    mEmailIncorrectFormatMessage, Snackbar.LENGTH_SHORT);
            snack.show();
            viewHolder.mLoginEmailEditText.setError(mEmailInvalidPrompt);
            return;
        }

        //store password
        mPasswordHash = HashingUtils.hashSHA256(inputPassword);

        //do http call
        authClient = AuthClient.getInstance(getActivity());
        authClient.loginUser(inputEmail, inputPassword,
                ConstantVariables.getUniqueAndroidID(getActivity()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
    }


    @Override
    public void onSubscribe(Disposable d) {
        disposableLoginUser = d;
    }

    @Override
    public void onNext(Response<AuthResponse> response) {
        Log.d(LOG_TAG, "APP onNext HTTP raw:" + response.raw());

        if(response.isSuccessful()) {
            authResponse = response.body();
        }else {
            //failed login response from serverResponse, 4xx error
            if (HTTPResponseUtils.check4xxClientError(response.code())) {
                String serverErrorMsg = "Error: Login not successful";

                if (response.errorBody() != null) {
                    Converter<ResponseBody, ErrorResponse> errorConverter =
                            authClient.getRetrofit().responseBodyConverter(
                                    ErrorResponse.class, new Annotation[0]);
                    try {
                        ErrorResponse errorResponse = errorConverter.convert(response.errorBody());
                        serverErrorMsg = errorResponse.getServerResponse().getMessage();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
                    }
                }

                viewHolder.mLoginPasswdEditText.setText(""); //clear password
                //Error Snackbar
                SnackBarUtil.snackBarForErrorCreate(getView(),
                        serverErrorMsg, Snackbar.LENGTH_SHORT).show();
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                return;
            }

            //other errors, just throw
            viewHolder.mLoginPasswdEditText.setText("");
            String errorBody = response.code() + " Error: " + response.message();

            SnackBarUtil.snackBarForErrorCreate(getView(),
                    errorBody,
                    Snackbar.LENGTH_SHORT).show();
            Log.e(LOG_TAG, "ERROR: " + errorBody);
        }
    }

    @Override
    public void onError(Throwable e) {
        //HTTP ERROR Handling
        viewHolder.mLoginPasswdEditText.setText("");
        e.printStackTrace();
        Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
        SnackBarUtil.snackBarForErrorCreate(getView(),
                mHttpErrorHandlingMessage,
                Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onComplete() {
        Log.d(LOG_TAG, "APP onCompleted");

        //success login
        if(authResponse != null){
            if (HTTPResponseUtils.check2xxSuccess(authResponse.getServerResponse().getStatus())) {
                Log.d(LOG_TAG, "APP: onComplete > response.isSuccessful TRUE");

                if (HTTPResponseUtils.check2xxSuccess(authResponse.getServerResponse().getStatus())) {
                    //show http success
                    SnackBarUtil.snackBarForErrorCreate(getView(),
                            authResponse.getServerResponse().getMessage(),
                            Snackbar.LENGTH_SHORT).show();

                    final String email = authResponse.getMember().getEmail();
                    final String authToken = authResponse.getAuthToken();
                    final String passwordHash = mPasswordHash;
                    final Member member = authResponse.getMember();

                    final Bundle userData = new Bundle();
                    userData.putString(AuthActivity.POST_AUTH_MEMBER_ID, member.getId().toString());
                    userData.putString(AuthActivity.POST_AUTH_MEMBER_EMAIL, member.getEmail());
                    userData.putString(AuthActivity.POST_AUTH_MEMBER_NAME, member.getName());
                    userData.putString(AuthActivity.POST_AUTH_MEMBER_LOCALE, member.getLocalePreference());

                    //return back to authenticator result handling
                    Bundle data = new Bundle();
                    data.putString(AccountManager.KEY_ACCOUNT_NAME, email);
                    data.putString(AccountManager.KEY_ACCOUNT_TYPE, CrowdoAccountGeneral.ACCOUNT_TYPE);
                    data.putString(AccountManager.KEY_AUTHTOKEN, authToken);
                    data.putString(AuthActivity.PARAM_USER_PASS_HASH, passwordHash);

                    final Intent res = new Intent();
                    res.putExtras(data);

                    //go back to AuthActivity to create account
                    ((AuthActivity) getActivity()).finishAuth(res, userData);
                }
            }
        }
    }
}
