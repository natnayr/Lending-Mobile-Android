package com.crowdo.p2pconnect.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.data.client.AuthClient;
import com.crowdo.p2pconnect.helpers.LocaleHelper;
import com.crowdo.p2pconnect.model.response.MessageResponse;
import com.crowdo.p2pconnect.model.response.AuthResponse;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.RegexValidationUtil;
import com.crowdo.p2pconnect.helpers.SnackBarUtil;
import com.crowdo.p2pconnect.helpers.HTTPResponseUtils;
import com.crowdo.p2pconnect.helpers.SoftInputHelper;
import com.crowdo.p2pconnect.model.core.Member;
import com.crowdo.p2pconnect.view.activities.AuthActivity;
import com.crowdo.p2pconnect.viewholders.LoginViewHolder;
import com.facebook.login.widget.LoginButton;
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
 * Created by cwdsg05 on 8/3/17.
 */

public class LoginFragment extends Fragment implements Observer<Response<AuthResponse>>{

    @BindView(R.id.auth_login_lang_spinner) MaterialSpinner mLoginLangSpinner;
    @BindString(R.string.auth_http_error_message) String mHttpErrorServerMessage;
    @BindString(R.string.auth_http_handling_message) String mHttpErrorHandlingMessage;
    @BindString(R.string.auth_email_incorrect_format) String mEmailIncorrectFormatMessage;
    @BindString(R.string.auth_email_not_valid) String mEmailInvalidPrompt;
    @BindString(R.string.language_english_label) String mLanguageEnglishLabel;
    @BindString(R.string.language_bahasa_label) String mLanguageBahasaIndoLabel;
    @BindString(R.string.auth_social_wait) String mLoginRequestingWait;

    @BindColor(R.color.color_accent) int mColorAccent;
    @BindColor(R.color.color_icons_text) int mColorIconText;
    @BindColor(R.color.color_primary_700) int mColorPrimaryDark;

    private static final String LOG_TAG = LoginFragment.class.getSimpleName();
    public static final String LOGIN_FRAGMENT_TAG = "LOGIN_FRAGMENT_TAG";

    private AuthClient authClient;
    private LoginViewHolder viewHolder;
    private Disposable disposableLoginUser;
    private AuthResponse authResponse;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                getActivity().onBackPressed();
            }
        });

        viewHolder.mLoginSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

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

        final List<String> languageSet = new LinkedList<>(Arrays.asList(mLanguageEnglishLabel,
                mLanguageBahasaIndoLabel));
        mLoginLangSpinner.setItems(languageSet);

        String localeVal = LocaleHelper.getLanguage(getActivity());
        if(localeVal.equals(ConstantVariables.APP_LANG_ID)){
            mLoginLangSpinner.setSelectedIndex(1);
        }else if(localeVal.equals(ConstantVariables.APP_LANG_EN)){
            mLoginLangSpinner.setSelectedIndex(0);
        }

        mLoginLangSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
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

        viewHolder.mLoginLinkedinButton.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                        Toast.makeText(getActivity(), mLoginRequestingWait, Toast.LENGTH_LONG).show();
                       ((AuthActivity) getActivity()).callLinkedinAuth();
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
            final Snackbar snack = SnackBarUtil.snackBarForWarningCreate(getView(),
                    mEmailIncorrectFormatMessage, Snackbar.LENGTH_SHORT);
            snack.show();
            viewHolder.mLoginEmailEditText.setError(mEmailInvalidPrompt);
            return;
        }


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
        if(response.isSuccessful()) {
            authResponse = response.body();
        }else {
            //failed login response from serverResponse, 4xx error
            if (HTTPResponseUtils.check4xxClientError(response.code())) {
                String serverErrorMsg = "Error: Login not successful";

                if (response.errorBody() != null) {
                    Converter<ResponseBody, MessageResponse> errorConverter =
                            authClient.getRetrofit().responseBodyConverter(
                                    MessageResponse.class, new Annotation[0]);
                    try {
                        MessageResponse errorResponse = errorConverter.convert(response.errorBody());
                        serverErrorMsg = errorResponse.getServer().getMessage();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(LOG_TAG, "ERROR " + e.getMessage(), e);
                    }
                }

                viewHolder.mLoginPasswdEditText.setText(""); //clear password
                //Error Snackbar
                SnackBarUtil.snackBarForWarningCreate(getView(),
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
            Log.e(LOG_TAG, "ERROR " + errorBody);
        }
    }

    @Override
    public void onError(Throwable e) {
        //HTTP ERROR Handling
        viewHolder.mLoginPasswdEditText.setText("");
        e.printStackTrace();
        Log.e(LOG_TAG, "ERROR " + e.getMessage(), e);
        if(getView() != null) {
            SnackBarUtil.snackBarForErrorCreate(getView(),
                    mHttpErrorHandlingMessage,
                    Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onComplete() {
        Log.d(LOG_TAG, "APP onCompleted");

        //success login
        if(authResponse != null){
            if (HTTPResponseUtils.check2xxSuccess(authResponse.getServer().getStatus())) {
                Log.d(LOG_TAG, "APP onComplete > response.isSuccessful TRUE");

                if (HTTPResponseUtils.check2xxSuccess(authResponse.getServer().getStatus())) {
                    //show http success
                    if(getView() != null) {
                        SnackBarUtil.snackBarForSuccessCreate(getView(),
                                authResponse.getServer().getMessage(),
                                Snackbar.LENGTH_SHORT).show();
                    }

                    final Member member = authResponse.getMember();

                    final Bundle userData = new Bundle();
                    userData.putString(AuthActivity.AUTH_MEMBER_EMAIL, member.getEmail());
                    userData.putString(AuthActivity.AUTH_MEMBER_NAME, member.getName());
                    userData.putString(AuthActivity.AUTH_MEMBER_TOKEN, authResponse.getAuthToken());
                    userData.putString(AuthActivity.AUTH_MEMBER_LOCALE, member.getLocalePreference());

                    //go back to AuthActivity to create account
                    ((AuthActivity) getActivity()).finishAuth(userData);
                }
            }
        }
    }
}
