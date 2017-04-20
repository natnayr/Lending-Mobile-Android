package com.crowdo.p2pconnect.oauth;

import android.accounts.AccountManager;
import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.crowdo.p2pconnect.helpers.AuthAccountUtils;
import com.crowdo.p2pconnect.helpers.CallBackUtil;
import com.crowdo.p2pconnect.helpers.SharedPreferencesUtils;

/**
 * Created by cwdsg05 on 20/4/17.
 */

public class CrowdoSessionCheckService extends IntentService{

    public static final String LOG_TAG = CrowdoSessionCheckService.class.getSimpleName();

    public CrowdoSessionCheckService(){
        super("CrowdoSessionCheckService");
    }

    public CrowdoSessionCheckService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(LOG_TAG, "APP CrowdoSessionCheckService onHandleIntent");
        AccountManager accountManager = AccountManager.get(this);

        //remove SharedPref authToken if not in-sync with AccountManager
        AuthAccountUtils.getExisitingAuthToken(accountManager, new CallBackUtil<String>() {
            @Override
            public void eventCallBack(String authToken) {
                Log.d(LOG_TAG, "APP CrowdoSessionCheckService onHandleIntent > getExisitingAuthToken token updated");
                SharedPreferencesUtils.setSharePrefString(CrowdoSessionCheckService.this,
                        CrowdoAccountGeneral.AUTHTOKEN_SHARED_PREF_KEY, authToken);
            }
        });
    }
}
