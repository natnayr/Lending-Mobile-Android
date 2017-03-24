package com.crowdo.p2pconnect.view.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.helpers.LocaleHelper;
import com.crowdo.p2pconnect.oauth.AccountAuthenticatorFragmentActivity;
import com.crowdo.p2pconnect.oauth.AccountGeneral;
import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;

/**
 * Created by cwdsg05 on 10/3/17.
 */

public class AuthActivity extends AccountAuthenticatorFragmentActivity {

    private final static String LOG_TAG = AuthActivity.class.getSimpleName();

    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
    public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";

    public final static String KEY_ERROR_MESSAGE = "ERR_MSG";

    public final static String PARAM_USER_PASS = "USER_PASS";

    public final static String FRAGMENT_CLASS_CALL = "FRAGMENT_CLASS";
    private final int REQ_SIGNUP = 1;

    @Nullable @InjectExtra Class fragmentClass;

    private AccountManager mAccountManager;
    private String mAuthTokenType;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_auth);
        Dart.inject(this);

        mAccountManager = AccountManager.get(getBaseContext());

        String accountName = getIntent().getStringExtra(ARG_ACCOUNT_NAME);
        mAuthTokenType = getIntent().getStringExtra(ARG_AUTH_TYPE);

        if(mAuthTokenType == null){
            mAuthTokenType = AccountGeneral.AUTHTOKEN_TYPE_ONLINE_ACCESS;
        }


        if(fragmentClass != null) {
            Fragment fragment = null;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            }catch (Exception e) {
                Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
                e.printStackTrace();
            }

            if(fragment != null) {
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_SIGNUP && resultCode == RESULT_OK) {
            finishLogin(data);
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void finishLogin(Intent intent){
        Log.d(LOG_TAG, "APP: finishLogin()");
        String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        String accountPassword = intent.getStringExtra(PARAM_USER_PASS);
        final Account account = new Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));

        if(getIntent().getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)){
            Log.d(LOG_TAG, "APP: finishLogin() > addAccountExplicitly");
            String authToken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
            String authTokeType = mAuthTokenType;

            // Creating the account on the device and setting the auth token we got
            // (Not setting the auth token will cause another call to the server to authenticate the user)
            mAccountManager.addAccountExplicitly(account, accountPassword, null);
            mAccountManager.setAuthToken(account, authTokeType, authToken);
        } else {
            Log.d(LOG_TAG, "APP: finishLogin() > setPassword");
            mAccountManager.setPassword(account, accountPassword);
        }

        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }

}
