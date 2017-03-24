package com.crowdo.p2pconnect.oauth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by cwdsg05 on 24/3/17.
 */

public class AccountAuthenticatorService extends Service{

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //service extended for AccountManager
        AccountAuthenticator authAuthenticator = new AccountAuthenticator(this);
        return authAuthenticator.getIBinder();
    }
}
