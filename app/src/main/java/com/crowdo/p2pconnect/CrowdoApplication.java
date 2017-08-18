package com.crowdo.p2pconnect;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.crowdo.p2pconnect.helpers.LocaleHelper;
import com.crowdo.p2pconnect.helpers.ConstantVariables;


import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by cwdsg05 on 6/1/17.
 */

public class CrowdoApplication extends MultiDexApplication{

    private Realm realm;
    private RealmConfiguration realmConfig;
    private static final String LOG_TAG = CrowdoApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        configureRealm(); //set configuration

//        initApp(); //if need to redirect

//        sessionCheckingStart();
    }

    private static CrowdoApplication get(Context context){
        return (CrowdoApplication) context.getApplicationContext();
    }

    public static CrowdoApplication create(Context context){
        return CrowdoApplication.get(context);
    }

    private void configureRealm(){
        //init mobile db
        Realm.init(getApplicationContext());
        realmConfig = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .schemaVersion(ConstantVariables.REALM_DB_VERSION)
                .build();

        Realm.setDefaultConfiguration(realmConfig);
        realm = Realm.getDefaultInstance();
        Log.d(LOG_TAG, "APP Realm DB configured to revision " + realm.getVersion());
    }


    private void initApp(){
        //init Learning DB Marker
//        SharedPreferencesUtils.setSharePrefBool(this,
//                ConstantVariables.PREF_KEY_LOADED_LEARNINGCENTER_DB, false);
    }

    @Override
    public void onTerminate() {
        realm.close();
        super.onTerminate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        MultiDex.install(this);
        super.attachBaseContext(LocaleHelper.onAttach(base, ConstantVariables.APP_LANG_DEFAULT));
    }

}
