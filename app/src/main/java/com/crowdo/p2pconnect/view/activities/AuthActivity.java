package com.crowdo.p2pconnect.view.activities;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.helpers.LocaleHelper;
import com.crowdo.p2pconnect.oauth.AccountAuthenticatorFragmentActivity;
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

    private AccountManager mAccountManager;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_auth);
        Dart.inject(this);

        mAccountManager = AccountManager.get(getBaseContext());
        Bundle extras = getIntent().getExtras();
        Class fragmentClass = (Class<Activity>) extras.getSerializable(FRAGMENT_CLASS_CALL);


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
                fragment.setArguments(extras);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.auth_content, fragment)
                        .commit();
            }
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }
}
