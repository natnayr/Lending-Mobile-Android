package com.crowdo.p2pconnect;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.crowdo.p2pconnect.data.APIServices;
import com.crowdo.p2pconnect.view.activities.WelcomeActivity;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.SharedPreferencesUtils;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Created by cwdsg05 on 6/1/17.
 */

public class CrowdoApplication extends Application{

    private APIServices apiServices;
    private Scheduler scheduler;
    private Realm realm;
    private static final String LOG_TAG = CrowdoApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        //first thing to do
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        //init mobile db


        Realm.init(getApplicationContext());
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();
        Log.d(LOG_TAG, "APP: Realm DB configured");

        redirectToWelcome(); //if need to redirect
    }

    private static CrowdoApplication get(Context context){
        return (CrowdoApplication) context.getApplicationContext();
    }

    public static CrowdoApplication create(Context context){
        return CrowdoApplication.get(context);
    }

    public Scheduler subscribeScheduler(){
        if(scheduler == null)
            scheduler = Schedulers.io();
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler){
        this.scheduler = scheduler;
    }

    private void redirectToWelcome(){
        int userId = SharedPreferencesUtils.getSharedPrefInt(this,
                ConstantVariables.PREF_KEY_USER_ID, -1);

        //if userId is less than 0, not registered.
        if(userId < 0){
            Intent intent = new Intent(this, WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onTerminate() {
        realm.close();
        super.onTerminate();
    }
}
