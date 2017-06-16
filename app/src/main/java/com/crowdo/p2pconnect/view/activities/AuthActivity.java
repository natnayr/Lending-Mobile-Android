package com.crowdo.p2pconnect.view.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.commons.NetworkConnectionChecks;
import com.crowdo.p2pconnect.data.APIServices;
import com.crowdo.p2pconnect.helpers.CallBackUtil;
import com.crowdo.p2pconnect.helpers.SnackBarUtil;
import com.crowdo.p2pconnect.oauth.AuthAccountUtils;
import com.crowdo.p2pconnect.helpers.LocaleHelper;
import com.crowdo.p2pconnect.helpers.SharedPreferencesUtils;
import com.crowdo.p2pconnect.oauth.AccountAuthenticatorFragmentActivity;
import com.crowdo.p2pconnect.oauth.CrowdoAccountGeneral;
import com.crowdo.p2pconnect.view.fragments.LoginFragment;
import com.crowdo.p2pconnect.view.fragments.RegisterFragment;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;


/**
 * Created by cwdsg05 on 10/3/17.
 */

public class AuthActivity extends AccountAuthenticatorFragmentActivity {

    private final static String LOG_TAG = AuthActivity.class.getSimpleName();

    public final static String PARAM_USER_PASS_HASH = "USER_PASS_HASH";
    public final static String ARG_ACCOUNT_EMAIL = "ACCOUNT_EMAIL";
    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TOKEN_TYPE = "AUTH_TOKEN_TYPE";
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";

    public final static String POST_AUTH_MEMBER_ID = "AUTH_MEMBER_KEY_ID";
    public final static String POST_AUTH_MEMBER_EMAIL = "AUTH_MEMBER_KEY_EMAIL";
    public final static String POST_AUTH_MEMBER_NAME = "AUTH_MEMBER_KEY_NAME";
    public final static String POST_AUTH_MEMBER_LOCALE = "AUTH_MEMBER_KEY_LOCALE";

    public final static String ARG_KEY_MESSAGE_ERROR = "MSG_ERR";
    public final static String FRAGMENT_CLASS_TAG_CALL = "FRAGMENT_CLASS";

    private AccountManager mAccountManager;
    private String mAccountName;
    private String mAccountType;
    private String mAuthTokenType;
    private boolean mIsNewAccountRequested;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_auth);

        mAccountManager = AccountManager.get(getBaseContext());
        Bundle extras = getIntent().getExtras();

        mAccountName = extras.getString(ARG_ACCOUNT_EMAIL);
        mAccountType = extras.getString(ARG_ACCOUNT_TYPE);
        mAuthTokenType = extras.getString(ARG_AUTH_TOKEN_TYPE);
        mIsNewAccountRequested = extras.getBoolean(ARG_IS_ADDING_NEW_ACCOUNT);

        if(mAccountType == null){
            mAccountType = CrowdoAccountGeneral.ACCOUNT_TYPE;
        }

        String fragmentTag = extras.getString(FRAGMENT_CLASS_TAG_CALL);
        if(fragmentTag != null) {
            Fragment fragment = null;
            if(fragmentTag.equals(LoginFragment.LOGIN_FRAGMENT_TAG)){
                fragment = new LoginFragment();
            }else if(fragmentTag.equals(RegisterFragment.REGISTER_FRAGMENT_TAG)){
                fragment = new RegisterFragment();
            }

            if(fragment != null) {
                Bundle args = new Bundle();
                //append if not null
                if(mAccountName != null) {
                    args.putString(AuthActivity.ARG_ACCOUNT_EMAIL, mAccountName);
                }

                args.putString(AuthActivity.ARG_ACCOUNT_TYPE, mAccountType);
                fragment.setArguments(args);

                //fragment should be either Login or Register
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.auth_content, fragment)
                        .commit();
            }
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    public void finishAuth(final Intent intent, final Bundle userData){

        Log.d(LOG_TAG, "APP finishAuth");

        final String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        final String accountPasswordHash = intent.getStringExtra(AuthActivity.PARAM_USER_PASS_HASH);
        final String authToken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
        
        //remove all other accounts
        AuthAccountUtils.removeAccounts(this);

        //Create Account
        final Account account = new Account(accountName, CrowdoAccountGeneral.ACCOUNT_TYPE);

        Log.d(LOG_TAG, "APP finishAuth() > addAccountExplicitly");
        boolean accountSuccess = mAccountManager.addAccountExplicitly(account, accountPasswordHash, userData);

        //set post keychange actions
        SharedPreferencesUtils.setOnSharedPrefChanged(this, new CallBackUtil() {
            @Override
            public void eventCallBack(Object keyChanged) {
                Log.d(LOG_TAG, "APP finishAuth() > OnSharedPrefChanged");
                if(keyChanged.equals(CrowdoAccountGeneral.AUTHTOKEN_SHARED_PREF_KEY)){
                    if(mAuthTokenType == null){
                        mAuthTokenType = CrowdoAccountGeneral.AUTHTOKEN_TYPE_ONLINE_ACCESS;
                    }

                    //set AuthToken
                    mAccountManager.setAuthToken(account, mAuthTokenType, authToken);

                    //Alert other apps
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        mAccountManager.notifyAccountAuthenticated(account);
                    }

                    setAccountAuthenticatorResult(intent.getExtras());
                    setResult(RESULT_OK, intent);
                    finish(); //carry on with either AccountManager or In-App Login
                }
            }
        });

        if(accountSuccess) {
            Log.d(LOG_TAG, "APP finishAuth() > account created");

            //Auth Token Store in Shared Pref for easy access
            SharedPreferencesUtils.setSharePrefString(this,
                    CrowdoAccountGeneral.AUTHTOKEN_SHARED_PREF_KEY, authToken);

        }else{
            Log.d(LOG_TAG, "APP finishAuth() > account creation failed");
            Toast.makeText(AuthActivity.this, R.string.auth_account_creation_failed,
                    Toast.LENGTH_SHORT).show();

            //TODO: Possible setResult(RESULT_FAILED) handler here
        }

    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }


    @Override
    protected void onResume() {
        super.onResume();

        //check network and prompt only
        NetworkConnectionChecks.isOnline(this, false);
    }

}
