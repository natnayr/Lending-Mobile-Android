package com.crowdo.p2pconnect;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.crowdo.p2pconnect.data.APIServices;
import com.crowdo.p2pconnect.helpers.LocaleHelper;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.SharedPreferencesUtils;
import com.crowdo.p2pconnect.oauth.CrowdoAccountGeneral;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmException;
import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Created by cwdsg05 on 6/1/17.
 */

public class CrowdoApplication extends Application{

    private APIServices apiServices;
    private Scheduler scheduler;
    private Realm realm;
    private RealmConfiguration realmConfig;
    private static final String LOG_TAG = CrowdoApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        configureRealm(); //set configuration

        initApp(); //if need to redirect
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

    public void setScheduler(Scheduler scheduler){
        this.scheduler = scheduler;
    }

    private void initApp(){
        //check with authToken
        String authToken = SharedPreferencesUtils.getSharedPrefString(this,
                CrowdoAccountGeneral.AUTHTOKEN_SHARED_PREF_KEY, null);

        //if userId is less than 0, not registered & clean db
        if(authToken == null){
            //destroy and recreate db
            if(realm.isClosed() == false){
                realm.close();
            }

            try {
                SharedPreferencesUtils.setSharePrefBool(this,
                        ConstantVariables.PREF_KEY_LOADED_LEARNINGCENTER_DB, false);
                Realm.deleteRealm(realmConfig);
                configureRealm(); //reconfigure and init realm
            }catch (RealmException rx){
                Log.d(LOG_TAG, "ERROR: "+rx.getMessage(), rx);
                rx.printStackTrace();
            }
        }
    }

    @Override
    public void onTerminate() {
        realm.close();
        super.onTerminate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, ConstantVariables.APP_LANG_DEFAULT));
    }
}
