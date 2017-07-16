package com.crowdo.p2pconnect.oauth;

import android.accounts.AccountManager;
import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.crowdo.p2pconnect.helpers.CallBackUtil;
import com.crowdo.p2pconnect.model.others.AccountStore;

import io.realm.Realm;

/**
 * Created by cwdsg05 on 20/4/17.
 */

public class CrowdoSessionCheckService extends IntentService{

    public AccountManager mAccountManager;

    public CrowdoSessionCheckService(){
        super("CrowdoSessionCheckService");
    }

    public CrowdoSessionCheckService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
//        Log.d(LOG_TAG, "APP CrowdoSessionCheckService onHandleIntent");
        mAccountManager = AccountManager.get(getApplicationContext());


        //remove SharedPref authToken if not in-sync with AccountManager
//        AuthAccountUtils.getAccountManagerAuthToken(mAccountManager, new CallBackUtil<String>() {
//            @Override
//            public void eventCallBack(final String authToken) {
//                Log.d(LOG_TAG, "APP CrowdoSessionCheckService onHandleIntent > " +
//                        "getAccountManagerAuthToken token updated ");
//
//                Realm realm = Realm.getDefaultInstance();
//                realm.executeTransactionAsync(new Realm.Transaction() {
//                    @Override
//                    public void execute(Realm realm) {
//                        if(authToken != null && realm.where(AccountStore.class).count() > 0) {
//                            //if authToken not null and AccountStore has record
//                            AccountStore accountStore = realm.where(AccountStore.class).findFirst();
//                            accountStore.setAccountAuthToken(authToken);
//                        }else{
//                            //delete all
//                            realm.where(AccountStore.class).findAll().deleteAllFromRealm();
//                        }
//                    }
//                });
//                realm.close();
//            }
//        });
    }
}
