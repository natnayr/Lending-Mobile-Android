package com.crowdo.p2pconnect.view.activities;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.andretietz.retroauth.AuthenticationActivity;
import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.data.client.AuthClient;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.model.response.AuthResponse;
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
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.AuthListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Set;

import butterknife.BindString;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by cwdsg05 on 10/3/17.
 */

public class AuthActivity extends AuthenticationActivity implements Observer<Response<AuthResponse>> {

    private final static String LOG_TAG = AuthActivity.class.getSimpleName();

    @BindString(R.string.auth_welcome_message) String mWelcomeMessage;
    @BindString(R.string.auth_fb_email_permission_required) String mFbEmailRequired;

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

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);

        Log.d(LOG_TAG, "APP AuthActivity onCreate");

        callbackManagerFB = CallbackManager.Factory.create();

        authListenerLI = new AuthListener() {
            @Override
            public void onAuthSuccess() {

            }

            @Override
            public void onAuthError(LIAuthError liAuthError) {

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
                        Toast.makeText(AuthActivity.this, mFbEmailRequired, Toast.LENGTH_LONG).show();
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
                Toast.makeText(AuthActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
        Log.d(LOG_TAG, "APP onActivityResult " + requestCode);

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
                            Toast.makeText(AuthActivity.this, jse.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        if(fbName != null && fbId != null)

                        mAuthClient = AuthClient.getInstance(AuthActivity.this);
                        //Start fb hander here
                        mAuthClient.socialAuthUser(ConstantVariables.AUTH_FACEBOOK_PROVIDER_VALUE,
                                fbId, accessToken.getToken(),
                                ConstantVariables.getUniqueAndroidID(AuthActivity.this))
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(AuthActivity.this);
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
    public void onNext(Response<AuthResponse> authResponseResponse) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
