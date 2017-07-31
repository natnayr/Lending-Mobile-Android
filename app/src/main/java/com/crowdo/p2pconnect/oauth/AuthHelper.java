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
 * Created by cwdsg05 on 20/7/17.
 */

public class AuthHelper {

    public static void invalidateTokenAndMakeCall(Activity activity, CallBackUtil<MemberInfoResponse> callBackUtil){
        AuthAccountManager authAccountManager = new AuthAccountManager();
        Account activeAccount = authAccountManager
                .getActiveAccount(activity.getString(R.string.authentication_ACCOUNT));
        if(activeAccount != null) {
            AccountManager.get(activity).setAuthToken(activeAccount,
                    activity.getString(R.string.authentication_TOKEN),
                    activity.getString(R.string.authentication_INVALID_TOKEN));
            //call member retreival
            new MemberInfoRetrieval().retrieveInfo(activity,
                    callBackUtil);

        }

    }
}
