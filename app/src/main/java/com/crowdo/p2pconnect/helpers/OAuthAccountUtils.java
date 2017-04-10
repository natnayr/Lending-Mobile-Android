package com.crowdo.p2pconnect.helpers;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;

import com.crowdo.p2pconnect.oauth.AccountGeneral;
import com.crowdo.p2pconnect.view.activities.AuthActivity;
import com.crowdo.p2pconnect.view.activities.LaunchActivity;

import java.io.IOException;

/**
 * Created by cwdsg05 on 29/3/17.
 */

public class OAuthAccountUtils {

    public static final String LOG_TAG = OAuthAccountUtils.class.getSimpleName();

    public static void removeAccounts(Activity activity, final CallBackInterface callBackUtil){

        AccountManager am = AccountManager.get(activity);
        Account[] accounts = am.getAccountsByType(AccountGeneral.getACCOUNT_TYPE(activity));
        if(accounts.length != 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                for (Account acc : accounts) {
                    am.clearPassword(acc);
                    am.removeAccountExplicitly(acc);
                }
            } else {
                for (Account acc : accounts) {
                    am.clearPassword(acc);
                    am.removeAccount(acc, null, null);
                }
            }
        }

        callBackUtil.eventCallBack();
    }

    public static void invalidateAuthToken(AccountManager accountManager){
        accountManager.invalidateAuthToken(getOnlyAccount(accountManager).type,
                getExisitingAuthToken(accountManager));
    }


    public static Account getOnlyAccount(AccountManager accountManager){
        Account[] accounts = accountManager.getAccounts();
        if(accounts.length == 1){
            return accounts[0];
        }
        return null;
    }

    public static String getExisitingAuthToken(AccountManager accountManager){

        Log.d(LOG_TAG, "APP: getExisitingAuthToken()");

        String mAuthToken = accountManager.peekAuthToken(OAuthAccountUtils.getOnlyAccount(accountManager),
                AccountGeneral.AUTHTOKEN_TYPE_ONLINE_ACCESS);

        Log.d(LOG_TAG, "APP: AccountManager mAuthToken is " + mAuthToken);

        Log.d(LOG_TAG, "APP: POST_AUTH_MEMBER_ID => " + accountManager.getUserData(
                OAuthAccountUtils.getOnlyAccount(accountManager),
                AuthActivity.POST_AUTH_MEMBER_ID));
        Log.d(LOG_TAG, "APP: POST_AUTH_MEMBER_EMAIL => " + accountManager.getUserData(
                OAuthAccountUtils.getOnlyAccount(accountManager),
                AuthActivity.POST_AUTH_MEMBER_EMAIL));
        Log.d(LOG_TAG, "APP: POST_AUTH_MEMBER_NAME => " + accountManager.getUserData(
                OAuthAccountUtils.getOnlyAccount(accountManager),
                AuthActivity.POST_AUTH_MEMBER_NAME));
        Log.d(LOG_TAG, "APP: POST_AUTH_MEMBER_LOCALE => " + accountManager.getUserData(
                OAuthAccountUtils.getOnlyAccount(accountManager),
                AuthActivity.POST_AUTH_MEMBER_LOCALE));

        return mAuthToken;

    }

    public static void actionLogout(AccountManager accountManager, final Activity activity){
        Log.d(LOG_TAG, "APP: actionLogout()");

        //invalidate only account and remove accounts
        OAuthAccountUtils.invalidateAuthToken(accountManager);
        OAuthAccountUtils.removeAccounts(activity, new CallBackInterface(){
            @Override
            public void eventCallBack() {
                //Call LaunchActivity to Welcome & Authenticate
                Intent intent = new Intent(activity, LaunchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);
                activity.finish();
            }
        });

        //clear cookie cache fro webview
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeSessionCookies(new ValueCallback<Boolean>() {
                @Override
                public void onReceiveValue(Boolean value) {
                    Log.d(LOG_TAG, "APP: CookieManager.removeSessionCookies onReceiveValue " + value);
                }
            });
            cookieManager.flush();
        }else{
            CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(activity);
            cookieSyncManager.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncManager.stopSync();
            cookieSyncManager.sync();
        }
    }
}
