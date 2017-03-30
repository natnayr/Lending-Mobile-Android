package com.crowdo.p2pconnect.helpers;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.os.Build;

import com.crowdo.p2pconnect.oauth.AccountGeneral;

/**
 * Created by cwdsg05 on 29/3/17.
 */

public class AccountManagerUtils {

    public static final String LOG_TAG = AccountManagerUtils.class.getSimpleName();

    public static void removeAccounts(Activity activity){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            AccountManager am = AccountManager.get(activity);
            Account[] accounts = am.getAccountsByType(AccountGeneral.getACCOUNT_TYPE(activity));

            if (accounts.length == 0) return;

            for (Account acc : accounts) {
                am.removeAccount(acc, activity, null, null);
            }
        }
    }

}
