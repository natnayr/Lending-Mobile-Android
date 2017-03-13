package com.crowdo.p2pconnect.view.activities;

import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.os.Bundle;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.oauth.AccountAuthenticatorFragmentActivity;
import com.crowdo.p2pconnect.view.fragments.LoginFragment;
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

    @InjectExtra public String fragmentClass;
    private AccountManager mAccountManager;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_auth);
        Dart.inject(this);

        mAccountManager = AccountManager.get(getBaseContext());
        if(fragmentClass != null) {
            if (fragmentClass.equals(LoginFragment.class.getName())) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.auth_content, new LoginFragment())
                        .commit();
            }
        }



    }



}
