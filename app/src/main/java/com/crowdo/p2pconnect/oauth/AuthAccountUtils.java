package com.crowdo.p2pconnect.oauth;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.helpers.CallBackUtil;
import com.crowdo.p2pconnect.helpers.SharedPreferencesUtils;
import com.crowdo.p2pconnect.helpers.SnackBarUtil;
import com.crowdo.p2pconnect.view.activities.LaunchActivity;


/**
 * Created by cwdsg05 on 29/3/17.
 */

public class AuthAccountUtils {

    public static final String LOG_TAG = AuthAccountUtils.class.getSimpleName();

    private static Account getOnlyAccount(AccountManager accountManager){


        Account[] accounts = accountManager.getAccountsByType(CrowdoAccountGeneral.ACCOUNT_TYPE);
        for(Account acc : accounts) {
            Log.d(LOG_TAG, "APP getOnlyAccount > accounts.name = " + acc.name + " , accounts.type = " + acc.type);
        }
        if(accounts.length > 0){
            return accounts[0];
        }
        return null;
    }

    public static void removeAccounts(final Activity activity){

        AccountManager am = AccountManager.get(activity);
        Account[] accounts = am.getAccountsByType(CrowdoAccountGeneral.ACCOUNT_TYPE);
        if(accounts.length > 0) {
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
    }


    public static void invalidateAuthToken(final Activity activity, final AccountManager accountManager, String authToken){

        //invalidate SharedPref
        SharedPreferencesUtils.setSharePrefString(activity,
                CrowdoAccountGeneral.AUTHTOKEN_SHARED_PREF_KEY, null);

        final Account account = getOnlyAccount(accountManager);
        if(account != null) {
            final AccountManagerFuture<Bundle> future = accountManager.getAuthToken(account,
                    authToken, null, activity, null, null);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Bundle bnd = future.getResult();
                        final String authToken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
                        accountManager.invalidateAuthToken(account.type, authToken);
                    } catch (Exception e) {
                        Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static void getExisitingAuthToken(final AccountManager accountManager,
                                             final CallBackUtil<String> callback){

        Log.d(LOG_TAG, "APP getExisitingAuthToken()");

        Account account = AuthAccountUtils.getOnlyAccount(accountManager);
        if(account == null) {
            callback.eventCallBack(null); //return back to callback a null string
            return;
        }

        Log.d(LOG_TAG, "APP getExisitingAuthToken() > account.name:" + account.name);
        final AccountManagerFuture<Bundle> future =
                accountManager.getAuthToken(account,
                        CrowdoAccountGeneral.AUTHTOKEN_TYPE_ONLINE_ACCESS,
                        null, false, null, null);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Bundle bundle = future.getResult();
                    String authToken = bundle.getString(AccountManager.KEY_AUTHTOKEN);
                    Log.d(LOG_TAG, "APP getExisitingAuthToken > authToken");
                    callback.eventCallBack(authToken);
                }catch (Exception e){
                    Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void actionLogout(final Activity activity){
        //for signing out that have logged in.
        actionLogout(activity, false);
    }


    public static void actionLogout(final Activity activity, boolean isNewSession){
        Log.d(LOG_TAG, "APP actionLogout()");


        //begin process
        AccountManager accountManager = AccountManager.get(activity);

        //clear cookie cache fro webview
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeSessionCookies(new ValueCallback<Boolean>() {
                @Override
                public void onReceiveValue(Boolean value) {
                    Log.d(LOG_TAG, "APP CookieManager.removeSessionCookies onReceiveValue " + value);
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

        //invalidate authKey if avalible
        String authToken = SharedPreferencesUtils.getSharedPrefString(activity,
                CrowdoAccountGeneral.AUTHTOKEN_SHARED_PREF_KEY, null);
        if(authToken != null){
            AuthAccountUtils.invalidateAuthToken(activity, accountManager, authToken);
        }

        //remove accounts
        AuthAccountUtils.removeAccounts(activity);

        String authMessage;
        if(isNewSession){
            authMessage = activity.getResources().getString(R.string.auth_welcome);
        }else{
            authMessage = activity.getResources().getString(R.string.auth_session_end_and_logout);
        }

        //if activity has not attached itself to a view
        View rootView = null;
        try{
            rootView = activity.getCurrentFocus().getRootView();
        }catch (NullPointerException e){
            e.printStackTrace();
            Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
        }


        if(rootView != null) {
            //notify user of logout.
            SnackBarUtil.snackBarForInfoCreate(rootView,
                    authMessage, Snackbar.LENGTH_SHORT).addCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);
                    goToLaunchActivity(activity);
                }
            }).show();
        }else{
            goToLaunchActivity(activity);
        }
    }

    private static void goToLaunchActivity(Activity activity){
        Intent intent = new Intent(activity, LaunchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        activity.startActivity(intent);
        activity.finish(); //close activity
    }
}
