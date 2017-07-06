package com.crowdo.p2pconnect.view.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.commons.NetworkConnectionChecks;
import com.crowdo.p2pconnect.helpers.CallBackUtil;
import com.crowdo.p2pconnect.model.others.AccountStore;
import com.crowdo.p2pconnect.oauth.AuthAccountUtils;
import com.crowdo.p2pconnect.helpers.LocaleHelper;
import com.crowdo.p2pconnect.oauth.AccountAuthenticatorFragmentActivity;
import com.crowdo.p2pconnect.oauth.CrowdoAccountGeneral;
import com.crowdo.p2pconnect.view.fragments.LoginFragment;
import com.crowdo.p2pconnect.view.fragments.RegisterFragment;

import io.realm.Realm;


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

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_auth);

        mAccountManager = AccountManager.get(getBaseContext());
        Bundle extras = getIntent().getExtras();


        String fragmentTag = extras.getString(FRAGMENT_CLASS_TAG_CALL);
        if(fragmentTag != null) {
            Fragment fragment = null;
            if(fragmentTag.equals(LoginFragment.LOGIN_FRAGMENT_TAG)){
                fragment = new LoginFragment();
            }else if(fragmentTag.equals(RegisterFragment.REGISTER_FRAGMENT_TAG)){
                fragment = new RegisterFragment();
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

    public void finishAuth(final Intent intent, final Bundle userData){

        Log.d(LOG_TAG, "APP finishAuth");

        final String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        final String accountUserName = intent.getStringExtra(AuthActivity.POST_AUTH_MEMBER_NAME);
        final String accountUserEmail = intent.getStringExtra(AuthActivity.POST_AUTH_MEMBER_EMAIL);
        final String accountPasswordHash = intent.getStringExtra(AuthActivity.PARAM_USER_PASS_HASH);
        final String authToken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);

        //remove all other accounts
        AuthAccountUtils.removeAccounts(this, new CallBackUtil<Object>() {
            @Override
            public void eventCallBack(Object obj) {
                accountProcedure(intent, userData, accountName,
                        accountUserName, accountUserEmail, accountPasswordHash, authToken);
            }
        });


    }

    private void accountProcedure(final Intent intent, final Bundle userData, final String accountName,
                                  final String accountUserName, final String accountUserEmail,
                                  final String accountPasswordHash, final String authToken){
        //Create Account
        final Account account = new Account(accountName, CrowdoAccountGeneral.ACCOUNT_TYPE);

        Log.d(LOG_TAG, "APP finishAuth() > addAccountExplicitly");
        boolean accountSuccess = mAccountManager.addAccountExplicitly(account,
                accountPasswordHash, userData);

        Log.d(LOG_TAG, "APP finishAuth() > accountSuccess: " + accountSuccess);

        if(accountSuccess) {

            //Auth Token Store in Shared Pref for easy access
            Log.d(LOG_TAG, "APP finishAuth() > store authToken into SharedPref");

            Realm realm = Realm.getDefaultInstance();
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    AccountStore accountStore = realm.createObject(AccountStore.class);
                    accountStore.setAccountAuthToken(authToken);
                    accountStore.setAccountUserEmail(accountUserEmail);
                    accountStore.setAccountUserName(accountUserName);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Log.d(LOG_TAG, "APP finishAuth() > OnSharedPrefChanged eventCallBack");

                    String authTokenType = CrowdoAccountGeneral.AUTHTOKEN_TYPE_ONLINE_ACCESS;

                    //set AuthToken
                    mAccountManager.setAuthToken(account, authTokenType, authToken);

                    //Alert other apps
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        mAccountManager.notifyAccountAuthenticated(account);
                    }

                    setAccountAuthenticatorResult(intent.getExtras());
                    setResult(RESULT_OK, intent);
                    finish(); //carry on with either AccountManager or In-App Login
                }
            });
            realm.close();
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
