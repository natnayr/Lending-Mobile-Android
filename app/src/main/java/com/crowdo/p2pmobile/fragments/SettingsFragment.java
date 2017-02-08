package com.crowdo.p2pmobile.fragments;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;

import com.crowdo.p2pmobile.R;
import com.crowdo.p2pmobile.data.RegisteredMemberCheck;
import com.crowdo.p2pmobile.data.RegisteredMemberCheckClient;
import com.crowdo.p2pmobile.helpers.ConstantVariables;
import com.crowdo.p2pmobile.helpers.PerformEmailIdentityCheckTemp;
import com.crowdo.p2pmobile.helpers.SharedPreferencesUtils;
import com.crowdo.p2pmobile.helpers.SnackBarUtil;

import java.util.Map;

import butterknife.BindString;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cwdsg05 on 29/12/16.
 */

public class SettingsFragment extends PreferenceFragmentCompat
        implements SharedPreferences.OnSharedPreferenceChangeListener{

    //SharedPreference object uses default provided from PreferenceManager
    SharedPreferences sharedPreferences;
    private static final String LOG_TAG = SettingsFragment.class.getSimpleName();
    public static final String TAG_SETTINGS_FRAGMENT = "LOAN_DETAILS_FRAGMENT_TAG";
    private Subscription memberCheckSubscription;

    @BindString(R.string.okay_label) String mLabelOkay;
    @BindString(R.string.pref_user_session_clear_label) String mLabelSessionClear;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        sharedPreferences = SharedPreferencesUtils.getSharedPref(getActivity());

        //load preference on Create
        for(int i=0; i<getPreferenceScreen().getPreferenceCount(); i++){
            pickPreferenceObject(getPreferenceScreen().getPreference(i));
        }

        Preference exitSessBtn = findPreference(ConstantVariables.PREF_KEY_USER_LOGOUT_KEY);
        exitSessBtn.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Log.d(LOG_TAG, "APP: Session Cleared");
                int colorIconText = ContextCompat.getColor(getContext(), R.color.color_icons_text);

                final Snackbar snackBarOnNext = SnackBarUtil.snackBarCreate(getView(),
                        mLabelSessionClear, colorIconText);
                snackBarOnNext.setAction(mLabelOkay, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackBarOnNext.dismiss();
                    }
                }).show();

                SharedPreferencesUtils.resetUserAccountSharedPreferences(getActivity());

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
                    Log.d(LOG_TAG, "APP: Remove Cookies API < " + String.valueOf(Build.VERSION_CODES.LOLLIPOP));
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
}
