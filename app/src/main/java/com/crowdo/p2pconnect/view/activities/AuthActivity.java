package com.crowdo.p2pconnect.view.activities;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.andretietz.retroauth.AuthenticationActivity;
import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.data.client.AuthClient;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.HTTPResponseUtils;
import com.crowdo.p2pconnect.helpers.SharedPreferencesUtils;
import com.crowdo.p2pconnect.helpers.SnackBarUtil;
import com.crowdo.p2pconnect.model.core.Member;
import com.crowdo.p2pconnect.model.response.AuthResponse;
import com.crowdo.p2pconnect.model.response.MessageResponse;
import com.crowdo.p2pconnect.support.NetworkConnectionChecks;
import com.crowdo.p2pconnect.helpers.LocaleHelper;
import com.crowdo.p2pconnect.view.fragments.LoginFragment;
import com.crowdo.p2pconnect.view.fragments.RegisterFragment;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.linkedin.platform.LISession;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.AuthListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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
 * Created by cwdsg05 on 10/3/17.
 */

public class AuthActivity extends AuthenticationActivity implements Observer<Response<AuthResponse>> {

    private final static String LOG_TAG = AuthActivity.class.getSimpleName();

    @BindString(R.string.auth_welcome_message) String mWelcomeMessage;
    @BindString(R.string.auth_fb_email_permission_required) String mFbEmailRequired;
    @BindString(R.string.auth_http_error_message) String mHttpErrorServerMessage;
    @BindString(R.string.auth_http_handling_message) String mHttpErrorHandlingMessage;


    public final static String AUTH_MEMBER_EMAIL = "AUTH_MEMBER_EMAIL";
    public final static String AUTH_MEMBER_NAME = "AUTH_MEMBER_NAME";
    public final static String AUTH_MEMBER_TOKEN = "AUTH_MEMBER_TOKEN";
    public final static String AUTH_MEMBER_LOCALE = "AUTH_MEMBER_LOCALE";

    public final static int REQUEST_FRAGMENT_RESULT = 123;
    public final static String FRAGMENT_CLASS_TAG_CALL = "AUTH_ACTIVITY_FRAGMENT_CLASS_TAG_CALL";

    public AuthListener authListenerLI;
    private CallbackManager callbackManagerFB;
    private AuthClient mAuthClient;
    private Disposable disposableSocialAuthUser;
    private AuthResponse authResponse;
    private View mRootView;


    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);

        ////////// TO REMOVE
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.crowdo.p2pconnect",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e(LOG_TAG, "ERROR hash key: " + something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e(LOG_TAG, "ERROR name not found: " + e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e(LOG_TAG, "ERROR no such an algorithm: " + e.toString());
        } catch (Exception e) {
            Log.e(LOG_TAG,  "ERROR exception: " + e.toString());
        }
        /////////////

        mRootView = findViewById(android.R.id.content);

        callbackManagerFB = CallbackManager.Factory.create();

        authListenerLI = new AuthListener() {
            @Override
            public void onAuthSuccess() {
                LISession liSession = LISessionManager
                        .getInstance(AuthActivity.this)
                        .getSession();
                submitLinkedin(liSession);
            }

            @Override
            public void onAuthError(LIAuthError liAuthError) {
                Log.d(LOG_TAG, "APP onAuthError: " + liAuthError.toString());
                SnackBarUtil.snackBarForWarningCreate(mRootView,
                        liAuthError.toString(), Snackbar.LENGTH_LONG).show();
            }
        };

        LoginManager.getInstance().registerCallback(callbackManagerFB,
                new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if(loginResult.getAccessToken() != null){
                    Set<String> deniedPermissions = loginResult.getRecentlyDeniedPermissions();
                    if(deniedPermissions.contains("email")){
                        LoginManager.getInstance().logOut(); //log user out since no email given

                        //then tell and re-login
                        SnackBarUtil.snackBarForInfoCreate(mRootView, mFbEmailRequired,
                                Snackbar.LENGTH_LONG).show();
                        LoginManager.getInstance().logInWithReadPermissions(AuthActivity.this,
                                Arrays.asList(ConstantVariables.AUTH_FACEBOOK_READ_PERMISSIONS));
                    }else{
                        submitFB(loginResult);
                    }
                }
            }

            @Override
            public void onCancel() {  }

            @Override
            public void onError(FacebookException error) {
                Log.e(LOG_TAG, "ERROR " + error.getMessage(), error);
                SnackBarUtil.snackBarForErrorCreate(mRootView, error.getMessage(),
                        Snackbar.LENGTH_LONG).show();
            }
        });

        Bundle extras = getIntent().getExtras();

        Fragment fragment = null;
        String fragmentTag = extras.getString(FRAGMENT_CLASS_TAG_CALL);
        if(fragmentTag != null) {
            if(fragmentTag.equals(LoginFragment.LOGIN_FRAGMENT_TAG)){
                fragment = new LoginFragment();
            }else if(fragmentTag.equals(RegisterFragment.REGISTER_FRAGMENT_TAG)){
                fragment = new RegisterFragment();
            }
        }else{
            fragment = new LoginFragment(); //default
        }
        //fragment should be either Login or Register
        getSupportFragmentManager().beginTransaction()
                .add(R.id.auth_content, fragment)
                .commit();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    public void finishAuth(Bundle userData){

        Log.d(LOG_TAG, "APP finishAuth");
        final String accountUserName = userData.getString(AuthActivity.AUTH_MEMBER_NAME);
        final String accountUserEmail = userData.getString(AuthActivity.AUTH_MEMBER_EMAIL);
        final String accountAuthToken = userData.getString(AuthActivity.AUTH_MEMBER_TOKEN);
        final String accountUserLocale = userData.getString(AuthActivity.AUTH_MEMBER_LOCALE);

        if(accountAuthToken != null) {
            Toast.makeText(AuthActivity.this, mWelcomeMessage +
                    accountUserName, Toast.LENGTH_LONG).show();

            final Account userAccount = createOrGetAccount(accountUserName);
            storeToken(userAccount, getRequestedTokenType(), accountAuthToken);
            storeUserData(userAccount, getString(R.string.authentication_EMAIL), accountUserEmail);
            storeUserData(userAccount, getString(R.string.authentication_LOCALE), accountUserLocale);
            finalizeAuthentication(userAccount);
        }
    }

    @Override
    public void onBackPressed() {
        Intent launchIntent = new Intent(AuthActivity.this, LaunchActivity.class);
        startActivityForResult(launchIntent, REQUEST_FRAGMENT_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(LOG_TAG, "APP Auth onActivityResult " + requestCode);

        if(requestCode == REQUEST_FRAGMENT_RESULT) {
            Fragment fragment = null;
            if(resultCode == LaunchActivity.RESULT_CODE_REGISTER){
                fragment = new RegisterFragment();
            }else{
                fragment = new LoginFragment();
            }
            //fragment should be either Login or Register
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.auth_content, fragment)
                    .commitAllowingStateLoss();
        }else{
            LISessionManager.getInstance(getApplicationContext()).onActivityResult(this,
                    requestCode, resultCode, data);
            callbackManagerFB.onActivityResult(requestCode, resultCode, data);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //check network and dun show loggout
        NetworkConnectionChecks.isOnline(this);
    }

    @Override
    protected void onStop() {
        if(disposableSocialAuthUser != null){
            if(!disposableSocialAuthUser.isDisposed()){
                disposableSocialAuthUser.dispose();
            }
        }
        super.onStop();
    }

    private void submitLinkedin(LISession liSession){

        Log.d(LOG_TAG, "APP liSession token " + liSession.getAccessToken().getValue());

    }

    private void submitFB(LoginResult loginResult){
        final AccessToken accessToken = loginResult.getAccessToken();

        Log.d(LOG_TAG, "APP Login FB AccessToken: " + loginResult.getAccessToken().getToken());

        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d(LOG_TAG, "APP Login FB response: " + response.getRawResponse());
                        String fbName = null;
                        String fbId = null;

                        try{
                            fbName = object.getString("name");
                            fbId = object.getString("id");
                        }catch (JSONException jse){
                            Log.e(LOG_TAG, "ERROR " + jse.getMessage(), jse);

                            SnackBarUtil.snackBarForErrorCreate(mRootView, jse.getMessage(),
                                    Snackbar.LENGTH_LONG).show();
                            return;
                        }

                        if(fbName != null && fbId != null) {
                            mAuthClient = AuthClient.getInstance(AuthActivity.this);
                            //Start fb hander here
                            mAuthClient.socialAuthUser(ConstantVariables.AUTH_FACEBOOK_PROVIDER_VALUE,
                                    fbId, accessToken.getToken(),
                                    LocaleHelper.getLanguage(AuthActivity.this),
                                    ConstantVariables.getUniqueAndroidID(AuthActivity.this))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(AuthActivity.this);
                        }
                    }
                }
        );
        request.executeAsync();
    }

    @Override
    public void onSubscribe(Disposable d) {
        this.disposableSocialAuthUser = d;
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
                            mAuthClient.getRetrofit().responseBodyConverter(
                                    MessageResponse.class, new Annotation[0]);
                    try {
                        MessageResponse errorResponse = errorConverter.convert(response.errorBody());
                        serverErrorMsg = errorResponse.getServer().getMessage();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(LOG_TAG, "ERROR " + e.getMessage(), e);
                    }
                }

                //Error Snackbar
                SnackBarUtil.snackBarForWarningCreate(mRootView,
                        serverErrorMsg, Snackbar.LENGTH_SHORT).show();
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                return;
            }

            //other errors, just throw
            String errorBody = response.code() + " Error: " + response.message();
            SnackBarUtil.snackBarForErrorCreate(mRootView,
                    errorBody,
                    Snackbar.LENGTH_SHORT).show();
            Log.e(LOG_TAG, "ERROR " + errorBody);
        }
    }

    @Override
    public void onError(Throwable e) {
        //HTTP ERROR Handling
        e.printStackTrace();
        Log.e(LOG_TAG, "ERROR " + e.getMessage(), e);
        if(mRootView != null) {
            SnackBarUtil.snackBarForErrorCreate(mRootView,
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
                    if(mRootView != null) {
                        SnackBarUtil.snackBarForSuccessCreate(mRootView,
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
                    finishAuth(userData);
                }
            }
        }
    }
}
