package com.crowdo.p2pconnect.oauth;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;

import com.andretietz.retroauth.AuthenticationService;
import com.crowdo.p2pconnect.R;

/**
 * 
 */

public class AuthService extends AuthenticationService {

    @Override
    public String getLoginAction(Context context) {
        return context.getString(R.string.authentication_ACTION);
    }
}
