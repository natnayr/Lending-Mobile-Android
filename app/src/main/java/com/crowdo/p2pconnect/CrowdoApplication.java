package com.crowdo.p2pconnect;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.crowdo.p2pconnect.helpers.LocaleHelper;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.SharedPreferencesUtils;
import com.crowdo.p2pconnect.oauth.CrowdoAccountGeneral;
import com.crowdo.p2pconnect.oauth.CrowdoSessionCheckService;

import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmException;

/**
 * Created by cwdsg05 on 6/1/17.
 */

public class CrowdoApplication extends Application{

    private Realm realm;
    private RealmConfiguration realmConfig;
    private static final String LOG_TAG = CrowdoApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        configureRealm(); //set configuration

        initApp(); //if need to redirect

        sessionCheckingStart();
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

    private void sessionCheckingStart(){
        Intent checkSessionIntent = new Intent(this, CrowdoSessionCheckService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this,  0, checkSessionIntent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 20); // first time
        long frequency= 20 * 1000; // 20seconds repeat
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(),
                frequency, pendingIntent);

    }
}
