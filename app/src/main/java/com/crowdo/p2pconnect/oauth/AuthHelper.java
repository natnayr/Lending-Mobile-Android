package com.crowdo.p2pconnect.oauth;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;

import com.andretietz.retroauth.AuthAccountManager;
import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.support.MemberInfoRetrieval;
import com.crowdo.p2pconnect.helpers.CallBackUtil;
import com.crowdo.p2pconnect.model.response.MemberInfoResponse;

/**
 * Custom Helper class using RetroAuth to change AccountManager status
 */
public class AuthHelper {

    /**
     * To force Logout via RetroAuth token invalidation and do casual API call.
     *
     * @param activity current Activity being called
     * @param callBackUtil handle during post-logout (currently nothing)
     */
    public static void invalidateTokenAndMakeCall(Activity activity, CallBackUtil<MemberInfoResponse> callBackUtil){
        AuthAccountManager authAccountManager = new AuthAccountManager();
        Account activeAccount = authAccountManager
                .getActiveAccount(activity.getString(R.string.authentication_ACCOUNT));
        if(activeAccount != null) {
            AccountManager.get(activity).setAuthToken(activeAccount,
                    activity.getString(R.string.authentication_TOKEN),
                    activity.getString(R.string.authentication_INVALID_TOKEN));


            //fake API call to MemberInfo to trigger AuthActivity via 401 status
            new MemberInfoRetrieval().retrieveInfo(activity,
                    callBackUtil);

        }

    }
}
