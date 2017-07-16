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

        final String accountUserName = intent.getStringExtra(AuthActivity.AUTH_MEMBER_NAME);
        final String accountUserEmail = intent.getStringExtra(AuthActivity.AUTH_MEMBER_EMAIL);
        final String accountAuthToken = intent.getStringExtra(AuthActivity.AUTH_MEMBER_TOKEN);
        final String accountUserLocale = intent.getStringExtra(AuthActivity.AUTH_MEMBER_LOCALE);

        Account userAccount = createOrGetAccount(accountUserName);

        storeToken(userAccount, getString(R.string.authentication_ACTION), accountAuthToken);

        storeUserData(userAccount, getString(R.string.authentication_EMAIL), accountUserEmail);
        storeUserData(userAccount, getString(R.string.authentication_LOCALE), accountUserLocale);

        finalizeAuthentication(userAccount);
    }

    private void accountProcedure(final Intent intent, final Bundle userData, final String accountName,
                                  final String accountUserName, final String accountUserEmail,
                                  final String accountPasswordHash, final String authToken){
        //Create Account
//        final Account account = new Account(accountName, CrowdoAccountGeneral.ACCOUNT_TYPE);
//
//        Log.d(LOG_TAG, "APP finishAuth() > addAccountExplicitly");
//        boolean accountSuccess = mAccountManager.addAccountExplicitly(account,
//                accountPasswordHash, userData);
//
//        Log.d(LOG_TAG, "APP finishAuth() > accountSuccess: " + accountSuccess);
//
//        if(accountSuccess) {
//
//            //Auth Token Store in Shared Pref for easy access
//            Log.d(LOG_TAG, "APP finishAuth() > store authToken into SharedPref");
//
//            Realm realm = Realm.getDefaultInstance();
//            realm.executeTransactionAsync(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//                    AccountStore accountStore = realm.createObject(AccountStore.class);
//                    accountStore.setAccountAuthToken(authToken);
//                    accountStore.setAccountUserEmail(accountUserEmail);
//                    accountStore.setAccountUserName(accountUserName);
//                }
//            }, new Realm.Transaction.OnSuccess() {
//                @Override
//                public void onSuccess() {
//                    Log.d(LOG_TAG, "APP finishAuth() > OnSharedPrefChanged eventCallBack");
//
//                    String authTokenType = CrowdoAccountGeneral.AUTHTOKEN_TYPE_ONLINE_ACCESS;
//
//                    //set AuthToken
//                    mAccountManager.setAuthToken(account, authTokenType, authToken);
//
//                    //Alert other apps
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        mAccountManager.notifyAccountAuthenticated(account);
//                    }
//
//                    setAccountAuthenticatorResult(intent.getExtras());
//                    setResult(RESULT_OK, intent);
//                    finish(); //carry on with either AccountManager or In-App Login
//                }
//            });
//            realm.close();
//        }
    }

    @Override
    public void onBackPressed() {
//        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }


    @Override
    protected void onResume() {
        super.onResume();

        //check network and dun show loggout
        NetworkConnectionChecks.isOnline(this, false);
    }

}
