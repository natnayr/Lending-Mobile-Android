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
import com.crowdo.p2pconnect.model.others.AccountStore;
import com.crowdo.p2pconnect.view.activities.LaunchActivity;

import io.realm.Realm;
import io.realm.RealmResults;


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

    public static void removeAccounts(final Activity activity,
                                      final CallBackUtil<Object> callBackUtil){

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
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

                realm.where(AccountStore.class).findAll().deleteAllFromRealm();
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d(LOG_TAG, "APP removeAccounts > removed all accounts & shared pref");
                callBackUtil.eventCallBack(null);
            }
        });
        realm.close();
    }


    public static void invalidateAuthToken(final Activity activity,
                                           final AccountManager accountManager,
                                           final String authToken){

        //invalidate authtoken from realm first then the rest
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<AccountStore> results = realm.where(AccountStore.class).findAll();
                results.deleteAllFromRealm();
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
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
        });
    }

    public static void getAccountManagerAuthToken(final AccountManager accountManager,
                                                  final CallBackUtil<String> callback){

        Log.d(LOG_TAG, "APP getAccountManagerAuthToken()");

        Account account = AuthAccountUtils.getOnlyAccount(accountManager);
        if(account == null) {
            callback.eventCallBack(null); //return back to callback a null string
            return;
        }

        Log.d(LOG_TAG, "APP getAccountManagerAuthToken() > account.name:" + account.name);
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
                    Log.d(LOG_TAG, "APP getAccountManagerAuthToken > authToken");
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


    public static void actionLogout(final Activity activity, final boolean isNewSession){
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
        String authToken = getAuthTokenFromRealm();
        if(authToken != null){
            AuthAccountUtils.invalidateAuthToken(activity, accountManager, authToken);
        }

        //remove accounts
        AuthAccountUtils.removeAccounts(activity, new CallBackUtil<Object>() {
            @Override
            public void eventCallBack(Object obj) {
                String authMessage;
                if(isNewSession){
                    authMessage = activity.getResources().getString(R.string.auth_welcome);
                }else{
                    authMessage = activity.getResources().getString(R.string.auth_session_end_and_logout);
                }
                if(activity != null) {
                    //if activity has not attached itself to a view
                    View rootView = null;
                    try {
                        rootView = activity.getCurrentFocus().getRootView();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        Log.e(LOG_TAG, "ERROR: " + e.getMessage(), e);
                    }


                    if (rootView != null) {
                        //notify user of logout.
                        SnackBarUtil.snackBarForInfoCreate(rootView,
                                authMessage, Snackbar.LENGTH_SHORT).addCallback(new Snackbar.Callback() {
                            @Override
                            public void onShown(Snackbar sb) {
                                super.onShown(sb);
                                goToLaunchActivity(activity);
                            }
                        }).show();
                    } else {
                        goToLaunchActivity(activity);
                    }
                }
            }
        });
    }

    public static String getAuthTokenFromRealm(){
        Realm realm = Realm.getDefaultInstance();
        realm.refresh();

        AccountStore accountStore = realm.where(AccountStore.class).findFirst();
        if(accountStore == null){
            realm.close();
            return null;
        }

        String token = accountStore.getAccountAuthToken();
        if(token == null){
            token = "";
        }
        realm.close();
        return token;
    }

    private static void goToLaunchActivity(Activity activity){
        Intent intent = new Intent(activity, LaunchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NO_ANIMATION |
                Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.finish(); //close activity
    }
}
