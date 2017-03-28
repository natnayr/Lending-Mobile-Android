package com.crowdo.p2pconnect.oauth;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.view.activities.AuthActivity;
import com.crowdo.p2pconnect.view.fragments.LoginFragment;

/**
 * Created by cwdsg05 on 9/3/17.
 */

public class AccountAuthenticator extends AbstractAccountAuthenticator {

    private static final String LOG_TAG = AccountAuthenticator.class.getSimpleName();
    private final Context mContext;

    public AccountAuthenticator(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType,
                             String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        Log.v(LOG_TAG, "APP: addAccount()");

        final Intent intent = new Intent(mContext, AuthActivity.class);
        intent.putExtra(AuthActivity.FRAGMENT_CLASS_TAG_CALL, LoginFragment.LOGIN_FRAGMENT_TAG);
        intent.putExtra(AuthActivity.ARG_ACCOUNT_TYPE, accountType);
        intent.putExtra(AuthActivity.ARG_AUTH_TOKEN_TYPE, authTokenType);
        intent.putExtra(AuthActivity.ARG_IS_ADDING_NEW_ACCOUNT, true);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);

        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account,
                               String authTokenType, Bundle options) throws NetworkErrorException {
        Log.v(LOG_TAG, "APP: getAuthToken()");

        //Check token Auth Type
        if(!AccountGeneral.AUTHTOKEN_TYPE_ONLINE_ACCESS.equals(authTokenType)){
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ERROR_MESSAGE, mContext.getString(R.string.auth_invalid_token));
            return result;
        }

        final AccountManager accountManager = AccountManager.get(mContext);
        String authToken = accountManager.peekAuthToken(account, authTokenType);
        Log.d(LOG_TAG, "APP: peekAuthToken returned - " + authToken);

        //if not empty authtoken
        if(!TextUtils.isEmpty(authToken)){
            final Bundle bundle = new Bundle();
            bundle.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            bundle.putString(AccountManager.KEY_AUTHTOKEN, authToken);
            return bundle;
        }

        //else call login intent
        final Intent intent = new Intent(mContext, AuthActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        intent.putExtra(AuthActivity.ARG_ACCOUNT_EMAIL, account.name);
        intent.putExtra(AuthActivity.ARG_ACCOUNT_TYPE, account.type);
        intent.putExtra(AuthActivity.ARG_AUTH_TOKEN_TYPE, authTokenType);


        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);

        return bundle;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {

        if(AccountGeneral.AUTHTOKEN_TYPE_ONLINE_ACCESS.equals(authTokenType)) {
            return AccountGeneral.AUTHTOKEN_TYPE_ONLINE_ACCESS_LABEL;
        }else{
            return authTokenType + " (Label)";
        }
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        final Bundle result = new Bundle();
        result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
        return result;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

}
