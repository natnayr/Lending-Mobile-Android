package com.crowdo.p2pconnect.view.activities;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.andretietz.retroauth.AuthenticationActivity;
import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.data.client.AuthClient;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.HTTPResponseUtils;
import com.crowdo.p2pconnect.helpers.SnackBarUtil;
import com.crowdo.p2pconnect.model.core.Member;
import com.crowdo.p2pconnect.model.response.AuthResponse;
import com.crowdo.p2pconnect.model.response.MessageResponse;
import com.crowdo.p2pconnect.oauth.LinkedInAuthHandler;
import com.crowdo.p2pconnect.oauth.SocialAuthConstant;
import com.crowdo.p2pconnect.support.NetworkConnectionChecks;
import com.crowdo.p2pconnect.helpers.LocaleHelper;
import com.crowdo.p2pconnect.view.fragments.LoginFragment;
import com.crowdo.p2pconnect.view.fragments.RegisterFragment;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ExecutionException;
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
 * AuthActivity is the landing activity for any "Failed to Authenticate" (e.g. status 401) requests,
 * startActivity to call this activity is located as the RetroAuth implementation for AuthenticationActivity under manifest for:
 * <action android:name="com.crowdo.p2pconnect.ACTION"/>.
 *
 * Note: as this activity for authentication failure, LaunchActivity is used only as the CHILD activity of this.
 * LaunchsActivity does not create new AuthActivity but simply terminates when login/register is clicked and fragment is handled here.
 *
 * See implementation: github.com/andretietz/retroauth/tree/master/retroauth-android
 */

public class AuthActivity extends AuthenticationActivity implements Observer<Response<AuthResponse>> {

    private final static String LOG_TAG = AuthActivity.class.getSimpleName();

    @BindString(R.string.auth_welcome_message) String mWelcomeMessage;
    @BindString(R.string.auth_fb_email_permission_required) String mFbEmailRequired;
    @BindString(R.string.auth_http_error_message) String mHttpErrorServerMessage;
    @BindString(R.string.auth_http_handling_message) String mHttpErrorHandlingMessage;
    @BindString(R.string.wait_message) String mWaitLabel;
    @BindString(R.string.linkedin_client_id) String mLinkedinClientID;
    @BindString(R.string.linkedin_client_secret) String mLinkedinClientSecret;

    // KEYS for Android Account Manager
    public final static String AUTH_MEMBER_EMAIL = "AUTH_MEMBER_EMAIL";
    public final static String AUTH_MEMBER_NAME = "AUTH_MEMBER_NAME";
    public final static String AUTH_MEMBER_TOKEN = "AUTH_MEMBER_TOKEN";
    public final static String AUTH_MEMBER_LOCALE = "AUTH_MEMBER_LOCALE";

    // for onActivityResult
    public final static int REQUEST_FRAGMENT_RESULT = 4562;
    public final static int REQUEST_LINKEDIN_OAUTH_RESULT = 9123;

    // Fragment tag for Login/Registration Fragment
    public final static String FRAGMENT_CLASS_TAG_CALL = "AUTH_ACTIVITY_FRAGMENT_CLASS_TAG_CALL";

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

        mRootView = findViewById(android.R.id.content);

        // Facebook ndroid sdk
        callbackManagerFB = CallbackManager.Factory.create();
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
                                Arrays.asList(SocialAuthConstant.AUTH_FACEBOOK_READ_PERMISSIONS));
                    }else{
                        SnackBarUtil.snackBarForInfoCreate(mRootView, mWaitLabel,
                                Snackbar.LENGTH_SHORT).show();

                        //process results, request API for crowdo token, using fb token
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

        // onCreate detect fragment choice from LaunchActivity or others
        Fragment fragment = null;
        String fragmentTag = extras.getString(FRAGMENT_CLASS_TAG_CALL);
        if(fragmentTag != null) {
            if(fragmentTag.equals(LoginFragment.LOGIN_FRAGMENT_TAG)){
                fragment = new LoginFragment();
            }else if(fragmentTag.equals(RegisterFragment.REGISTER_FRAGMENT_TAG)){
                fragment = new RegisterFragment();
            }
        }else{
            fragment = new LoginFragment(); //Default fragment
        }

        //fragment should be either Login or Register by here
        getSupportFragmentManager().beginTransaction()
                .add(R.id.auth_content, fragment)
                .commit();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    /**
     * Method is used for the creation of AccoutManager instance, stores token into account under AccountManager
     * using RetroAuth plugin (see gradle) along with other misc details for identification.
     * @param userData key-value bundle with 4 keys, name, email, token, locale
     */
    public void finishAuth(Bundle userData){
        Log.d(LOG_TAG, "APP finishAuth");
        final String accountUserName = userData.getString(AuthActivity.AUTH_MEMBER_NAME);
        final String accountUserEmail = userData.getString(AuthActivity.AUTH_MEMBER_EMAIL);
        final String accountAuthToken = userData.getString(AuthActivity.AUTH_MEMBER_TOKEN);
        final String accountUserLocale = userData.getString(AuthActivity.AUTH_MEMBER_LOCALE);

        //retreive token, if present then continue
        if(accountAuthToken != null) {
            Toast.makeText(AuthActivity.this, mWelcomeMessage +
                    accountUserName, Toast.LENGTH_LONG).show();

            //Retroauth plugin methods used here for ease of creating token auth account
            final Account userAccount = createOrGetAccount(accountUserName);
            storeToken(userAccount, getRequestedTokenType(), accountAuthToken);
            storeUserData(userAccount, getString(R.string.authentication_EMAIL), accountUserEmail);
            storeUserData(userAccount, getString(R.string.authentication_LOCALE), accountUserLocale);
            finalizeAuthentication(userAccount); //does finish() call to end activity (to main)
        }
    }

    @Override
    public void onBackPressed() {
        // reverse effect, when "back" button is pressed LaunchActivity is started
        Intent launchIntent = new Intent(AuthActivity.this, LaunchActivity.class);
        startActivityForResult(launchIntent, REQUEST_FRAGMENT_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(LOG_TAG, "APP Auth onActivityResult " + requestCode);

        if(requestCode == REQUEST_FRAGMENT_RESULT) {
            // From LaunchActivity login/register button click, fragment is called.
            // Note: Launch Activity is Child fragment of AuthActivity
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
        }else if(requestCode == REQUEST_LINKEDIN_OAUTH_RESULT){
            //Response for Social Linkedin Auth which uses webview for OAuth callback,
            //response from webview contains crowdo token.
            if(resultCode == RESULT_OK){
                //SUCCESS
                Log.d(LOG_TAG, "APP WebView RESULT_OK");
                String name = data.getStringExtra(SocialAuthConstant.AUTH_LINKEDIN_RESULT_NAME_EXTRA);
                String email = data.getStringExtra(SocialAuthConstant.AUTH_LINKEDIN_RESULT_EMAIL_EXTRA);
                String token = data.getStringExtra(SocialAuthConstant.AUTH_LINKEDIN_RESULT_TOKEN_EXTRA);
                String locale = data.getStringExtra(SocialAuthConstant.AUTH_LINKEDIN_RESULT_LOCALE_EXTRA);

                final Bundle userData = new Bundle();
                userData.putString(AuthActivity.AUTH_MEMBER_EMAIL, email);
                userData.putString(AuthActivity.AUTH_MEMBER_NAME, name);
                userData.putString(AuthActivity.AUTH_MEMBER_TOKEN, token);
                userData.putString(AuthActivity.AUTH_MEMBER_LOCALE, locale);

                //Create Account with finishAuth
                finishAuth(userData);

            }else if(resultCode == RESULT_CANCELED){
                //FAILURE
                Log.d(LOG_TAG, "APP WebView RESULT_CANCELED");
                if(data != null) {
                    if(data.hasExtra(SocialAuthConstant.AUTH_LINKEDIN_RESULT_FAILURE_EXTRA)) {
                        //Tell user failure
                        String errorMsg = data.getStringExtra(SocialAuthConstant.AUTH_LINKEDIN_RESULT_FAILURE_EXTRA);
                        SnackBarUtil.snackBarForWarningCreate(mRootView, errorMsg,
                                Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        }else{
            //Facebook Ndroid SDK passing response to LoginManager's registerCallback() *implementation on top^
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
        // dispose
        if(disposableSocialAuthUser != null){
            if(!disposableSocialAuthUser.isDisposed()){
                disposableSocialAuthUser.dispose();
            }
        }
        super.onStop();
    }

    // Post
    private void submitFB(LoginResult loginResult){
        //get fb token
        final com.facebook.AccessToken fbAccessToken = loginResult.getAccessToken();

        //post FB Token handling, do GraphAPI calls to get user info (id only)
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        //
                        String fbId = null;

                        try{
                            fbId = object.getString("id");
                        }catch (JSONException jse){
                            Log.e(LOG_TAG, "ERROR " + jse.getMessage(), jse);
                            SnackBarUtil.snackBarForErrorCreate(mRootView, jse.getMessage(),
                                    Snackbar.LENGTH_LONG).show();
                            return;
                        }

                        if(fbId != null) {
                            mAuthClient = AuthClient.getInstance(AuthActivity.this);
                            // API call to pass FB ID & Token to authenticate + login/register user via token,
                            // response is identical to login/register fragment implementation
                            mAuthClient.postFBSocialAuthUser(
                                    SocialAuthConstant.AUTH_FACEBOOK_PROVIDER_VALUE,
                                    fbId, fbAccessToken.getToken(),
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

    /**
     * Crowdo Social Auth response (for fb currently),
     * set object as global authResponse for onComplete()
     *
     * @param response from API Call
     */
    @Override
    public void onNext(Response<AuthResponse> response) {
        if(response.isSuccessful()) {
            authResponse = response.body();
        }else {
            String serverErrorMsg = "Error: Login/Register not successful";

            //failed login response from serverResponse, 4xx error
            if (HTTPResponseUtils.check4xxClientError(response.code())) {
                if (response.errorBody() != null) {
                    Converter<ResponseBody, MessageResponse> errorConverter =
                            mAuthClient.getRetrofit().responseBodyConverter(
                                    MessageResponse.class, new Annotation[0]);
                    try {
                        MessageResponse errorResponse = errorConverter.convert(response.errorBody());
                        serverErrorMsg = errorResponse.getServer().getMessage();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                        Log.e(LOG_TAG, "ERROR " + ioe.getMessage(), ioe);
                    } catch(NullPointerException npe){
                        npe.printStackTrace();
                        Log.e(LOG_TAG, "ERROR " + npe.getMessage(), npe);
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
            String errorBody = response.code() + serverErrorMsg;
            SnackBarUtil.snackBarForErrorCreate(mRootView,
                    errorBody,
                    Snackbar.LENGTH_SHORT).show();
            Log.e(LOG_TAG, "ERROR " + errorBody);
        }
    }

    /**
     * Error handling and notifying user
     * @param e
     */
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

    /**
     * onComplete, massage authResponse (if !null) for finishAuth()
     */
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

                    //assign bundle keys to AccountManager
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

    public void callLinkedinAuth(){
        LinkedInAuthHandler linkedInAuthHandler = new LinkedInAuthHandler(
                SocialAuthConstant.AUTH_LINKEDIN_CLIENT_ID,
                SocialAuthConstant.AUTH_LINKEDIN_CLIENT_SECRET, this);

        try {
            final String linkedInAuthUrl = linkedInAuthHandler.getAuthorizationUrl();

            Intent webViewIntent = new Intent(AuthActivity.this, WebViewActivity.class);
            webViewIntent.putExtra(WebViewActivity.URL_TARGET_EXTRA, linkedInAuthUrl);
            webViewIntent.putExtra(WebViewActivity.REQUIRE_AUTH_TOKEN_EXTRA, false);
            AuthActivity.this.startActivityForResult(webViewIntent, REQUEST_LINKEDIN_OAUTH_RESULT);

        }catch (IOException ioe){
            Log.e(LOG_TAG, "ERROR " + ioe.getMessage(), ioe);
        }catch(InterruptedException ie){
            Log.e(LOG_TAG, "ERROR " + ie.getMessage(), ie);
        }catch(ExecutionException ee){
            Log.e(LOG_TAG, "ERROR " + ee.getMessage(), ee);
        }
    }
}
