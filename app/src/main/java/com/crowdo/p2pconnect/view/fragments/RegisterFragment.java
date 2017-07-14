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
import com.crowdo.p2pconnect.model.response.AuthResponse;
import com.crowdo.p2pconnect.model.response.MessageResponse;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.HTTPResponseUtils;
import com.crowdo.p2pconnect.helpers.HashingUtils;
import com.crowdo.p2pconnect.helpers.LocaleHelper;
import com.crowdo.p2pconnect.helpers.RegexValidationUtil;
import com.crowdo.p2pconnect.helpers.SnackBarUtil;
import com.crowdo.p2pconnect.helpers.SoftInputHelper;
import com.crowdo.p2pconnect.model.core.Member;
import com.crowdo.p2pconnect.oauth.CrowdoAccountGeneral;
import com.crowdo.p2pconnect.view.activities.AuthActivity;
import com.crowdo.p2pconnect.viewholders.RegisterViewHolder;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by cwdsg05 on 15/3/17.
 */

public class RegisterFragment extends Fragment implements Observer<Response<AuthResponse>>{

    @BindView(R.id.auth_register_lang_spinner) MaterialSpinner mRegisterLangSpinner;

    @BindString(R.string.auth_http_error_message) String mHttpErrorServerMessage;
    @BindString(R.string.auth_http_handling_message) String mHttpErrorHandlingMessage;
    @BindString(R.string.auth_email_incorrect_format) String mEmailIncorrectFormatMessage;
    @BindString(R.string.auth_email_not_valid) String mEmailInvalidPrompt;
    @BindString(R.string.auth_password_incorrect_format) String mPasswordIncorrectFormatMessage;
    @BindString(R.string.auth_password_too_short) String mPasswordTooShortPrompt;
    @BindString(R.string.auth_password_incorrect_match) String mPasswordIncorrectMatchMessage;
    @BindString(R.string.auth_password_do_not_match) String mPasswordDoNotMatchPrompt;

    @BindString(R.string.language_english_label) String mLanguageEnglishLabel;
    @BindString(R.string.language_bahasa_label) String mLanguageBahasaIndoLabel;

    @BindColor(R.color.color_accent) int mColorAccent;
    @BindColor(R.color.color_icons_text) int mColorIconText;
    @BindColor(R.color.color_primary_700) int mColorPrimaryDark;

    private static final String LOG_TAG = RegisterFragment.class.getSimpleName();
    public static final String REGISTER_FRAGMENT_TAG = "REGISTER_FRAGMENT_TAG";

    private AuthClient mAuthClient;
    private RegisterViewHolder viewHolder;
    private Disposable disposableRegisterUser;
    private String mPasswordHash;
    private AuthResponse authResponse;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, rootView);

        viewHolder = new RegisterViewHolder(rootView, getActivity());
        viewHolder.init();

        viewHolder.mRegisterExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        viewHolder.mRegisterSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        SoftInputHelper.setupUI(rootView, getActivity(), new EditText[]{
                viewHolder.mRegisterEmailEditText, viewHolder.mRegisterNameEditText,
                viewHolder.mRegisterPasswordEmailText, viewHolder.mRegisterPasswordEmailText});

        viewHolder.mRegisterRedirectToLoginTextView
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment loginFragment = new LoginFragment();
                loginFragment.setArguments(getArguments());

                getFragmentManager().beginTransaction()
                        .replace(R.id.auth_content, loginFragment)
                        .commit();
            }
        });

        final List<String> languageSet = new LinkedList<>(Arrays.asList(mLanguageEnglishLabel, mLanguageBahasaIndoLabel));
        mRegisterLangSpinner.setItems(languageSet);

        mRegisterLangSpinner.setSelectedIndex(0);
        String localeVal = LocaleHelper.getLanguage(getActivity());
        if(localeVal.equals(ConstantVariables.APP_LANG_ID)){
            mRegisterLangSpinner.setSelectedIndex(1);
        }

        mRegisterLangSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if(languageSet.get(0).equals(item)){
                    //english
                    LocaleHelper.setLocale(getActivity(), ConstantVariables.APP_LANG_EN);
                    getActivity().recreate();
                }else if(languageSet.get(1).equals(item)){
                    //indo
                    LocaleHelper.setLocale(getActivity(), ConstantVariables.APP_LANG_ID);
                    getActivity().recreate();
                }
            }
        });

        return rootView;
    }


    @Override
    public void onPause() {
        if(disposableRegisterUser != null){
            if(!disposableRegisterUser.isDisposed()) {
                disposableRegisterUser.dispose();
            }
        }
        super.onPause();
    }

    private void submit() {
        final String inputEmail = viewHolder.mRegisterEmailEditText.getText().toString().trim();
        final String inputName = viewHolder.mRegisterNameEditText.getText().toString().trim();
        final String inputPassword = viewHolder.mRegisterPasswordEmailText.getText().toString();
        final String inputConfirmPassword = viewHolder.mRegisterConfirmPasswdEditText.getText().toString();

        //hide keyboard
        SoftInputHelper.hideSoftKeyboard(getActivity());

        //local incorrect email check
        if (!RegexValidationUtil.isValidEmailFormat(inputEmail)) {
            SnackBarUtil.snackBarForWarningCreate(getView(),
                    mEmailIncorrectFormatMessage, Snackbar.LENGTH_SHORT).show();
            viewHolder.mRegisterEmailEditText.setError(mEmailInvalidPrompt);
            return;
        }

        //local incorrect password check
        if (!RegexValidationUtil.isValidPasswordLength(inputPassword)) {
            SnackBarUtil.snackBarForWarningCreate(getView(),
                    mPasswordIncorrectFormatMessage, Snackbar.LENGTH_SHORT).show();
            viewHolder.mRegisterPasswordEmailText.setError(mPasswordTooShortPrompt);
            return;
        }

        //check if they match each other
        if (!inputPassword.equals(inputConfirmPassword)){
            SnackBarUtil.snackBarForWarningCreate(getView(),
                    mPasswordIncorrectMatchMessage, Snackbar.LENGTH_SHORT).show();
            viewHolder.mRegisterConfirmPasswdEditText.setError(mPasswordDoNotMatchPrompt);
            return;
        }

        //store password
        mPasswordHash = HashingUtils.hashSHA256(inputPassword);

        mAuthClient = AuthClient.getInstance(getActivity());
        mAuthClient.registerUser(inputEmail, inputName, inputPassword, inputConfirmPassword,
                        LocaleHelper.getLanguage(getActivity()),
                        ConstantVariables.getUniqueAndroidID(getActivity()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
    }

    private void handleResult(Response<AuthResponse> response){
        final Bundle data = new Bundle();
        final Intent res = new Intent();

        if(response.isSuccessful()) {
            Log.d(LOG_TAG, "APP: handleResult > response.isSuccessful()");
            AuthResponse oauth = response.body();


        }

    }

    @Override
    public void onSubscribe(Disposable d) {
        disposableRegisterUser = d;
    }

    @Override
    public void onNext(Response<AuthResponse> response) {

        handleResult(response);

        Log.d(LOG_TAG, "APP onNext HTTP raw:" + response.raw());

        if(response.isSuccessful()) {
            authResponse = response.body();
        }else {
            //failed register response from serverResponse, 4xx error
            if(HTTPResponseUtils.check4xxClientError(response.code())) {
                String serverErrorMsg = "Error: Registration not successful";

                if (response.errorBody() != null) {
                    Converter<ResponseBody, MessageResponse> errorConverter =
                            mAuthClient.getRetrofit().responseBodyConverter(
                                    MessageResponse.class, new Annotation[0]);
                    try {
                        MessageResponse errorResponse = errorConverter.convert(response.errorBody());
                        serverErrorMsg = errorResponse.getServer().getMessage();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
                    }
                }
                viewHolder.mRegisterPasswordEmailText.setText(""); //clear passwords
                viewHolder.mRegisterConfirmPasswdEditText.setText(""); //clear confirm passwords
                //Warning Snackbar
                SnackBarUtil.snackBarForWarningCreate(getView(),
                        serverErrorMsg,
                        Snackbar.LENGTH_SHORT).show();
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                return;
            }

            viewHolder.mRegisterPasswordEmailText.setText(""); //clear passwords
            viewHolder.mRegisterConfirmPasswdEditText.setText(""); //clear confirm passwords
            String errorBody = response.code() + " Error: " + response.message();

            SnackBarUtil.snackBarForErrorCreate(getView(),
                    errorBody,
                    Snackbar.LENGTH_SHORT).show();
            Log.e(LOG_TAG, "ERROR: " + errorBody);
        }
    }

    @Override
    public void onError(Throwable e) {
        viewHolder.mRegisterPasswordEmailText.setText(""); //clear passwords
        viewHolder.mRegisterConfirmPasswdEditText.setText(""); //clear confirm passwords
        e.printStackTrace();
        Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
        SnackBarUtil.snackBarForErrorCreate(getView(), mHttpErrorHandlingMessage,
                Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onComplete() {
        Log.d(LOG_TAG, "APP onCompleted");

        //success register
        if(authResponse != null) {
            if (HTTPResponseUtils.check2xxSuccess(authResponse.getServer().getStatus())) {
                Log.d(LOG_TAG, "APP: onComplete > response.isSuccessful TRUE");

                //show success
                if(getView() != null) {
                    SnackBarUtil.snackBarForSuccessCreate(getView(),
                            authResponse.getServer().getMessage(),
                            Snackbar.LENGTH_SHORT).show();
                }
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
