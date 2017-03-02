package com.crowdo.p2pconnect.view.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.widget.Toast;

import com.crowdo.p2pconnect.R;
import com.crowdo.p2pconnect.helpers.SoftInputHelper;
import com.crowdo.p2pconnect.model.RegisteredMemberCheck;
import com.crowdo.p2pconnect.data.RegisteredMemberCheckClient;
import com.crowdo.p2pconnect.helpers.ConstantVariables;
import com.crowdo.p2pconnect.helpers.PerformEmailIdentityCheckTemp;
import com.crowdo.p2pconnect.helpers.SharedPreferencesUtils;

import java.util.Map;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cwdsg05 on 29/12/16.
 */

public class UserSettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener{

    //SharedPreference object uses default provided from PreferenceManager
    SharedPreferences sharedPreferences;
    private static final String LOG_TAG = UserSettingsFragment.class.getSimpleName();
    private Subscription memberCheckSubscription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        sharedPreferences = SharedPreferencesUtils.getSharedPref(getActivity());

        //load preference on Create
        for(int i=0; i<getPreferenceScreen().getPreferenceCount(); i++){
            pickPreferenceObject(getPreferenceScreen().getPreference(i));
        }

        Preference exitSessBtn = findPreference(ConstantVariables.PREF_KEY_USER_LOGOUT);
        exitSessBtn.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Log.d(LOG_TAG, "APP: Session Cleared");
                Toast.makeText(getContext(), getString(R.string.pref_user_session_clear_label),
                        Toast.LENGTH_SHORT).show();

                UserSettingsFragment.resetUserAccountSharedPreferences(getActivity());

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    CookieManager cookieManager = CookieManager.getInstance();
                    cookieManager.removeSessionCookies(new ValueCallback<Boolean>() {
                        @Override
                        public void onReceiveValue(Boolean value) {
                            Log.d(LOG_TAG, "APP: CookieManager.removeSessionCookies onReceiveValue " + value);
                        }
                    });
                    cookieManager.flush();
                }else{
                    CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(getActivity());
                    cookieSyncManager.startSync();
                    CookieManager cookieManager = CookieManager.getInstance();
                    cookieManager.removeAllCookie();
                    cookieManager.removeSessionCookie();
                    cookieSyncManager.stopSync();
                    cookieSyncManager.sync();
                }

                return true;
            }
        });

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {


    }

    private void pickPreferenceObject(Preference pref){
        if(pref instanceof PreferenceCategory) {
            PreferenceCategory prefCategory = (PreferenceCategory) pref;
            for (int i = 0; i < prefCategory.getPreferenceCount(); i++) {
                pickPreferenceObject(prefCategory.getPreference(i));
            }
        }else{
            initSummary(pref);
        }
    }

    private void initSummary(Preference pref){
        if(pref != null) {
            if (pref instanceof EditTextPreference) {
                EditTextPreference editTextPreference = (EditTextPreference) pref;
                pref.setSummary(editTextPreference.getText());
            } else {
                //for static display preferences
                try {
                    Map<String, ?> keys = sharedPreferences.getAll();
                    if(keys.get(pref.getKey()) != null) {
                        if (keys.get(pref.getKey()).getClass().equals(String.class)) {
                            pref.setSummary(sharedPreferences.getString(pref.getKey(), ""));
                        } else if (keys.get(pref.getKey()).getClass().equals(Integer.class)) {
                            pref.setSummary(Integer.toString(sharedPreferences.getInt(pref.getKey(), -1)));
                        } else if (keys.get(pref.getKey()).getClass().equals(Boolean.class)) {
                            pref.setSummary(Boolean.toString(sharedPreferences.getBoolean(pref.getKey(), false)));
                        }
                    }
                }catch (NullPointerException npe){
                    Log.e(LOG_TAG, "ERROR: " + npe.getMessage(), npe);
                }
           }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SoftInputHelper.hideSoftKeyboard(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        if(memberCheckSubscription != null && !memberCheckSubscription.isUnsubscribed()){
            memberCheckSubscription.unsubscribe();
        }
        super.onDestroy();
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        Log.d(LOG_TAG, "APP: Preference Dialog triggered for: " +
                getResources().getResourceName(preference.getLayoutResource()));
        super.onDisplayPreferenceDialog(preference);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(sharedPreferences != null || key != null) {
            Preference pref = findPreference(key);

            Log.d(LOG_TAG, "APP: onSharedPreferenceChanged called with key: " + key);
            if (pref instanceof EditTextPreference) {
                pref.setSummary(sharedPreferences.getString(key, ""));
            } else {
                try {
                    Map<String, ?> keys = sharedPreferences.getAll();
                    if(keys.get(pref.getKey()) != null) {
                        if (keys.get(pref.getKey()).getClass().equals(String.class)) {
                            pref.setSummary(sharedPreferences.getString(pref.getKey(), ""));
                        } else if (keys.get(pref.getKey()).getClass().equals(Integer.class)) {
                            pref.setSummary(Integer.toString(sharedPreferences.getInt(pref.getKey(), -1)));
                        } else if (keys.get(pref.getKey()).getClass().equals(Boolean.class)) {
                            pref.setSummary(Boolean.toString(sharedPreferences.getBoolean(pref.getKey(), false)));
                        }
                    }
                }catch (NullPointerException npe){
                    Log.e(LOG_TAG, "ERROR: " + npe.getMessage(), npe);
                }
            }

            //perform identify user
            if (key.equals(ConstantVariables.PREF_KEY_USER_EMAIL)) {
                performEmailIdentify(sharedPreferences);
            }
        }
    }

    //Temp to update user in settings
    private void performEmailIdentify(SharedPreferences sharedPreferences) {

        final String enteredEmail = sharedPreferences
                .getString(ConstantVariables.PREF_KEY_USER_EMAIL, null)
                .toLowerCase().trim();

        if (enteredEmail == null || enteredEmail.equals("")){
            return;
        }else {

            Log.d(LOG_TAG, "APP: enteredEmail is " + enteredEmail);

            final PerformEmailIdentityCheckTemp idenCheck =
                    new PerformEmailIdentityCheckTemp(getActivity());

            memberCheckSubscription = RegisteredMemberCheckClient.getInstance()
                    .postUserCheck(enteredEmail)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<RegisteredMemberCheck>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            idenCheck.onFailure(LOG_TAG, enteredEmail, e, getView());
                        }

                        @Override
                        public void onNext(RegisteredMemberCheck registeredMemberCheck) {
                            idenCheck.onResponseCode(LOG_TAG, enteredEmail,
                                    registeredMemberCheck, getView());
                        }
                    });
        }
    }

    public static void resetUserAccountSharedPreferences(Context context){
        SharedPreferencesUtils.setSharePrefInt(context,
                ConstantVariables.PREF_KEY_USER_ID,
                -1);
        SharedPreferencesUtils.setSharePrefBool(context,
                ConstantVariables.PREF_KEY_USER_IS_MEMBER,
                false);
        SharedPreferencesUtils.setSharePrefString(context,
                ConstantVariables.PREF_KEY_USER_EMAIL,
                "");
        SharedPreferencesUtils.setSharePrefString(context,
                ConstantVariables.PREF_KEY_USER_NAME,
                "");
        SharedPreferencesUtils.setSharePrefBool(context,
                ConstantVariables.PREF_KEY_USER_INVESTOR_APPROVAL_SGD,
                false);
        SharedPreferencesUtils.setSharePrefBool(context,
                ConstantVariables.PREF_KEY_USER_INVESTOR_APPROVAL_IDR,
                false);
    }
}
