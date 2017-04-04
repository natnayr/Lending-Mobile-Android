package com.crowdo.p2pconnect.view.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.helpers.AccountManagerUtils;
import com.crowdo.p2pconnect.helpers.CallBackInterface;
import com.crowdo.p2pconnect.helpers.LocaleHelper;
import com.crowdo.p2pconnect.oauth.AccountAuthenticatorFragmentActivity;
import com.crowdo.p2pconnect.oauth.AccountGeneral;
import com.crowdo.p2pconnect.view.fragments.LoginFragment;
import com.crowdo.p2pconnect.view.fragments.RegisterFragment;

/**
 * Created by cwdsg05 on 10/3/17.
 */

public class AuthActivity extends AccountAuthenticatorFragmentActivity {

    private final static String LOG_TAG = AuthActivity.class.getSimpleName();

    public final static String ARG_ACCOUNT_EMAIL = "ACCOUNT_EMAIL";
    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TOKEN_TYPE = "AUTH_TOKEN_TYPE";
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";

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

        if(mAuthTokenType == null){
            mAuthTokenType = AccountGeneral.AUTHTOKEN_TYPE_ONLINE_ACCESS;
        }

        if(mAccountType == null){
            mAccountType = AccountGeneral.getACCOUNT_TYPE(this);
        }

        //check if there are accounts before, if so terminate
        ifAccountExistsThenExit();

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

    public void finishAuth(final Intent intent, Bundle userData){
        Log.d(LOG_TAG, "APP: finishAuth()");

        //remove all other accounts
        AccountManagerUtils.removeAccounts(this, new CallBackInterface() {
            @Override
            public void eventCallBack() {
                Bundle extras = intent.getExtras();

                //if not set by activity
                String accountType = extras.getString(AccountManager.KEY_ACCOUNT_TYPE);
                if(accountType == null){
                    accountType = AccountGeneral.getACCOUNT_TYPE(AuthActivity.this);
                }

                String accountName = extras.getString(AccountManager.KEY_ACCOUNT_NAME);

                //Hash Password before storing,
                final Account account = new Account(accountName, accountType);
                String accountPasswordHash = extras.getString(AccountManager.KEY_PASSWORD);
                if(extras.getBoolean(ARG_IS_ADDING_NEW_ACCOUNT, true)){
                    Log.d(LOG_TAG, "APP: finishAuth() > addAccountExplicitly");
                    String authToken = extras.getString(AccountManager.KEY_AUTHTOKEN);
                    String authTokeType = mAuthTokenType;

                    // Creating the account on the device and setting the auth token we got
                    // (Not setting the auth token will cause another call to the server to authenticate the user)
                    mAccountManager.addAccountExplicitly(account, accountPasswordHash, null);
                    mAccountManager.setAuthToken(account, authTokeType, authToken);

                } else {
                    Log.d(LOG_TAG, "APP: finishAuth() > setPassword");
                    mAccountManager.setPassword(account, accountPasswordHash);
                }

                setAccountAuthenticatorResult(extras);
                setResult(RESULT_OK, intent);

                Intent resetIntent = new Intent(AuthActivity.this, MainActivity.class);
                resetIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(resetIntent);
                AuthActivity.this.finish(); //carry on with either AccountManager or In-App Login
            }
        });


    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    private void ifAccountExistsThenExit(){
        Account[] accounts = mAccountManager.getAccountsByType(mAccountType);
        if(accounts.length > 0){
            Toast.makeText(this, R.string.auth_one_account_allowed, Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
