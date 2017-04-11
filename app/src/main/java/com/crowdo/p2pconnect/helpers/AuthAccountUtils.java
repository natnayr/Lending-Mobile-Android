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

public class AuthAccountUtils {

    public static final String LOG_TAG = AuthAccountUtils.class.getSimpleName();

    public static void removeAccounts(Activity activity, final CallBackUtil<Object> callBackUtil){

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

        callBackUtil.eventCallBack(null);
    }

    public static void invalidateAuthToken(AccountManager accountManager, String authToken){
        Account account = getOnlyAccount(accountManager);

        if(account != null) {
            accountManager.invalidateAuthToken(account.type, authToken);
        }
    }


    public static Account getOnlyAccount(AccountManager accountManager){
        Account[] accounts = accountManager.getAccounts();
        if(accounts.length > 0){
            return accounts[0];
        }
        return null;
    }

    public static void getExisitingAuthToken(Activity activity, AccountManager accountManager,
                                             final CallBackUtil<String> callback){

        Log.d(LOG_TAG, "APP: getExisitingAuthToken()");

        Account account = AuthAccountUtils.getOnlyAccount(accountManager);
        if(account == null) {
            callback.eventCallBack(null); //return back to callback a null string
            return;
        }

        Log.d(LOG_TAG, "APP: getExisitingAuthToken() > account.name " + account.name);


        accountManager.getAuthToken(account, AccountGeneral.AUTHTOKEN_TYPE_ONLINE_ACCESS, null,
                activity, new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {
                        try{
                            Bundle bundle = future.getResult();
                            String authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);
                            Log.d(LOG_TAG, "APP: getExisitingAuthToken > authToken = "
                                    + authToken);

                            callback.eventCallBack(authToken);

                        }catch (OperationCanceledException oce){
                            Log.e(LOG_TAG, "ERROR: " + oce.getMessage(), oce);
                            oce.printStackTrace();
                        }catch (IOException ioe){
                            Log.e(LOG_TAG, "ERROR: " + ioe.getMessage(), ioe);
                            ioe.printStackTrace();
                        }catch (AuthenticatorException ae){
                            Log.e(LOG_TAG, "ERROR: " + ae.getMessage(), ae);
                            ae.printStackTrace();
                        }
                    }
                }, null);

        Log.d(LOG_TAG, "APP: POST_AUTH_MEMBER_ID => " + accountManager.getUserData(
                AuthAccountUtils.getOnlyAccount(accountManager),
                AuthActivity.POST_AUTH_MEMBER_ID));
        Log.d(LOG_TAG, "APP: POST_AUTH_MEMBER_EMAIL => " + accountManager.getUserData(
                AuthAccountUtils.getOnlyAccount(accountManager),
                AuthActivity.POST_AUTH_MEMBER_EMAIL));
        Log.d(LOG_TAG, "APP: POST_AUTH_MEMBER_NAME => " + accountManager.getUserData(
                AuthAccountUtils.getOnlyAccount(accountManager),
                AuthActivity.POST_AUTH_MEMBER_NAME));
        Log.d(LOG_TAG, "APP: POST_AUTH_MEMBER_LOCALE => " + accountManager.getUserData(
                AuthAccountUtils.getOnlyAccount(accountManager),
                AuthActivity.POST_AUTH_MEMBER_LOCALE));

    }

    public static void actionLogout(AccountManager accountManager, final Activity activity){
        Log.d(LOG_TAG, "APP: actionLogout()");

        //invalidate only account and remove accounts
        AuthAccountUtils.removeAccounts(activity, new CallBackUtil(){
            @Override
            public void eventCallBack(Object o) {
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
