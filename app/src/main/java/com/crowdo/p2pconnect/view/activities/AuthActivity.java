package com.crowdo.p2pconnect.view.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.andretietz.retroauth.AuthenticationActivity;
import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.commons.NetworkConnectionChecks;
import com.crowdo.p2pconnect.helpers.LocaleHelper;
import com.crowdo.p2pconnect.view.fragments.LoginFragment;
import com.crowdo.p2pconnect.view.fragments.RegisterFragment;


/**
 * Created by cwdsg05 on 10/3/17.
 */

public class AuthActivity extends AuthenticationActivity {

    private final static String LOG_TAG = AuthActivity.class.getSimpleName();

    public final static String AUTH_MEMBER_EMAIL = "AUTH_MEMBER_EMAIL";
    public final static String AUTH_MEMBER_NAME = "AUTH_MEMBER_NAME";
    public final static String AUTH_MEMBER_TOKEN = "AUTH_MEMBER_TOKEN";
    public final static String AUTH_MEMBER_LOCALE = "AUTH_MEMBER_LOCALE";

    public final static String FRAGMENT_CLASS_TAG_CALL = "AUTH_ACTIVITY_FRAGMENT_CLASS";


    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_auth);

        Log.d(LOG_TAG, "APP AuthActivity onCreate");

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
            Account userAccount = createOrGetAccount(accountUserName);
            storeToken(userAccount, getRequestedTokenType(), accountAuthToken);
            storeUserData(userAccount, getString(R.string.authentication_EMAIL), accountUserEmail);
            storeUserData(userAccount, getString(R.string.authentication_LOCALE), accountUserLocale);
            finalizeAuthentication(userAccount);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onResume() {
        super.onResume();

        //check network and dun show loggout
        NetworkConnectionChecks.isOnline(this, false);
    }

}
