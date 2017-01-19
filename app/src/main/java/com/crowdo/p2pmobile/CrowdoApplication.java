package com.crowdo.p2pmobile;

import android.app.Application;
import android.content.Intent;
import android.support.v7.preference.PreferenceManager;

import com.crowdo.p2pmobile.activity.WelcomeActivity;
import com.crowdo.p2pmobile.helper.SharedPreferencesUtils;

/**
 * Created by cwdsg05 on 6/1/17.
 */

public class CrowdoApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        //first thing to do
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        redirectToWelcome(); //if need to redirect

    }

    private void redirectToWelcome(){
        int userId = SharedPreferencesUtils.getSharedPrefInt(this,
                getString(R.string.pref_user_id_key), -1);

        //if userId is less than 0, not registered.
        if(userId < 0){
            Intent intent = new Intent(this, WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
